package com.example.bisu_inventoryqrcode;

public class InventoryItem {
    private String Description;
    private String WhereAbout;
    private String Image; // New field for the image URL

    public InventoryItem(String description, String whereAbout, String imageURL) {
        this.Description = description;
        this.WhereAbout = whereAbout;
        this.Image = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getWhereAbout() {
        return WhereAbout;
    }

    public void setWhereAbout(String whereAbout) {
        this.WhereAbout = whereAbout;
    }

    public String getImageURL() {
        return Image;
    }

    public void setImageURL(String imageURL) {
        this.Image = imageURL;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "description='" + Description + '\'' +
                ", whereAbout='" + WhereAbout + '\'' +
                ", imageURL='" + Image + '\'' +
                '}';
    }
}
