package com.example.bisu_inventoryqrcode;

public class ItemData {
    private String description;
    private int stockAvailable;
    private String image;
    private String propertyNumber;
    private String dateAcquired;
    private String unit;
    private String unitCost;
    private String supplier;
    private String particular;
    private String propertyStatus;
    private String sourceFund;

    public ItemData(String description, int stockAvailable, String image, String propertyNumber, String dateAcquired,
                    String unit, String unitCost, String supplier, String particular, String propertyStatus, String sourceFund) {
        this.description = description;
        this.stockAvailable = stockAvailable;
        this.image = image;
        this.propertyNumber = propertyNumber;
        this.dateAcquired = dateAcquired;
        this.unit = unit;
        this.unitCost = unitCost;
        this.supplier = supplier;
        this.particular = particular;
        this.propertyStatus = propertyStatus;
        this.sourceFund = sourceFund;
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
    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(String dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public String getSourceFund() {
        return sourceFund;
    }

    public void setSourceFund(String sourceFund) {
        this.sourceFund = sourceFund;
    }
}
