package com.example.bisu_inventoryqrcode;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ShowQRCode extends AppCompatActivity {
    Button save, add;
    ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        save = findViewById(R.id.save_qr);
        add = findViewById(R.id.add_again);
        qrCode = findViewById(R.id.qr_code_img);

        byte[] byteArray = getIntent().getByteArrayExtra("qr_code");
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        qrCode.setImageBitmap(bmp);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToGallery(bmp);
            }
        });
    }

    private void saveImageToGallery(Bitmap bitmap) {
        String fileName = "QRCodeImage";
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/BISU_Inventory_QRCode");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fname = fileName + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();

        try {
            OutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            // Add image to the gallery
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "BISU Inventory QR Code");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
