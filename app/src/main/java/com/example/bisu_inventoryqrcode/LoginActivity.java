package com.example.bisu_inventoryqrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
    String ipAddress = "";
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
        click = findViewById(R.id.click);
        ipAddress = ipAddressManager.getIPAddress();

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(view -> {
            AlertDialog.Builder adminDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            adminDialogBuilder.setTitle("Are you a fund administrator?");
            adminDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    proceedToDashboard(true);
                }
            });
            adminDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    proceedToDashboard(false);
                }
            });
            AlertDialog adminDialog = adminDialogBuilder.create();
            adminDialog.show();
        });
    }

    private void proceedToDashboard(boolean isFundAdministrator) {
        String Email = String.valueOf(usernameEditText.getText());
        String Password = String.valueOf(passwordEditText.getText());

        // Validation checks
        if (!isValidEmail(Email)) {
            usernameEditText.setBackgroundResource(R.drawable.edittext_error_background);
            Toast.makeText(LoginActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        } else {
            usernameEditText.setBackgroundResource(R.drawable.edittext);
        }

        if (Password.length() < 8 || Password.length() > 16) {
            passwordEditText.setBackgroundResource(R.drawable.edittext_error_background);
            Toast.makeText(LoginActivity.this, "Password must be between 8 and 16 characters", Toast.LENGTH_SHORT).show();
            return;
        } else {
            passwordEditText.setBackgroundResource(R.drawable.edittext);
        }

        if (Email.isEmpty() || Password.isEmpty()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder.setMessage("All fields required");
            alertDialogBuilder.setPositiveButton("OK", null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

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
                        String[] userData = result.split(",");
                        String userID = userData[0]; // UserID
                        String firstName = userData[1]; // FirstName
                        String confirmStatus = userData[2]; // ConfirmStatus

                        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                        intent.putExtra("UserID", userID);
                        intent.putExtra("FirstName", firstName);
                        intent.putExtra("IsFundAdministrator", isFundAdministrator);
                        startActivity(intent);
                        finish();
                    } else {
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
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@bisu\\.edu\\.ph";
        return email.matches(emailPattern);
    }
}
