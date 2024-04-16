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

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.resource.bitmap.CircleCrop;
        import com.bumptech.glide.request.RequestOptions;
        import com.example.bisu_inventoryqrcode.ApiService;
        import com.example.bisu_inventoryqrcode.CaptureActivityPortrait;
        import com.example.bisu_inventoryqrcode.IPAddressManager;
        import com.example.bisu_inventoryqrcode.Item;
        import com.example.bisu_inventoryqrcode.ItemResponse;
        import com.example.bisu_inventoryqrcode.MainActivity;
        import com.example.bisu_inventoryqrcode.R;
        import com.example.bisu_inventoryqrcode.ViewInventoryItem;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;
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
        import java.util.Objects;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class UserDashboard extends AppCompatActivity {

    EditText search;
    ImageView inventory_view, request_item, borrowed_item, scanner, settings_user, more, returned, imageView9;
    TextView userNamePlaceholder;
    String ipAddress = ""; //hjhj
    String userId = "";
    String id, fn, userID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        ipAddress = ipAddressManager.getIPAddress();







        search = findViewById(R.id.search);
        userNamePlaceholder = findViewById(R.id.userNamePlaceholder);


        // Initialize ImageViews
        scanner = findViewById(R.id.scanner);
        borrowed_item = findViewById(R.id.report_damage);
        request_item = findViewById(R.id.request_item);
        inventory_view = findViewById(R.id.inventory_view);
        settings_user = findViewById(R.id.settings_user);
        returned = findViewById(R.id.returned);
        more = findViewById(R.id.more);
        imageView9 = findViewById(R.id.imageView9);

        userID = getIntent().getStringExtra("UserID");
        fn = getIntent().getStringExtra("FirstName");
        userNamePlaceholder.setText(fn);
        String cn = getIntent().getStringExtra("ConfirmStatus");
        System.out.println("ConfirmStatus "+cn);

        boolean isFundAdministrator = getIntent().getBooleanExtra("IsFundAdministrator", false);
        System.out.println("IsFundAdministrator: " + isFundAdministrator);
        String url="http://192.168.1.11/LoginRegister/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the ApiService interface
        apiset apiService = retrofit.create(apiset.class);

        // Get the UserID from the intent
        String userID = getIntent().getStringExtra("UserID");

        // Make the API call
        Call<ImageResponse> call = apiService.getImageUrl(userID);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Load and display the image using Glide
                    String imageUrl = response.body().getImageUrl();
                    Glide.with(UserDashboard.this)
                            .load("http://192.168.1.11/BISU_SupplyManagementQRCode/uploads/pictures/"+imageUrl)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(imageView9);
                } else {
                    Toast.makeText(UserDashboard.this, "Failed to fetch image URL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(UserDashboard.this, "Failed to fetch image URL: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        returned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, ReturnedItems.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });


        borrowed_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, BorrowedItems.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

        request_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, MyRequest.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserDashboard.this, ReqReport.class);
//                intent.putExtra("UserID", userID);
//                startActivity(intent);
//            }
//        });


        // Set onClickListener for inventory_view
        inventory_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, ViewInventoryItem.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("IsFundAdministrator", isFundAdministrator);
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
                scanQRCode();
            }
        });
    }

    private void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(UserDashboard.this);
        integrator.setPrompt("Scan QR Code");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
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
                // Check if the scannedResult corresponds to a valid item
                if (isValidItem(scannedResult)) {
                    // Start the next activity and pass the scannedResult
                    Intent intent = new Intent(UserDashboard.this, AfterScannedActivity.class);
                    intent.putExtra("ScannedResult", scannedResult);
                    intent.putExtra("UserID", userID);
                    startActivity(intent);
                } else {
                    // Display a toast message indicating that the item doesn't exist
                    Toast.makeText(UserDashboard.this, "Item not found", Toast.LENGTH_LONG).show();
                    // Go back to UserDashboard
                    finish();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    // Method to check if the scanned result corresponds to a valid item
    private boolean isValidItem(String scannedResult) {
        // Perform the necessary validation here, such as checking the scanned result against your database
        // For demonstration purposes, assume that scannedResult is valid if it's not empty
        return scannedResult != null && !scannedResult.isEmpty();
    }





}
