package com.example.bisu_inventoryqrcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserDashboard extends AppCompatActivity {

    EditText search;
    ImageView inventory_view, request_item, report_damage, scanner, settings_user;
    TextView userNamePlaceholder;
    String ipAddress="";//hjhj
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);

        // Initialize EditText
        search = findViewById(R.id.search);
        userNamePlaceholder = findViewById(R.id.userNamePlaceholder);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        ipAddress=ipAddressManager.getIPAddress();

        // Initialize ImageViews
        scanner = findViewById(R.id.scanner);
        report_damage = findViewById(R.id.report_damage);
        request_item = findViewById(R.id.request_item);
        inventory_view = findViewById(R.id.inventory_view);
        settings_user = findViewById(R.id.settings_user);

        // Retrieve user ID or email from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Email")) {
            userId = intent.getStringExtra("Email");
            Log.d("UserDashboard", "Fetched Email: " + userId);

        }

        // Call AsyncTask to fetch user's first name
        new FetchUserFirstNameTask().execute();

        // Set onClickListener for inventory_view
        inventory_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, ViewInventory.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for settings_user
        settings_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for scanner
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                IntentIntegrator integrator = new IntentIntegrator(UserDashboard.this);
                integrator.setPrompt("Scan QR Code");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
            }
        });
    }

    // AsyncTask to fetch user's first name from server
    private class FetchUserFirstNameTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String firstName = "";

            try {
                // Construct URL for PHP script with parameters
                URL url = new URL("http://" + ipAddress + "/getUserFirstName.php?userId=" + userId);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Read response from server
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();

                // Parse JSON response
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                if (jsonObject.getBoolean("success")) {
                    firstName = jsonObject.getString("FirstName");

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return firstName;
        }

        @Override
        protected void onPostExecute(String firstName) {
            // Update TextView with user's first name
            userNamePlaceholder.setText(firstName);
            Log.d("UserDashboard", "Fetched first name: " + firstName);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(UserDashboard.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                String scannedResult = intentResult.getContents();
                Toast.makeText(UserDashboard.this, "Scanned: " + scannedResult, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
