package com.example.bisu_inventoryqrcode;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ItemDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView imageView;
    private TextView descrip, stock, propertyID, borrowers_id;
    Button request_button, report_button;
    private EditText  date, time, quantity, purposeEditText;

    String userID;

    private Calendar calendar;

    private String ipAddress = "";

    ImageView backtoReq;
    private ProgressDialog progressDialog;
    String fn, role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        ipAddress = ipAddressManager.getIPAddress();
        // Initialize views
       // imageView = findViewById(R.id.image_view);
        stock = findViewById(R.id.stock);
        descrip = findViewById(R.id.descrip);
        propertyID = findViewById(R.id.propertyNumber);
        date = findViewById(R.id.dateAcquired);
        time = findViewById(R.id.time);
        quantity = findViewById(R.id.quantity);
        //purposeEditText = findViewById(R.id.purpose);
        borrowers_id = findViewById(R.id.borrowers_id);
        request_button=findViewById(R.id.request_button);

        fn = getIntent().getStringExtra("FirstName");
        role = getIntent().getStringExtra("Role");
        // After initializing fn
        Log.d("ItemDetails", "First Name: " + fn);
        Log.d("ItemDetails", "Role: " + role);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);

        backtoReq=findViewById(R.id.backtoReq);

        backtoReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        calendar = Calendar.getInstance();

        // Set current date to dateEditText
        updateDate();

        // Set current time to timeEditText
        updateTime();

        // Instantiate serverUrl
       // String serverUrl = ipAddress + "/LoginRegister/request_item.php";
        Log.d("ip daw", ipAddress);

// Set OnClickListener for request_button
        // Set OnClickListener for request_button
        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String dateAcquired = date.getText().toString();
                String timeAcquired = time.getText().toString();
                String itemQuantity = quantity.getText().toString();
                int status = Integer.parseInt("1"); // Get status data if available
                //String purpose = purposeEditText.getText().toString();
                String userID = borrowers_id.getText().toString();
                String propertyId = propertyID.getText().toString();

                // Check if any of the fields are empty
                if (TextUtils.isEmpty(dateAcquired) || TextUtils.isEmpty(timeAcquired) || TextUtils.isEmpty(itemQuantity)
                         || TextUtils.isEmpty(userID) || TextUtils.isEmpty(propertyId)) {
                    // Show a Toast message indicating that some fields are empty
                    Toast.makeText(ItemDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method if any field is empty
                }


                int quantityValue = Integer.parseInt(itemQuantity);
                if (quantityValue == 0) {
                    // Display an AlertDialog indicating that the quantity cannot be 0
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemDetails.this);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder.setMessage("Quantity cannot be 0");
                    alertDialogBuilder.setPositiveButton("OK", null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    progressDialog.dismiss();
                    return; // Exit the onClick method if quantity is 0
                }


                // Inside onClick of request_button
                if (Integer.parseInt(itemQuantity) > Integer.parseInt(stock.getText().toString())) {
                    // Display an AlertDialog indicating that the requested quantity exceeds the available stock
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemDetails.this);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder.setMessage("Requested quantity exceeds available stock");
                    alertDialogBuilder.setPositiveButton("OK", null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    progressDialog.dismiss();
                    return; // Exit the onClick method if requested quantity exceeds available stock
                }

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        // Prepare data for POST request
                        String[] field = {"Date", "Time", "Quantity", "RequestStatus", "UserID", "PropertyID"};
                        String[] data = {dateAcquired, timeAcquired, itemQuantity, String.valueOf(status), userID, propertyId};

                        // Perform POST request using PutData
                        PutData putData = new PutData(ipAddress + "/LoginRegister/request_item.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                Log.d("Response", "Result: " + result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    boolean success = jsonObject.getBoolean("success");
                                    String message = jsonObject.getString("message");
                                    if (success) {

                                        // Display an AlertDialog indicating that the request was successful
                                        AlertDialog.Builder successDialogBuilder = new AlertDialog.Builder(ItemDetails.this);
                                        successDialogBuilder.setTitle("Success");
                                        successDialogBuilder.setMessage(message);
                                        successDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Clear input fields
                                                date.getText().clear();
                                                time.getText().clear();
                                                quantity.getText().clear();
                                                // Navigate back
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(ItemDetails.this, ViewInventoryItem.class);
                                                        intent.putExtra("UserID", userID);
                                                        intent.putExtra("Mode", getIntent().getStringExtra("Mode"));
                                                        intent.putExtra("FirstName", fn);
                                                        intent.putExtra("Role",role);
                                                        startActivity(intent);
                                                        finish();
                                                        progressDialog.dismiss(); // Dismiss progress dialog after navigation
                                                    }
                                                }, 1000); // Delay in milliseconds, adjust as needed
                                            }
                                        });
                                        AlertDialog successDialog = successDialogBuilder.create();
                                        successDialog.show(); // Show the dialog
                                    } else {
                                        // Display an AlertDialog indicating an error message
                                        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(ItemDetails.this);
                                        errorDialogBuilder.setTitle("Error");
                                        errorDialogBuilder.setMessage(message);
                                        errorDialogBuilder.setPositiveButton("OK", null);
                                        AlertDialog errorDialog = errorDialogBuilder.create();
                                        errorDialog.show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Display an AlertDialog indicating an error parsing response
                                    AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(ItemDetails.this);
                                    errorDialogBuilder.setTitle("Error");
                                    errorDialogBuilder.setMessage("Error parsing response");
                                    errorDialogBuilder.setPositiveButton("OK", null);
                                    AlertDialog errorDialog = errorDialogBuilder.create();
                                    errorDialog.show();
                                }
                            }
                        } else {
                            // Display an AlertDialog indicating an error sending request
                            AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(ItemDetails.this);
                            errorDialogBuilder.setTitle("Error");
                            errorDialogBuilder.setMessage("Error sending request");
                            errorDialogBuilder.setPositiveButton("OK", null);
                            AlertDialog errorDialog = errorDialogBuilder.create();
                            errorDialog.show();
                        }
                    }
                });

            }
        });



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();

            }
        });


        // Setting up action bar


        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String description = intent.getStringExtra("Description");
            String stockAvailable = intent.getStringExtra("StockAvailable");
            String image = intent.getStringExtra("Image");

            // Set data to views
            //mActionBar.setTitle(description); // Set title to description
            descrip.setText(description);
            stock.setText(String.valueOf(stockAvailable));

            // Load image using Glide
           // Glide.with(this).load("http://192.168.1.16/LoginRegister/item_images/"+image).into(imageView);

            // Set other data if needed
            propertyID.setText(intent.getStringExtra("PropertyID"));
            borrowers_id.setText(intent.getStringExtra("UserID"));

        }
    }
    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ItemDetails.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDate();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    // Method to show TimePickerDialog
    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ItemDetails.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        updateTime();
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    // Method to update dateEditText with the current date
    private void updateDate() {
        String dateString = android.text.format.DateFormat.format("yyyy-MM-dd", calendar).toString();
        date.setText(dateString);
    }

    // Method to update timeEditText with the current time
    private void updateTime() {
        String timeString = android.text.format.DateFormat.format("hh:mm a", calendar).toString();
        time.setText(timeString);
    }
}
