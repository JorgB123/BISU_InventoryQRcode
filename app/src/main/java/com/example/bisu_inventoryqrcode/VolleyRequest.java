package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

public class VolleyRequest {
    private RequestQueue requestQueue;
    private Context context;

    public VolleyRequest(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetchDataFromServer(String serverUrl, final Callback callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                serverUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error fetching data: " + error.getMessage());
                    }
                }
        );

        // Add the JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    // Callback interface to handle success and error responses
    public interface Callback {
        void onSuccess(JSONArray jsonArray);
        void onError(String errorMessage);
    }
}
