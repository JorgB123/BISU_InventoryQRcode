package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView stock,descrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.image_view);
        stock = findViewById(R.id.stock);
        descrip = findViewById(R.id.descrip);

        Intent intent = getIntent();
        if (intent != null) {
            String description = intent.getStringExtra("Description");
            String stockAvailable = intent.getStringExtra("StockAvailable");
            String image = intent.getStringExtra("Image");

            // Set data to views

            descrip.setText(description);
            stock.setText(stockAvailable);
            // Load image using Glide
            Glide.with(this).load("http://192.168.1.16/LoginRegister/item_images/"+image).into(imageView);

            // Set other data if needed

        }
    }
}