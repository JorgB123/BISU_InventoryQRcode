package com.example.bisu_inventoryqrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private static final int YOUR_CAMERA_PERMISSION_REQUEST_CODE = 100;

    private TextView scannedDataTextView;
    private EditText descriptionEditText, supplierNameEditText;
    private EditText dateAcquiredEditText;
    private EditText itemCostEditText;
    private EditText itemQuantityEditText;
    private Spinner categorySpinner;
    private Spinner statusSpinner, unitSpinner, sourceFundSpinner;
    private EditText whereaboutEditText;
    private Button insertItemButton;
    private ImageView imageView;
    private Bitmap selectedBitmap;
    private Bitmap bitmap;
    private byte[] byteArray;
    private Settings settings;
    private String ipAddress = "";
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_data);

        // Initialize the UI elements
        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        scannedDataTextView = findViewById(R.id.scannedDataTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateAcquiredEditText = findViewById(R.id.dateAcquiredEditText);
        itemCostEditText = findViewById(R.id.itemCostEditText);
        supplierNameEditText= findViewById(R.id.supplierNameEditText);
        itemQuantityEditText = findViewById(R.id.itemQuantityEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        unitSpinner = findViewById(R.id.unitSpinner);
        sourceFundSpinner=findViewById(R.id.sourceFundSpinner);
        whereaboutEditText = findViewById(R.id.whereabout);
        insertItemButton = findViewById(R.id.insertItemButton);
        imageView = findViewById(R.id.imageView);
        settings = new Settings();
        ipAddress = ipAddressManager.getIPAddress();

        String qr_id = getIntent().getStringExtra("scannedData");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        scannedDataTextView.setText("Property Number: " + currentDateandTime);

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

                String itemDescription = descriptionEditText.getText().toString();
                String dateAcquired = dateAcquiredEditText.getText().toString();
                String itemCost = itemCostEditText.getText().toString();
                String itemQuantity = itemQuantityEditText.getText().toString();
                String supplier = supplierNameEditText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String status = statusSpinner.getSelectedItem().toString();
                String whereabout = whereaboutEditText.getText().toString();
                String unit = unitSpinner.getSelectedItem().toString();
                String sourceFund = sourceFundSpinner.getSelectedItem().toString();

                String imageData = bitmapToBase64(selectedBitmap);

                imageView.setImageResource(R.drawable.placeholder);

                new InsertDataTask().execute(currentDateandTime, itemDescription, dateAcquired, itemCost, itemQuantity, supplier, category, status, whereabout, imageData, unit, sourceFund);
                descriptionEditText.getText().clear();
                dateAcquiredEditText.getText().clear();
                itemCostEditText.getText().clear();
                itemQuantityEditText.getText().clear();
                whereaboutEditText.getText().clear();
                supplierNameEditText.getText().clear();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for camera permission
                if (ContextCompat.checkSelfPermission(ScannedDataActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request camera permission
                    ActivityCompat.requestPermissions(ScannedDataActivity.this, new String[]{Manifest.permission.CAMERA}, YOUR_CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    // Camera permission already granted, proceed with opening the image chooser
                    openImageChooser();
                }
            }
        });
    }

    private void openImageChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there is a camera app available
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create a File to save the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile));
            }
        }

        // Create a chooser for the user to select camera or gallery
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    selectedBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    imageView.setImageBitmap(selectedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (currentPhotoPath != null) {
                setPic();
            }
        }
    }

    private void setPic() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        selectedBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(selectedBitmap);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private class InsertDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String scannedCode = params[0];
                String itemDescription = params[1];
                String dateAcquired = params[2];
                String itemCost = params[3];
                String itemQuantity = params[4];
                String supplier = params[5];
                String category = params[6];
                String status = params[7];
                String whereabout = params[8];
                String imageData = params[9];
                String unit = params[10];
                String sourceFund = params[11];

                String serverUrl = ipAddress + "/LoginRegister/insert_data.php";

                HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os);
                writer.write("PropertyNumber=" + scannedCode +
                        "&Description=" + itemDescription +
                        "&DateAcquired=" + dateAcquired +
                        "&UnitCost=" + itemCost +
                        "&StockAvailable=" + itemQuantity +
                        "&Supplier=" + supplier +
                        "&Particular=" + category +
                        "&PropertyStatus=" + status +
                        "&WhereAbout=" + whereabout +
                        "&Image=" + imageData +
                        "&Unit=" + unit +
                        "&SourceFund=" + sourceFund);
                writer.close();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Data inserted successfully";
                } else {
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("InsertDataResponse", result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ScannedDataActivity.this, result, Toast.LENGTH_SHORT).show();

                    if (result.equals("Data inserted successfully")) {
                        Intent showQRIntent = new Intent(getApplicationContext(), ShowQRCode.class);
                        showQRIntent.putExtra("qr_code", byteArray);
                        startActivity(showQRIntent);
                    }
                }
            });
        }
    }

    // Other helper methods...
}
