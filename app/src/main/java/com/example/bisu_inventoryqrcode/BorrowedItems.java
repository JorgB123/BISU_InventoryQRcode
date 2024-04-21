package com.example.bisu_inventoryqrcode;

// Inside MyRequest activity

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BorrowedItems extends AppCompatActivity {

    String userID;
    String ipAddress = "";
    RecyclerView recview;
    RequestItemAdapter adapter;
    List<RequestItems> requestItemList;

    ProgressBar progressBar;

    TextView prog;
    ConstraintLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_items);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        ipAddress = ipAddressManager.getIPAddress();

        userID = getIntent().getStringExtra("UserID");
        System.out.println("scatter"+userID);

        recview = findViewById(R.id.recview);
        requestItemList = new ArrayList<RequestItems>();
        adapter = new RequestItemAdapter(this, requestItemList, userID);
        recview.setAdapter(adapter);
        recview.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        prog = findViewById(R.id.prog);

        back=findViewById(R.id.back);

        fetchRequestItems(userID);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchRequestItems(String userID) {
        String url = ipAddress + "/LoginRegister/getRequestedItems.php?UserID=" + userID;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            prog.setVisibility(View.GONE);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String propertyName = jsonObject.getString("property_name");
                                String requestItemID = jsonObject.getString("RequestItemID");
                                String quantity = jsonObject.getString("Quantity");
                                String requestStatus = jsonObject.getString("RequestStatus");
                                String propertyID = jsonObject.getString("PropertyID");
                                String image = jsonObject.getString("image_url");


                                // Only add items with request status "1" (Requesting)
                                if (requestStatus.equals("2")) {
                                    RequestItems requestItem = new RequestItems(propertyName, requestItemID, quantity, requestStatus, propertyID,image);
                                    requestItemList.add(requestItem);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BorrowedItems.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                            Log.e("MyRequest", "Error parsing response: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        prog.setVisibility(View.GONE);
                        Toast.makeText(BorrowedItems.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        Log.e("MyRequest", "Error fetching data: " + error.toString());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}
