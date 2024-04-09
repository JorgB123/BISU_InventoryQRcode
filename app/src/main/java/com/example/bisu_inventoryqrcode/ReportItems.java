package com.example.bisu_inventoryqrcode;

public class ReportItems {

    private String propertyID;
    private String detail;

    public ReportItems(String propertyID, String detail) {
        this.propertyID = propertyID;
        this.detail = detail;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
