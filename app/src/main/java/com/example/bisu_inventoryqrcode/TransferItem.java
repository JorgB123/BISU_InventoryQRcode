package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TransferItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_item);

        // Receive the passed data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String requestItemID = extras.getString("RequestItemID");
            String propertyID = extras.getString("PropertyID");
            String quantity = extras.getString("Quantity");
            String userID = extras.getString("UserID");

            // Display the received data
            TextView requestItemIDTextView = findViewById(R.id.request_item_id_text_view);
            TextView propertyIDTextView = findViewById(R.id.property_id_text_view);
            TextView quantityTextView = findViewById(R.id.quantity_text_view);
            TextView userIDTextView = findViewById(R.id.user_id_text_view);

            requestItemIDTextView.setText(requestItemID);
            propertyIDTextView.setText(propertyID);
            quantityTextView.setText(quantity);
            userIDTextView.setText(userID);
        }
    }
}
