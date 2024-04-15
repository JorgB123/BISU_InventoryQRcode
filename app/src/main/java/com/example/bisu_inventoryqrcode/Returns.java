package com.example.bisu_inventoryqrcode;

public class Returns {
    private String propertyID;
    private String dateReturn;
    private String time;
    private String quantity;
    private String description;

    public Returns(String propertyID, String dateReturn, String time, String quantity, String description) {
        this.propertyID = propertyID;
        this.dateReturn = dateReturn;
        this.time = time;
        this.quantity = quantity;
        this.description = description;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(String dateReturn) {
        this.dateReturn = dateReturn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

