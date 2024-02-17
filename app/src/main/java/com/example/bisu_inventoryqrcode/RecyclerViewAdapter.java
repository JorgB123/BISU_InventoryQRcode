package com.example.bisu_inventoryqrcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> itemList;

    public RecyclerViewAdapter(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewItem);
            imageView = itemView.findViewById(R.id.imageViewrec);
        }

        public void bind(String item) {
            textView.setText(item);
        }
    }
}
