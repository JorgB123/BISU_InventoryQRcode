package com.example.bisu_inventoryqrcode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("getInventoryItemInfo.php")
    Call<ItemResponse> getItemInfo(@Query("PropertyNumber") String propertyNumber);
}


