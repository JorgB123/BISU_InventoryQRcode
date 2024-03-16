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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(int stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
