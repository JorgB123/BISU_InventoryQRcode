package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReturnedItemAdapter extends RecyclerView.Adapter<ReturnedItemAdapter.ReturnedItemViewHolder> {

    private Context context;
    private List<Returns> returnedItemsList;

    public ReturnedItemAdapter(Context context, List<Returns> returnedItemsList) {
        this.context = context;
        this.returnedItemsList = returnedItemsList;
    }

    @NonNull
    @Override
    public ReturnedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.return_item_row, parent, false);
        return new ReturnedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnedItemViewHolder holder, int position) {
        Returns returnedItem = returnedItemsList.get(position);
        holder.bind(returnedItem);
    }

    @Override
    public int getItemCount() {
        return returnedItemsList.size();
    }

    public class ReturnedItemViewHolder extends RecyclerView.ViewHolder {
        private TextView propertyIdTextView, quantityTextView,dateReturned , timeReturned;

        public ReturnedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyIdTextView = itemView.findViewById(R.id.property_id_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            dateReturned = itemView.findViewById(R.id.dateReturned);
            //timeReturned = itemView.findViewById(R.id.timeReturned);
        }

        public void bind(Returns returnedItem) {
            propertyIdTextView.setText(returnedItem.getDescription());
            quantityTextView.setText(returnedItem.getQuantity());
            dateReturned.setText(returnedItem.getDateReturn());


        }
    }
}
