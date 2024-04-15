package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferItem extends AppCompatActivity {

    Button transfer_button;
    TextView requestItemIDTextView, propertyIDTextView, quantityTextView, userIDTextView;
    private String ipAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_item);

        ipAddress = new IPAddressManager(getApplicationContext()).getIPAddress();

        transfer_button = findViewById(R.id.transfer_button);
        requestItemIDTextView = findViewById(R.id.request_item_id_text_view);
        propertyIDTextView = findViewById(R.id.property_id_text_view);
        quantityTextView = findViewById(R.id.quantity_text_view);
        userIDTextView = findViewById(R.id.user_id_text_view);

        // Receive the passed data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String requestItemID = extras.getString("RequestItemID");
            String propertyID = extras.getString("PropertyID");
            String quantity = extras.getString("Quantity");
            String userID = extras.getString("UserID");

            // Display the received data
            requestItemIDTextView.setText(requestItemID);
            propertyIDTextView.setText(propertyID);
            quantityTextView.setText(quantity);
            userIDTextView.setText(userID);
        }

        // Set OnClickListener for the transfer_button
        transfer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportItem();
            }
        });
    }

    private void reportItem() {
        // Prepare data for POST request to transfer the item
        String[] field = {"PropertyID", "Quantity", "DateReturn", "UserID", "Status", "Time", "AdminID"};
        String[] data = {propertyIDTextView.getText().toString(), quantityTextView.getText().toString(), getCurrentDateTime(), userIDTextView.getText().toString(), "1", getCurrentTime(), "1"};

        // Perform POST request using PutData to transfer the item
        PutData putData = new PutData(ipAddress + "/LoginRegister/transfer_item.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                // If transferring the item was successful, delete it from requestitems table
                deleteRequestItem();
            }
        } else {
            Toast.makeText(TransferItem.this, "Error sending request", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRequestItem() {
        // Prepare data for POST request to delete the item from requestitems table
        String[] deleteField = {"RequestItemID"};
        String[] deleteData = {requestItemIDTextView.getText().toString()};

        // Perform POST request using PutData to delete the item
        PutData deletePutData = new PutData(ipAddress + "/LoginRegister/delete_request_item.php", "POST", deleteField, deleteData);
        if (deletePutData.startPut()) {
            if (deletePutData.onComplete()) {
                String deleteResult = deletePutData.getResult();
                Log.d("DeleteServerResponse", "Result: " + deleteResult);
                try {
                    JSONObject deleteJsonObject = new JSONObject(deleteResult);
                    boolean deleteSuccess = deleteJsonObject.getBoolean("success");
                    String deleteMessage = deleteJsonObject.getString("message");
                    Toast.makeText(TransferItem.this, deleteMessage, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TransferItem.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(TransferItem.this, "Error sending delete request", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to get current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
