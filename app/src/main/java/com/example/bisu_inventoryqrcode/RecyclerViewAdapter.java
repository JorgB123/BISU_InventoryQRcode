package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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

        byte[] decodedBytes = Base64.decode(itemData.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        if (bitmap != null) {

        }
        holder.imageView.setImageBitmap(bitmap);



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
