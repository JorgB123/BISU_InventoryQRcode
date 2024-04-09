package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class MyReport extends AppCompatActivity {

    String userID;

    String ipAddress = "";

    ProgressBar progressBar;
    TextView prog;
    RecyclerView recview;

    ReportItemAdapter adapter;

    List<ReportItems> reportItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);

        IPAddressManager ipAddressManager = new IPAddressManager(getApplicationContext());
        ipAddress = ipAddressManager.getIPAddress();

        userID = getIntent().getStringExtra("UserID");

        recview = findViewById(R.id.recview);
        reportItemList = new ArrayList<ReportItems>();
        adapter = new ReportItemAdapter(this, reportItemList);
        recview.setAdapter(adapter);
        recview.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar); // Initialize progressBar
        prog = findViewById(R.id.prog);

        fetchReportItems(userID);
    }

    private void fetchReportItems(String userID) {
        String url = "http://192.168.1.11/LoginRegister/getReportedItems.php?UserID=" + userID;

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
                                String propertyID = jsonObject.getString("property_name");
                                String details = jsonObject.getString("Details");
                                ReportItems reportItems = new ReportItems(propertyID, details);
                                reportItemList.add(reportItems);

                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyReport.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                            Log.e("MyReport", "Error parsing response: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        prog.setVisibility(View.GONE);
                        Toast.makeText(MyReport.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        Log.e("MyReport", "Error fetching data: " + error.toString());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}