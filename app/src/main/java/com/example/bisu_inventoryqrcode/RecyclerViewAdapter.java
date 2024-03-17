package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Import Glide library
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<ItemData> itemList; // Modify ArrayList type to hold ItemData objects
    private Context context;

    public RecyclerViewAdapter(ArrayList<ItemData> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemData itemData = itemList.get(position);
        holder.descriptionTextView.setText(itemData.getDescription());
        holder.stockAvailableTextView.setText(String.valueOf(itemData.getStockAvailable()));

        // Load image using Glide
        Glide.with(context).load(itemData.getImage()).into(holder.imageView);

        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ItemDetailsActivity with item data
                Intent intent = new Intent(context, ItemDetails.class);
                intent.putExtra("Description", itemData.getDescription());
                intent.putExtra("StockAvailable", itemData.getStockAvailable());
                intent.putExtra("Image", itemData.getImage());
                // Add other data
                intent.putExtra("PropertyNumber", itemData.getPropertyNumber());
                intent.putExtra("DateAcquired", itemData.getDateAcquired());
                intent.putExtra("Unit", itemData.getUnit());
                intent.putExtra("UnitCost", itemData.getUnitCost());
                intent.putExtra("Supplier", itemData.getSupplier());
                intent.putExtra("Particular", itemData.getParticular());
                intent.putExtra("PropertyStatus", itemData.getPropertyStatus());
                intent.putExtra("SourceFund", itemData.getSourceFund());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView descriptionTextView, stockAvailableTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewrec);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            stockAvailableTextView = itemView.findViewById(R.id.textViewStockAvailable);
        }
    }
}
