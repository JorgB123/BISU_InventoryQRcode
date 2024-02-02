package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Settings extends AppCompatActivity {

    String ipAddress;
    Button save, signup;
    EditText ipAddressET;

    private static final String PREF_NAME = "IPAddressPref";
    private static final String KEY_IP_ADDRESS = "ipAddress";
    IPAddressManager ipAddressManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ipAddressManager = new IPAddressManager(getApplicationContext());


        save=findViewById(R.id.saveIPBtn);
        signup=findViewById(R.id.backSettings);
        ipAddressET=findViewById(R.id.ipAddressET);
        ipAddressET.setHint(ipAddressManager.getIPAddress());
        
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAddress=ipAddressET.getText().toString().trim();

                if (!ipAddress.equals("")) {
                    Handler handler = new Handler();
                    handler.post(() -> {
                        String[] field = new String[1];
                        field[0] = "IPAddress";

                        String[] data = new String[1];
                        data[0] = ipAddress;
                        PutData putData = new PutData(ipAddress+ "/LoginRegister/insertIPAddress.php","POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("Data inserted successfully")) {
                                    ipAddressManager.saveIPAddress(ipAddress);
                                    Toast.makeText(Settings.this, "Ip address successfully set\n"+ipAddress, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    System.out.println("error "+result);

                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                }


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }



//    public String getIpAddress(){
//        Handler handler = new Handler();
//        handler.post(() -> {
//            String[] field = new String[1];
//            field[0] = "username";
//            String[] data = new String[1];
//            data[0] = ipAddress;
//            PutData putData = new PutData(ipAddress+ "/LoginRegister/login.php","POST", field, data);
//            if (putData.startPut()) {
//                if (putData.onComplete()) {
//                    String result = putData.getResult();
//                    if (result.equals("Login Successfully")) {
//                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//        });
//
//        return  null;
//    }
}