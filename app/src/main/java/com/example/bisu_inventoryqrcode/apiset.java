package com.example.bisu_inventoryqrcode;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface apiset
{
    @GET("fetcher.php")
    Call<List<responsemodel>> getdata();

    @GET("serviceable_fetcher.php")
    Call<List<responsemodel>> getServiceableData();

    @GET("getImage.php")
    Call<ImageResponse> getImageUrl(@Query("UserID") String userID);

}
