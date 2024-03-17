package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class ItemDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView imageView;
    private EditText stockTextView, descriptionTextView, propertyNumberEditText, dateAcquiredEditText, unitEditText, unitCostEditText, supplierEditText, particularEditText, propertyStatusEditText, sourceFundEditText;
    Button request_button, report_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize views
        imageView = findViewById(R.id.image_view);
        stockTextView = findViewById(R.id.stock);
        descriptionTextView = findViewById(R.id.descrip);
        propertyNumberEditText = findViewById(R.id.propertyNumber);
        dateAcquiredEditText = findViewById(R.id.dateAcquired);
        unitEditText = findViewById(R.id.unit);
        unitCostEditText = findViewById(R.id.unitCost);
        supplierEditText = findViewById(R.id.supplier);
        particularEditText = findViewById(R.id.particular);
        propertyStatusEditText = findViewById(R.id.propertyStatus);
        sourceFundEditText = findViewById(R.id.sourceFund);
        request_button=findViewById(R.id.request_button);
        report_button=findViewById(R.id.report_button);


        // Setting up action bar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String description = intent.getStringExtra("Description");
            int stockAvailable = intent.getIntExtra("StockAvailable", 0);
            String image = intent.getStringExtra("Image");

            // Set data to views
            mActionBar.setTitle(description); // Set title to description
            descriptionTextView.setText(description);
            stockTextView.setText(String.valueOf(stockAvailable));

            // Load image using Glide
            Glide.with(this).load(image).into(imageView);

            // Set other data if needed
            propertyNumberEditText.setText(intent.getStringExtra("PropertyNumber"));
            dateAcquiredEditText.setText(intent.getStringExtra("DateAcquired"));
            unitEditText.setText(intent.getStringExtra("Unit"));
            unitCostEditText.setText(intent.getStringExtra("UnitCost"));
            supplierEditText.setText(intent.getStringExtra("Supplier"));
            particularEditText.setText(intent.getStringExtra("Particular"));
            propertyStatusEditText.setText(intent.getStringExtra("PropertyStatus"));
            sourceFundEditText.setText(intent.getStringExtra("SourceFund"));
        }
    }
}
