package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewInventoryItem extends AppCompatActivity implements myadapter.OnItemClickListener {
    RecyclerView recview;
    String userID;

    ConstraintLayout back;

    ImageView backArrow;

    ProgressBar progressBar;

    TextView prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory_item);

        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        back = findViewById(R.id.back);
        backArrow = findViewById(R.id.backArrow);

        progressBar = findViewById(R.id.progressBar);
        prog = findViewById(R.id.prog);

        userID = getIntent().getStringExtra("UserID");

        processdata();



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void processdata() {

        progressBar.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);
        Call<List<responsemodel>> call = apicontroller
                .getInstance()
                .getapi()
                .getdata();

        call.enqueue(new Callback<List<responsemodel>>() {
            @Override
            public void onResponse(Call<List<responsemodel>> call, Response<List<responsemodel>> response) {
                // Log the response
                Log.d("API_RESPONSE", "Response code: " + response.code());
                if (response.isSuccessful()) {
                    List<responsemodel> data = response.body();

                    if (data != null && !data.isEmpty()) {
                        // Initialize adapter here
                        myadapter adapter = new myadapter(data, ViewInventoryItem.this);
                        recview.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        prog.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        prog.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    prog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<responsemodel>> call, Throwable t) {
                // Log the error
                Log.e("API_ERROR", "Failed: " + t.getMessage(), t);
                Toast.makeText(getApplicationContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(responsemodel item) {
        Intent intent = new Intent(this, DetailActivity.class);

        // Pass relevant data from the clicked item as intent extras
        intent.putExtra("Description", item.getDescription());
        intent.putExtra("StockAvailable", item.getStockAvailable());
        intent.putExtra("Image", item.getImage());
        intent.putExtra("PropertyNumber",item.getPropertyNumber());
        intent.putExtra("UserID", userID);
        intent.putExtra("PropertyID",item.getPropertyID());

        // Add more data if needed

        // Start the ItemDetails activity
        startActivity(intent);
    }
}
