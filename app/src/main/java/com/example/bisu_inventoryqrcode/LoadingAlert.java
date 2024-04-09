package com.example.bisu_inventoryqrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.LayoutInflater;

public class LoadingAlert {

    private Activity activity;
    private AlertDialog dialog;
    private Handler handler;

    LoadingAlert(Activity myActivity) {
        activity = myActivity;
        handler = new Handler();
    }

    void startAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_layout, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    void closeAlertDialogWithDelay(long delayMillis) {
        if (dialog != null && dialog.isShowing()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, delayMillis);
        }
    }

    void closeAlertDialog() {
        closeAlertDialogWithDelay(2000); // Close immediately without delay
    }
}

