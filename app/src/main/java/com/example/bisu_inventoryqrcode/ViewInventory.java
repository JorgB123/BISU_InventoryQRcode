package com.example.bisu_inventoryqrcode;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

import java.util.ArrayList;

public class ViewInventory extends AppCompatActivity {

    private ArrayList<ItemData> itemList = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private String ipAddress = "http://192.168.1.14/LoginRegister/fetch_data.php"; // Update with your actual URL
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressbar);

        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ipAddress, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        handleServerResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void handleServerResponse(JSONObject response) {
        try {
            boolean success = response.getBoolean("success");
            if (success) {
                JSONArray dataArray = response.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    String description = dataObject.getString("Description");
                    int stockAvailable = dataObject.optInt("StockAvailable", 0);
                    String image = dataObject.optString("Image", "");
                    String propertyNumber = dataObject.optString("PropertyNumber", "");
                    String dateAcquired = dataObject.optString("DateAcquired", "");
                    String unit = dataObject.optString("Unit", "");
                    String unitCost = dataObject.optString("UnitCost", "");
                    String supplier = dataObject.optString("Supplier", "");
                    String particular = dataObject.optString("Particular", "");
                    String propertyStatus = dataObject.optString("PropertyStatus", "");
                    String sourceFund = dataObject.optString("SourceFund", "");
                    itemList.add(new ItemData(description, stockAvailable, image, propertyNumber, dateAcquired, unit,
                            unitCost, supplier, particular, propertyStatus, sourceFund));
                }
                adapter = new RecyclerViewAdapter(itemList);
                recyclerView.setAdapter(adapter);
            } else {
                // Handle unsuccessful response
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ViewInventory", e.getMessage());
        }
    }

}
