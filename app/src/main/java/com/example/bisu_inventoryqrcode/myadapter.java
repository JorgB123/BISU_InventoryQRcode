package com.example.bisu_inventoryqrcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder> {
    private List<responsemodel> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(responsemodel item);
    }

    public myadapter(List<responsemodel> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.bind(data.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView t1, t2;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
        }

        public void bind(final responsemodel item, final OnItemClickListener listener) {
            t1.setText(item.getDescription());
            if (item.getStockAvailable() != null && !item.getStockAvailable().isEmpty()) {
                t2.setText(item.getStockAvailable());
            } else {
                t2.setText("0");
            }
            Glide.with(itemView.getContext()).load("http://192.168.137.141/BISU_SupplyManagementQRCode/uploads/pictures/" + item.getImage()).into(img);

            // Set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
