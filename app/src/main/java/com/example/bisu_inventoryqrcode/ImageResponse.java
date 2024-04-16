package com.example.bisu_inventoryqrcode;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {
    @SerializedName("image")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }
}
