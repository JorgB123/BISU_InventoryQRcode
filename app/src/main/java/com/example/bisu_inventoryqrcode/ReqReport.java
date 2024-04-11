package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class ReqReport extends AppCompatActivity {

    String userID;
    RelativeLayout rel1,rel2;

    ConstraintLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_report);

        userID = getIntent().getStringExtra("UserID");
        rel1=findViewById(R.id.rel1);
        rel2=findViewById(R.id.rel2);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReqReport.this, MyRequest.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

        rel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReqReport.this, MyReport.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

    }
}