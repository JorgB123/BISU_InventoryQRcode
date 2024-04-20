package com.example.bisu_inventoryqrcode;

public class responsemodel
{

    String PropertyID,Description, StockAvailable, Image, PropertyNumber, UserID, Specs;


    public responsemodel() {

    }

    public responsemodel(String propertyID, String description, String stockAvailable, String image, String propertyNumber, String userID, String specs) {
        PropertyID = propertyID;
        Description = description;
        StockAvailable = stockAvailable;
        Image = image;
        PropertyNumber=propertyNumber;
        Specs=specs;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
    public String getPropertyNumber() {
        return PropertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        PropertyNumber = propertyNumber;
    }


    public String getPropertyID() {
        return PropertyID;
    }

    public void setPropertyID(String propertyID) {
        PropertyID = propertyID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStockAvailable() {
        return StockAvailable;
    }

    public void setStockAvailable(String stockAvailable) {
        StockAvailable = stockAvailable;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getSpecs() {
        return Specs;
    }

    public void setSpecs(String specs) {
        Specs = specs;
    }
}
