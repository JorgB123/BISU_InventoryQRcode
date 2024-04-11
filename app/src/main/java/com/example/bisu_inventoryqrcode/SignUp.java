package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {

    EditText usernameEditText, firstNameEditText, lastNameEditText;
    EditText passwordEditText, imageField;
    Button register;
    TextView clicker;
    ImageButton settingsBtn;
    IPAddressManager ipAddressManager;
    String IPAddress;

    String ipAddress="";
    Spinner departmentSpinner, roleSpinner;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private Uri imageUri;

    private LoadingAlert loadingAlert;

    public class Department {
        private String name;
        private String id;

        public Department(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name; // Display department name in spinner
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadingAlert = new LoadingAlert(this);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        ipAddress=ipAddressManager.getIPAddress();


        ipAddressManager = new IPAddressManager(this);
        IPAddress = ipAddressManager.getIPAddress();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        firstNameEditText = findViewById(R.id.firstname);
        lastNameEditText = findViewById(R.id.lastname);
        register = findViewById(R.id.register);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        roleSpinner = findViewById(R.id.roleSpinner);
        imageField = findViewById(R.id.imageField); // Assuming imageField is an EditText for image path
        clicker = findViewById(R.id.clicker);

        clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open image picker dialog
                pickImage();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingAlert.startAlertDialog();

                String Email = String.valueOf(usernameEditText.getText());
                String Password = String.valueOf(passwordEditText.getText());
                String FirstName = String.valueOf(firstNameEditText.getText());
                String LastName = String.valueOf(lastNameEditText.getText());
                Department selectedDepartment = (Department) departmentSpinner.getSelectedItem();
                String Department = selectedDepartment.getId();
                String Role = roleSpinner.getSelectedItem().toString();
                String status = "0";
                String ImagePath = imageField.getText().toString(); // Get the image file path from the EditText

                Log.d("SignUp", "Email: " + Email);
                Log.d("SignUp", "Password: " + Password);
                Log.d("SignUp", "FirstName: " + FirstName);
                Log.d("SignUp", "LastName: " + LastName);
                Log.d("SignUp", "Department: " + Department);
                Log.d("SignUp", "Role: " + Role);
                Log.d("SignUp", "ImagePath: " + ImagePath);


                if (!Email.isEmpty() && !Password.isEmpty() && !FirstName.isEmpty() && !LastName.isEmpty() && !Department.isEmpty() && !Role.isEmpty()) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Convert image file to byte array
                            byte[] imageBytes;
                            if (imageUri != null) {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                    imageBytes = bitmapToByteArray(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else {
                                imageBytes = getImageAsByteArray(ImagePath);
                            }

                            // Prepare data for POST request
                            String[] field = {"Email", "Password", "Firstname", "Lastname", "DepartmentID", "Role", "ConfirmStatus", "image"};
                            String[] data = {Email, Password, FirstName, LastName, Department, Role, status, imageBytes != null ? Base64.encodeToString(imageBytes, Base64.DEFAULT) : ""};

                            // Perform POST request using PutData
                            PutData putData = new PutData(IPAddress + "/LoginRegister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    loadingAlert.closeAlertDialog();
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    loadingAlert.closeAlertDialog();
                    Toast.makeText(getApplicationContext(), "All fields requireds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchDepartments();
    }

    // Method to pick an image from gallery or capture an image
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // Set the selected image URI to the imageField EditText
            imageUri = data.getData();
            imageField.setText(imageUri.toString());
        }
    }

    // Method to convert bitmap to byte array
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    // Method to read image file and convert it to byte array
    private byte[] getImageAsByteArray(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath); // Decode the image file
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream); // Compress the bitmap to reduce size
            return stream.toByteArray(); // Convert the compressed bitmap to byte array
        } else {
            return null; // Return null if bitmap is null (error reading image)
        }
    }
    // Method to fetch department choices using Volley
    private void fetchDepartments() {
        String url = ipAddress + "/LoginRegister/fetch_departments.php"; // Replace with the URL of your PHP script

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray departmentsArray = response.getJSONArray("departments");
                            List<Department> departmentList = new ArrayList<>();
                            for (int i = 0; i < departmentsArray.length(); i++) {
                                JSONObject departmentObject = departmentsArray.getJSONObject(i);
                                String departmentName = departmentObject.getString("name");
                                String departmentId = departmentObject.getString("id");
                                departmentList.add(new Department(departmentName, departmentId));
                            }
                            ArrayAdapter<Department> spinnerAdapter = new ArrayAdapter<>(SignUp.this, android.R.layout.simple_spinner_item, departmentList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            departmentSpinner.setAdapter(spinnerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(SignUp.this, "Error fetching departments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}
