package com.example.bisu_inventoryqrcode;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface apiset
{
    @GET("fetcher.php")
    Call<List<responsemodel>> getdata();

}
