package com.example.bisu_inventoryqrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewInventory extends AppCompatActivity {

    private ArrayList<ItemData> itemList = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private String ipAddress = "http://192.168.1.14/LoginRegister/fetch_data.php"; // Update with your actual URL
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressbar);

        // Make HTTP GET request using Volley
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ipAddress, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                String description = dataObject.getString("Description");
                                int stockAvailable = dataObject.optInt("StockAvailable", 0); // Default value if not present
                                String image = dataObject.optString("Image", "");
                                itemList.add(new ItemData(description, stockAvailable, image));
                            }
                            adapter = new RecyclerViewAdapter(itemList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            // Handle unsuccessful response
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("faking", e.getMessage());
                    }
                },
                error -> {
                    // Handle Volley errors
                    error.printStackTrace();
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
