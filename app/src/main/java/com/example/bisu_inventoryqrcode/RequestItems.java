package com.example.bisu_inventoryqrcode;

public class RequestItems {
    private String propertyID;
    private String quantity;

    public RequestItems(String propertyID, String quantity) {
        this.propertyID = propertyID;
        this.quantity = quantity;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

