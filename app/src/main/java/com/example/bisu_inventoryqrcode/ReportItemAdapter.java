package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ReportItemViewHolder> {

    private Context context;
    private List<ReportItems> reportItemList;

    public ReportItemAdapter(Context context, List<ReportItems> reportItemList) {
        this.context = context;
        this.reportItemList = reportItemList;
    }

    @NonNull
    @Override
    public ReportItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_item_row, parent, false);
        return new ReportItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportItemViewHolder holder, int position) {
        ReportItems reportItem = reportItemList.get(position);
        holder.bind(reportItem);
    }

    @Override
    public int getItemCount() {
        return reportItemList.size();
    }

    public class ReportItemViewHolder extends RecyclerView.ViewHolder {
        private TextView propertyIDTextView, quantityTextView;

        public ReportItemViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyIDTextView = itemView.findViewById(R.id.property_id_text_view); // Assuming property ID is still displayed in the report_item_row layout
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
        }

        public void bind(ReportItems reportItem) {
            propertyIDTextView.setText(reportItem.getPropertyID());
            quantityTextView.setText(reportItem.getDetail());
        }
    }
}
