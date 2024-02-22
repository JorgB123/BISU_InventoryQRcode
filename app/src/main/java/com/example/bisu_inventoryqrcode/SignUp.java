package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button register;
    TextView clicker;
    Settings settings;
    ImageButton settingsBtn;
    IPAddressManager ipAddressManager;
    String IPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ipAddressManager= new IPAddressManager(this);
        IPAddress=ipAddressManager.getIPAddress();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        settingsBtn=findViewById(R.id.settingsBUtton);
        register = findViewById(R.id.register);
        clicker=findViewById(R.id.clicker);
        settings= new Settings();

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,Settings.class);
                startActivity(intent);
            }
        });

        clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(view -> {
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
                    PutData putData = new PutData(IPAddress+"/LoginRegister/signup.php","POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
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
                });
            } else {
                Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

