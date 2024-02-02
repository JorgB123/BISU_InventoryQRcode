package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanFragment extends Fragment {

    private Button scanner, add;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        // Find views by their IDs
        scanner = view.findViewById(R.id.scanner);
        add =  view.findViewById(R.id.add);
        //text = view.findViewById(R.id.text);

        // Set click listener for the button (if needed)
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ScanFragment.this);
                integrator.setPrompt("Scan QR Code");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scannedDataIntent = new Intent(getActivity(), ScannedDataActivity.class);
                startActivity(scannedDataIntent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                String scannedResult = intentResult.getContents();
                Toast.makeText(getActivity(), "Scanned: " + scannedResult, Toast.LENGTH_LONG).show();

                // Create an Intent to start the ScannedDataActivity and pass the scanned result
                Intent scannedDataIntent = new Intent(getActivity(), ScannedDataActivity.class);
                scannedDataIntent.putExtra("scannedData", scannedResult);
                startActivity(scannedDataIntent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}


