package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
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
    private EditText stockTextView, descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize views
        imageView = findViewById(R.id.image_view);
        stockTextView = findViewById(R.id.stock);
        descriptionTextView = findViewById(R.id.descrip);

        // Setting up action bar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
       // mActionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));

        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String description = intent.getStringExtra("Description");
            int stockAvailable = intent.getIntExtra("StockAvailable", 0);
            String image = intent.getStringExtra("Image");

            // Set data to views
            mActionBar.setTitle(description); // Set title to description
            descriptionTextView.setText("" + description);
            stockTextView.setText("" + stockAvailable);

            // Load image using Glide
            Glide.with(this).load(image).into(imageView);
        }
    }
}
