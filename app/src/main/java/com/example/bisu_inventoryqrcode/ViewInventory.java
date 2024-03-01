package com.example.bisu_inventoryqrcode;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
    private String ipAddress = "http://192.168.1.12/LoginRegister/fetch_data.php"; // Update with your actual URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Make HTTP GET request using Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                ipAddress,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Process the JSON response
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String description = jsonObject.getString("Description");
                                int stockAvailable = jsonObject.getInt("StockAvailable");
                                String image = jsonObject.getString("Image");
                                itemList.add(new ItemData(description, stockAvailable, image));
                            }
                            // Update RecyclerView with the fetched data
                            adapter = new RecyclerViewAdapter(itemList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(ViewInventory.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {

        private  final String TAG = FetchDataTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... urls) {
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
                    // Process JSON data
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject item = dataArray.getJSONObject(i);
                        // Extract data from each item
                        String description = item.getString("Description");
                        String stockAvailable = item.optString("StockAvailable", "N/A");
                        String image = item.optString("Image", "N/A");
                        // Process item data
                        // Example: Log item details
                        Log.d(TAG, "Description: " + description +
                                ", Stock Available: " + stockAvailable +
                                ", Image: " + image);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON", e);
                }
            } else {
                Log.e(TAG, "No data received");
            }
        }

        private String fetchData(String urlString) throws IOException {
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
