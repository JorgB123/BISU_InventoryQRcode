package com.example.bisu_inventoryqrcode;

public class ItemData {
    private String description;
    private int stockAvailable;
    private String image;

    public ItemData(String description, int stockAvailable, String image) {
        this.description = description;
        this.stockAvailable = stockAvailable;
        this.image = image;
    }

    // Getter methods for accessing the fields

    public String getDescription() {
        return description;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public String getImage() {
        return image;
    }
}

