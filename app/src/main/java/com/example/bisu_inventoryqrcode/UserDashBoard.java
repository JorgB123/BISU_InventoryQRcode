package com.example.bisu_inventoryqrcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class UserDashBoard extends AppCompatActivity {

    EditText search;
    ImageView inventory_view, request_item, report_damage, scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);

        // Instantiate EditText
        search = findViewById(R.id.search);

        // Instantiate ImageViews
        scanner = findViewById(R.id.scanner);
        report_damage = findViewById(R.id.report_damage);
        request_item = findViewById(R.id.request_item);
        inventory_view = findViewById(R.id.inventory_view);

        inventory_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashBoard.this, ViewInventory.class);
                startActivity(intent);

            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                IntentIntegrator integrator = new IntentIntegrator(UserDashBoard.this);
                integrator.setPrompt("Scan QR Code");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
            }
        });

        // Now you can use these views as needed in your activity
        // For example, you can set onClickListener or perform other operations with them
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(UserDashBoard.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                String scannedResult = intentResult.getContents();
                Toast.makeText(UserDashBoard.this, "Scanned: " + scannedResult, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
