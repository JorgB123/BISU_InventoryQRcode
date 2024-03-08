package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AdminLogin extends AppCompatActivity {

    EditText admin_email, admin_password;
    Button admin_login;
    String ipAddress="";//hjhj

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        admin_email=findViewById(R.id.admin_email);
        admin_password=findViewById(R.id.admin_password);

        admin_login=findViewById(R.id.admin_login);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        ipAddress=ipAddressManager.getIPAddress();

        admin_login.setOnClickListener(view -> {
            String Email = String.valueOf(admin_email.getText());
            String Password = String.valueOf(admin_password.getText());



            if (!Email.equals("") && !Password.equals("")) {
                Handler handler = new Handler();
                handler.post(() -> {
                    String[] field = new String[2];
                    field[0] = "Email";
                    field[1] = "Password";
                    String[] data = new String[2];
                    data[0] = Email;
                    data[1] = Password;
                    PutData putData = new PutData(ipAddress+ "/LoginRegister/admin_login.php","POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (result.equals("Login Successfully")) {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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