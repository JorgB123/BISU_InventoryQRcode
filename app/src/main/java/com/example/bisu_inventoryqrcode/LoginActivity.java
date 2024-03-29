package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView click;
    Settings settings;
    String ipAddress="";//hjhj

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Handler handler = new Handler();
                handler.post(() -> {
                    String[] field = new String[2];
                    field[0] = "Email";
                    field[1] = "Password";
                    String[] data = new String[2];
                    data[0] = Email;
                    data[1] = Password;
                    PutData putData = new PutData(ipAddress+ "/LoginRegister/login.php","POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (!result.startsWith("Error:")) {
                                // Splitting the result string to extract UserID and FirstName
                                String[] userData = result.split(",");
                                String userID = userData[0]; // UserID
                                String firstName = userData[1]; // FirstName

                                // Creating the Intent with UserID and FirstName extras
                                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                                intent.putExtra("UserID", userID);
                                intent.putExtra("FirstName", firstName);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
