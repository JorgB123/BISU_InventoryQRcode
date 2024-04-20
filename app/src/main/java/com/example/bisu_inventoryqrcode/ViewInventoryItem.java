package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewInventoryItem extends AppCompatActivity implements myadapter.OnItemClickListener {
    RecyclerView recview;
    RecyclerView.LayoutManager layoutManager;
    String userID;

    ConstraintLayout back;

    ImageView backArrow;

    ProgressBar progressBar;

    TextView prog;

    String fn, role, mode;
    EditText searchEditText;

    List<responsemodel> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory_item);

        recview = findViewById(R.id.recview);
        //recview.setHasFixedSize(true);
        //recview.setLayoutManager(new LinearLayoutManager(this));
        searchEditText = findViewById(R.id.searchEditText);

        layoutManager=new GridLayoutManager(this,2);
        recview.setLayoutManager(layoutManager);

        back = findViewById(R.id.back);
        backArrow = findViewById(R.id.backArrow);

        progressBar = findViewById(R.id.progressBar);
        prog = findViewById(R.id.prog);

        userID = getIntent().getStringExtra("UserID");
        fn = getIntent().getStringExtra("FirstName");
        role = getIntent().getStringExtra("Role");
        mode = getIntent().getStringExtra("Mode");
        // After initializing fn
        Log.d("ViewInventory", "First Name: " + fn);
        Log.d("ViewInventory", "Role: " + role);
        Log.d("ViewInventory", "Mode: " + mode);



        processdata();



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewInventoryItem.this, UserDashboard.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("FirstName",fn);
                intent.putExtra("Role",role);
                startActivity(intent);
                finish();
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the data based on the search query
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No implementation needed
            }
        });




}

    @Override
    protected void onResume() {
        super.onResume();
        // Call processdata again when the activity resumes to refresh data
        processdata();
    }
    private void filterData(String query) {
        List<responsemodel> filteredList = new ArrayList<>();

        for (responsemodel item : dataList) {
            // Filter the data based on your criteria
            if (item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update the RecyclerView with the filtered data
        myadapter adapter = new myadapter(filteredList, ViewInventoryItem.this);
        recview.setAdapter(adapter);
    }

    public void processdata() {
        progressBar.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);

        Call<List<responsemodel>> call;

        // Check if the user is a fund administrator
        String selectedMode = getIntent().getStringExtra("Mode");

        // If the user is a fund administrator, fetch all data
        if (selectedMode != null && selectedMode.equals("Admin")) {
            call = apicontroller
                    .getInstance()
                    .getapi()
                    .getdata();
        } else {
            // If the user is not a fund administrator, fetch only serviceable items
            call = apicontroller
                    .getInstance()
                    .getapi()
                    .getServiceableData();
        }

        call.enqueue(new Callback<List<responsemodel>>() {
            @Override
            public void onResponse(Call<List<responsemodel>> call, Response<List<responsemodel>> response) {
                // Log the response
                Log.d("API_RESPONSE", "Response code: " + response.code());
                if (response.isSuccessful()) {
                    List<responsemodel> data = response.body();

                    if (data != null && !data.isEmpty()) {

                        dataList.clear();
                        dataList.addAll(data);
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
        intent.putExtra("Specs",item.getSpecs());


        // Add more data if needed
        intent.putExtra("Mode",mode);
        intent.putExtra("FirstName",fn);
        intent.putExtra("Role",role);
        // Start the ItemDetails activity
        startActivity(intent);
    }
}
