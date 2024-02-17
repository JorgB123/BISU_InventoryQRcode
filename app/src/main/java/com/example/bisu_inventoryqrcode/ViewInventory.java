package com.example.bisu_inventoryqrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;

public class ViewInventory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the data for RecyclerView
        itemList = new ArrayList<>();
        // Add sample data, you can replace it with your own data
        itemList.add("Item 1");
        itemList.add("Item 2");
        itemList.add("Item 3");

        // Initialize the adapter
        adapter = new RecyclerViewAdapter(itemList);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }
}
