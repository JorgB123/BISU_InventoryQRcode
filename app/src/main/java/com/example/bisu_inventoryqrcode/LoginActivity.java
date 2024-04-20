package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView click;
    Settings settings;
    String ipAddress="";//hjhj

    private LoadingAlert loadingAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingAlert = new LoadingAlert(this);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        click=findViewById(R.id.click);
        settings = new Settings();
//        ipAddress= settings.getIPAddress();
        ipAddress=ipAddressManager.getIPAddress();


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(view -> {
            String Email = String.valueOf(usernameEditText.getText());
            String Password = String.valueOf(passwordEditText.getText());

            if (!Email.equals("") && !Password.equals("")) {
                loadingAlert.startAlertDialog();
                Handler handler = new Handler();
                handler.post(() -> {
                    String[] field = new String[2];
                    field[0] = "Email";
                    field[1] = "Password";
                    String[] data = new String[2];
                    data[0] = Email;
                    data[1] = Password;
                    PutData putData = new PutData(ipAddress + "/LoginRegister/login.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            loadingAlert.closeAlertDialog();
                            String result = putData.getResult();
                            if (!result.startsWith("Error:")) {
                                // Splitting the result string to extract UserID, FirstName, and ConfirmStatus
                                String[] userData = result.split(",");
                                String userID = userData[0]; // UserID
                                String firstName = userData[1]; // FirstName
                                String confirmStatus = userData[2]; // ConfirmStatus
                                String role = userData[3]; // Role

                                // Check the confirmStatus value
                                if (confirmStatus.equals("1")) {
                                    // Creating the Intent with UserID and FirstName extras
                                    Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                                    intent.putExtra("UserID", userID);
                                    intent.putExtra("FirstName", firstName);
                                    intent.putExtra("Role", role);
                                    startActivity(intent);
                                    finish();
                                   // Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Display a toast message indicating the user cannot proceed
                                    Toast.makeText(getApplicationContext(), "Please wait for confirmation from the admin.", Toast.LENGTH_SHORT).show();
                                    // Display AlertDialog indicating the user cannot proceed
                                    AlertDialog.Builder confirmationDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                    confirmationDialogBuilder.setTitle("Confirmation Needed");
                                    confirmationDialogBuilder.setMessage("Please wait for confirmation from the admin.");
                                    confirmationDialogBuilder.setPositiveButton("OK", null);
                                    AlertDialog confirmationDialog = confirmationDialogBuilder.create();
                                    confirmationDialog.show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                // Display AlertDialog with the error message
                                AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                errorDialogBuilder.setTitle("Error");
                                errorDialogBuilder.setMessage(result);
                                errorDialogBuilder.setPositiveButton("OK", null);
                                AlertDialog errorDialog = errorDialogBuilder.create();
                                errorDialog.show();
                            }
                        }
                    }
                });
            } else {
                loadingAlert.closeAlertDialog();
                Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                // Display AlertDialog for empty fields
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage("All fields required");
                alertDialogBuilder.setPositiveButton("OK", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


    }

}

