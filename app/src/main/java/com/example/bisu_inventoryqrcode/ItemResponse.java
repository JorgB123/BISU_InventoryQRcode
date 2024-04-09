package com.example.bisu_inventoryqrcode;

public class ItemResponse
{
    // Define fields to represent the item information retrieved from the server
    boolean success;
    String message;
    Item data;

    // Define getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Item getData() {
        return data;
    }

    public void setData(Item data) {
        this.data = data;
    }

    // Define other methods as needed
}

