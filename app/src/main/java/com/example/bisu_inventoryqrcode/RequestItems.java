package com.example.bisu_inventoryqrcode;

public class RequestItems {
    private  String propertyName;
    private String propertyID;
    private String requestItemID;

    private String quantity;
    private String requestStatus;

    public RequestItems(String propertyID, String requestItemID, String quantity, String requestStatus, String propertyName) {
        this.propertyID = propertyID;
        this.requestItemID = requestItemID;
        this.quantity = quantity;
        this.requestStatus = requestStatus;
        this.propertyName = propertyName;


    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getRequestItemID() {
        return requestItemID;
    }

    public void setRequestItemID(String requestItemID) {
        this.requestItemID = requestItemID;
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}

