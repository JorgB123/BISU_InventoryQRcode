package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.content.SharedPreferences;

public class IPAddressManager {

    String defaultIPAddress="http://192.168.1.16";
    private static final String PREF_NAME = "IPAddressPref";
    private static final String KEY_IP_ADDRESS = "ipAddress";

    private final Context context;

    public IPAddressManager(Context context) {
        this.context = context;
    }

    // Method to store an IP address
    public void saveIPAddress(String ipAddress) {
        System.out.println("saving");
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_IP_ADDRESS, ipAddress);
            editor.apply();
            System.out.println("Saved to local");
        }
        else{
            System.out.println("Error saved to local");
        }
    }

    // Method to retrieve the stored IP address
    public String getIPAddress() {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            // Check if the key exists before attempting to retrieve the value
            if (sharedPreferences.contains(KEY_IP_ADDRESS)) {
                return sharedPreferences.getString(KEY_IP_ADDRESS, "");
            } else {
                // Return an empty string or handle the case accordingly
                return defaultIPAddress;
            }
        } else {
            // Return an empty string or handle the case accordingly
            return defaultIPAddress;
        }
    }
}

