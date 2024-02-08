package com.example.bisu_inventoryqrcode;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bisu_inventoryqrcode.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Make sure this is the correct layout file.

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setBackground(null);

        // Set up onClickListener for the FloatingActionButton
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ScanFragment());
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.History:
                    replaceFragment(new HistoryFragment());
                    break;

                case R.id.Scan:
                    replaceFragment(new ScanFragment());
                    break;

                case R.id.Inventory:
                    replaceFragment(new InventoryFragment());
                    break;

                case R.id.Profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navHostFragment, fragment);
        fragmentTransaction.commit();
    }
}


