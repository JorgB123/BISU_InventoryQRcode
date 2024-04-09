package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestItemAdapter extends RecyclerView.Adapter<RequestItemAdapter.RequestItemViewHolder> {

    private Context context;
    private List<RequestItems> requestItemList;

    public RequestItemAdapter(Context context, List<RequestItems> requestItemList) {
        this.context = context;
        this.requestItemList = requestItemList;
    }

    @NonNull
    @Override
    public RequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item_row, parent, false);
        return new RequestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestItemViewHolder holder, int position) {
        RequestItems requestItem = requestItemList.get(position);
        holder.bind(requestItem);
    }

    @Override
    public int getItemCount() {
        return requestItemList.size();
    }

    public class RequestItemViewHolder extends RecyclerView.ViewHolder {
        private TextView propertyIDTextView, quantityTextView;

        public RequestItemViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyIDTextView = itemView.findViewById(R.id.property_id_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
        }

        public void bind(RequestItems requestItem) {
            propertyIDTextView.setText(requestItem.getPropertyID());
            quantityTextView.setText(requestItem.getQuantity());
        }
    }
}

