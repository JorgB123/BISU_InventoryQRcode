package com.example.bisu_inventoryqrcode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemService {
    private ApiService apiService;

    public ItemService() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void fetchItemInformation(String propertyNumber, Callback<ItemResponse> callback) {
        Call<ItemResponse> call = apiService.getItemInfo(propertyNumber);
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ItemResponse itemResponse = response.body();
                    // Handle successful response and update UI with item information
                } else {
                    // Handle unsuccessful response (e.g., item not found) and display error message
                }
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                // Handle network failure (e.g., no internet connection) and display error message
            }
        });
    }
}

