package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class IPSettings extends Fragment {

    public String ipAddress="http://192.168.1.16";
    Button save, signup;
    EditText ipAddressET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_i_p_settings, container, false);

        SignUp signUp= (SignUp) getActivity();

        save=view.findViewById(R.id.saveIPBtn);
        signup=view.findViewById(R.id.backSettings);
        ipAddressET=view.findViewById(R.id.ipAddressET);
        ipAddressET.setText(ipAddress);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAddress=ipAddressET.getText().toString().trim();
                Toast.makeText(signUp, "Ip address successfully set\n"+ipAddress, Toast.LENGTH_SHORT).show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp,LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}