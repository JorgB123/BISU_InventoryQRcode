package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView stock, descrip;
    AppCompatButton report_btn, req_btn;

    String propertyNumber, userID, description, stockAvailable, propertyID;
    private String ipAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize views
        ipAddress = new IPAddressManager(getApplicationContext()).getIPAddress();
        imageView = findViewById(R.id.image_view);
        stock = findViewById(R.id.stock);
        descrip = findViewById(R.id.descrip);
        report_btn = findViewById(R.id.report_btn);
        req_btn = findViewById(R.id.req_btn);

        // Set onClickListener for req_btn
        req_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ItemDetails.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("PropertyNumber", propertyNumber);
                intent.putExtra("PropertyID", propertyID);
                intent.putExtra("Description", description);
                intent.putExtra("StockAvailable", stockAvailable);
                startActivity(intent);
            }
        });

        // Set onClickListener for report_btn
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a LayoutInflater instance
                LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);

                // Inflate the custom layout for the reason input
                View reasonView = inflater.inflate(R.layout.reason_input_dialog, null);

                // Find the EditText in the custom layout
                EditText reasonEditText = reasonView.findViewById(R.id.reason_edit_text);

                // Create and show the AlertDialog with custom layout
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("Report Item");
                builder.setView(reasonView); // Set the custom layout containing the reason input EditText
                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve the reason entered by the user
                        String details = reasonEditText.getText().toString();

                        // Create and show the confirmation dialog
                        AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(DetailActivity.this);
                        confirmationBuilder.setTitle("Confirmation");
                        confirmationBuilder.setMessage("Are you sure you want to report the item '" + description + "'?");
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

        // Retrieve intent data
        Intent intent = getIntent();
        if (intent != null) {
            description = intent.getStringExtra("Description");
            stockAvailable = intent.getStringExtra("StockAvailable");
            String image = intent.getStringExtra("Image");
            propertyNumber = getIntent().getStringExtra("PropertyNumber");
            userID = getIntent().getStringExtra("UserID");
            propertyID = getIntent().getStringExtra("PropertyID");

            // Set data to views
            descrip.setText(description);
            stock.setText(stockAvailable);
            // Load image using Glide
            Glide.with(this).load("http://192.168.1.16/LoginRegister/item_images/" + image).into(imageView);
        }
    }

    // Method to report item
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
                    Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(DetailActivity.this, "Error sending request", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
