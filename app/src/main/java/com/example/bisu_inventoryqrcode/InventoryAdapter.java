package com.example.bisu_inventoryqrcode;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<InventoryItem> itemList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTextView;
        public TextView whereAboutTextView;

        ImageView picImg;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.titleText);
            whereAboutTextView = itemView.findViewById(R.id.loc);
            picImg = itemView.findViewById(R.id.picImg);
        }
    }

    public InventoryAdapter(List<InventoryItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_whatsnew, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InventoryItem item = itemList.get(position);
        holder.descriptionTextView.setText(item.getDescription());
        holder.whereAboutTextView.setText(item.getWhereAbout());

        Glide.with(holder.itemView.getContext())
                .load("http://192.168.1.11/BISU_SupplyManagementQRCode/uploads/pictures/"+item.getImageURL()) // Assuming getImageURL() returns the image URL
                .placeholder(R.drawable.placeholder) // Placeholder image while loading
                .error(R.drawable.errorimage) // Image to show in case of error
                .into(holder.picImg);


        Log.d("InventoryAdapter", "Item at position " + position + ": Description - " + item.getDescription() + ", WhereAbout - " + item.getWhereAbout());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

