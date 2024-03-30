package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView stock,descrip;
    AppCompatButton report_btn,req_btn;

    String propertyNumber, userID, description, stockAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.image_view);
        stock = findViewById(R.id.stock);
        descrip = findViewById(R.id.descrip);
        report_btn = findViewById(R.id.report_btn);
        req_btn = findViewById(R.id.req_btn);

        req_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ItemDetails.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("PropertyNumber", propertyNumber);
                intent.putExtra("Description", description);
                intent.putExtra("StockAvailable", stockAvailable);
                startActivity(intent);

            }
        });

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        Intent intent = getIntent();
        if (intent != null) {
            description = intent.getStringExtra("Description");
            stockAvailable = intent.getStringExtra("StockAvailable");
            String image = intent.getStringExtra("Image");

            propertyNumber = getIntent().getStringExtra("PropertyNumber");
            userID = getIntent().getStringExtra("UserID");

            // Set data to views

            descrip.setText(description);
            stock.setText(stockAvailable);
            // Load image using Glide
            Glide.with(this).load("http://192.168.1.16/LoginRegister/item_images/"+image).into(imageView);

            // Set other data if needed

        }
    }
}