package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScannedDataActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView scannedDataTextView;
    private EditText descriptionEditText;
    private EditText dateAcquiredEditText;
    private EditText itemCostEditText;
    private Spinner categorySpinner;
    private Spinner statusSpinner;
    private EditText whereaboutEditText;
    private Button insertItemButton;
    private ImageView imageView;
    private Bitmap selectedBitmap;
    private Bitmap bitmap;
    private byte[] byteArray;
    Settings settings;
    String ipAddress="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_data);

        // Initialize the UI elements
        scannedDataTextView = findViewById(R.id.scannedDataTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateAcquiredEditText = findViewById(R.id.dateAcquiredEditText);
        itemCostEditText = findViewById(R.id.itemCostEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        whereaboutEditText = findViewById(R.id.whereabout);
        insertItemButton = findViewById(R.id.insertItemButton);
        imageView = findViewById(R.id.imageView);
        settings = new Settings();
        ipAddress= settings.ipAddress;

        String qr_id = getIntent().getStringExtra("scannedData");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        // Retrieve the scanned data from the Intent
        scannedDataTextView.setText("QR Number: " + currentDateandTime);

        // Set an OnClickListener for the "Insert Item" button
        insertItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(currentDateandTime, BarcodeFormat.QR_CODE, 300, 300);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();

                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }

                // Retrieve the item details
                String itemDescription = descriptionEditText.getText().toString();
                String dateAcquired = dateAcquiredEditText.getText().toString();
                String itemCost = itemCostEditText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String status = statusSpinner.getSelectedItem().toString();
                String whereabout = whereaboutEditText.getText().toString();

                // Convert the selected bitmap to base64
                String imageData = bitmapToBase64(selectedBitmap);

                // Reset the ImageView
                imageView.setImageResource(R.drawable.placeholder);

                // Execute AsyncTask to insert data into MySQL along with the image
                new InsertDataTask().execute(currentDateandTime, itemDescription, dateAcquired, itemCost, category, status, whereabout, imageData);
                descriptionEditText.getText().clear();
                dateAcquiredEditText.getText().clear();
                itemCostEditText.getText().clear();
                whereaboutEditText.getText().clear();
            }
        });

        // Set an OnClickListener for the "Upload Image" button
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                imageView.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // AsyncTask class for inserting data into MySQL
    private class InsertDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String scannedCode = params[0];
                String itemDescription = params[1];
                String dateAcquired = params[2];
                String itemCost = params[3];
                String category = params[4];
                String status = params[5];
                String whereabout = params[6];
                String imageData = params[7];

                String serverUrl = ipAddress +"/LoginRegister/insert_data.php"; // Replace with your server URL

                // Open a connection to the server
                HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Send the data to the server
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os);
                writer.write("scanned_code=" + scannedCode +
                        "&item_description=" + itemDescription +
                        "&date_acquired=" + dateAcquired +
                        "&item_cost=" + itemCost +
                        "&category=" + category +
                        "&status=" + status +
                        "&whereabout=" + whereabout +
                        "&image=" + imageData);
                writer.close();
                os.close();

                // Get the response from the server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Insertion successful
                    return "Data inserted successfully";
                } else {
                    // Insertion failed
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("InsertDataResponse", result); // Log the response

            // Access UI elements on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Display a message on the screen
                    Toast.makeText(ScannedDataActivity.this, result, Toast.LENGTH_SHORT).show();

                    if (result.equals("Data inserted successfully")) {
                        Intent showQRIntent = new Intent(getApplicationContext(), ShowQRCode.class);
                        showQRIntent.putExtra("qr_code", byteArray);
                        startActivity(showQRIntent);
                    }
                    // Handle the result as needed
                    // For example, you can finish the activity or navigate to another screen
                }
            });
        }
    }
}
