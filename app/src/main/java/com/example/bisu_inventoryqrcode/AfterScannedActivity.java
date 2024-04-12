package com.example.bisu_inventoryqrcode;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AfterScannedActivity extends AppCompatActivity {

    AppCompatButton report_btn, req_btn;
    private String ipAddress = "";
    String userID, propertyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_scanned);

        ipAddress = new IPAddressManager(getApplicationContext()).getIPAddress();
        userID = getIntent().getStringExtra("UserID");

        // Retrieve the scannedResult value passed from the previous activity
        String scannedResult = getIntent().getStringExtra("ScannedResult");

        // Display the name of the item, its image, and the StockAvailable corresponding to the scannedResult
        final TextView itemNameTextView = findViewById(R.id.descrip);
        final TextView stockAvailableTextView = findViewById(R.id.stock);
        final ImageView itemImageView = findViewById(R.id.image_view);
        req_btn = findViewById(R.id.req_btn);
        report_btn = findViewById(R.id.report_btn);



        // Make an HTTP request to fetch the item name, image URL, and StockAvailable from the PHP script
        String url = "http://192.168.137.141/LoginRegister/getInventoryItemInfo.php?PropertyNumber=" + scannedResult;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Check if the response contains the item name, image URL, and StockAvailable
                            if (response.has("itemName") && response.has("imageURL") && response.has("stockAvailable")) {
                                // Extract the item name, image URL, and StockAvailable from the response
                                String itemName = response.getString("itemName");
                                String imageURL = response.getString("imageURL");
                                String stockAvailable = response.getString("stockAvailable");
                                propertyID= response.getString("propertyID");

                                if (stockAvailable == null || stockAvailable.isEmpty() || stockAvailable.equals("null")) {
                                    stockAvailable = "0";
                                }

                                // Display the item name, image, and StockAvailable in the respective TextViews
                                itemNameTextView.setText(itemName);
                                stockAvailableTextView.setText(stockAvailable);
                                // Load and display the image using Glide with the base URL
                                Glide.with(AfterScannedActivity.this)
                                        .load("http://192.168.137.141/BISU_SupplyManagementQRCode/uploads/pictures/" + imageURL)
                                        .into(itemImageView);
                            } else {
                                // Display an error message if item name, image URL, or StockAvailable not found
                                Toast.makeText(AfterScannedActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Display an error message if there's an error parsing the response
                            Toast.makeText(AfterScannedActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Display an error message if there's an error making the request
                        Toast.makeText(AfterScannedActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);


        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a LayoutInflater instance
                LayoutInflater inflater = LayoutInflater.from(AfterScannedActivity.this);

                // Inflate the custom layout for the reason input
                View reasonView = inflater.inflate(R.layout.reason_input_dialog, null);

                // Find the EditText in the custom layout
                EditText reasonEditText = reasonView.findViewById(R.id.reason_edit_text);

                // Create and show the AlertDialog with custom layout
                AlertDialog.Builder builder = new AlertDialog.Builder(AfterScannedActivity.this);
                builder.setTitle("Report Item");
                builder.setView(reasonView); // Set the custom layout containing the reason input EditText
                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve the reason entered by the user
                        String details = reasonEditText.getText().toString();

                        // Create and show the confirmation dialog
                        AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(AfterScannedActivity.this);
                        confirmationBuilder.setTitle("Confirmation");
                        confirmationBuilder.setMessage("Are you sure you want to report the item '" + itemNameTextView.getText() + "'?");
                        confirmationBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reportItem(details);
                            }
                        });
                        confirmationBuilder.setNegativeButton("No", null);
                        confirmationBuilder.show(); // Show the confirmation dialog
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show(); // Show the AlertDialog with custom layout
            }
        });

    }
    private void reportItem(String details) {
        // Prepare data for POST request
        String[] field = {"ReportedDate", "Details", "UserID", "PropertyID"};
        String[] data = {getCurrentDateTime(), details, userID, propertyID};

        // Perform POST request using PutData
        PutData putData = new PutData(ipAddress + "/LoginRegister/report_item.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");
                    Toast.makeText(AfterScannedActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AfterScannedActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(AfterScannedActivity.this, "Error sending request", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}

