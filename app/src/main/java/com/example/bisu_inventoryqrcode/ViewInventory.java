package com.example.bisu_inventoryqrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.android.volley.toolbox.JsonArrayRequest;
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
    ImageView image;
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
                                String stockAvailable = dataObject.getString("StockAvailable");
                                System.out.println("stock "+stockAvailable);
                                String image = dataObject.getString("Image");

                                int stocks = 0;
                                if (stockAvailable != null && !stockAvailable.isEmpty()) {
                                    try {
                                        stocks = Integer.parseInt(stockAvailable);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                        // Handle the case where the string is not a valid integer
                                    }
                                }
                                //                                System.out.println("kuha na "+stockAvailable);
                                itemList.add(new ItemData(description,stocks , image));
                                // Do something with the data


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

//        // Add the request to the RequestQueue
//        Volley.newRequestQueue(this).add(jsonArrayRequest);
//
//        // Execute FetchDataTask with the ipAddress
//        new FetchDataTask().execute(ipAddress);
    }

    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void parseJsonResponse(JSONArray response) throws JSONException {
        // Process the JSON response
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            String description = jsonObject.getString("Description");
            int stockAvailable = jsonObject.optInt("StockAvailable", 0); // Default value if not present
            String image = jsonObject.optString("Image", "");
            itemList.add(new ItemData(description, stockAvailable, image));
        }
        // Update RecyclerView with the fetched data
        adapter = new RecyclerViewAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    private static class FetchDataTask extends AsyncTask<String, Void, String> {

        private final String TAG = FetchDataTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            try {
                return fetchData(urls[0]);
            } catch (IOException e) {
                Log.e(TAG, "Error fetching data", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject response = new JSONObject(result);
                    JSONArray dataArray = response.getJSONArray("data");
                    // Process JSON data (if needed)
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON", e);
                }
            } else {
                Log.e(TAG, "No data received");
            }
        }

        private static String fetchData(String urlString) throws IOException {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }
    }
}
