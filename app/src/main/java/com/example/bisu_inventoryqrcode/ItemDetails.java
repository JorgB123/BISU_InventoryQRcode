package com.example.bisu_inventoryqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView imageView;
    private EditText stockTextView, descriptionTextView, propertyNumberEditText, dateAcquiredEditText, unitEditText, unitCostEditText, supplierEditText, particularEditText, propertyStatusEditText, sourceFundEditText;

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
        }

        // Use Volley to retrieve additional data
        retrieveAdditionalData();
    }

    private void retrieveAdditionalData() {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.14/LoginRegister/fetchdetails.php";

        // Request a JSON response from the provided URL
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Assuming the first item in the JSON array contains the desired data
                            JSONObject jsonObject = response.getJSONObject(0);
                            String propertyNumber = jsonObject.getString("PropertyNumber");
                            String dateAcquired = jsonObject.getString("DateAcquired");
                            String unit = jsonObject.getString("Unit");
                            String unitCost = jsonObject.getString("UnitCost");
                            String supplier = jsonObject.getString("Supplier");
                            String particular = jsonObject.getString("Particular");
                            String propertyStatus = jsonObject.getString("PropertyStatus");
                            String sourceFund = jsonObject.getString("SourceFund");

                            // Set retrieved data to respective EditText fields
                            propertyNumberEditText.setText(propertyNumber);
                            dateAcquiredEditText.setText(dateAcquired);
                            unitEditText.setText(unit);
                            unitCostEditText.setText(unitCost);
                            supplierEditText.setText(supplier);
                            particularEditText.setText(particular);
                            propertyStatusEditText.setText(propertyStatus);
                            sourceFundEditText.setText(sourceFund);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }
}
