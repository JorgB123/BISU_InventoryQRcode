package com.example.bisu_inventoryqrcode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestItemAdapter extends RecyclerView.Adapter<RequestItemAdapter.RequestItemViewHolder> {

    private Context context;
    private List<RequestItems> requestItemList;
    private String userID;

    public RequestItemAdapter(Context context, List<RequestItems> requestItemList, String userID) {
        this.context = context;
        this.requestItemList = requestItemList;
        this.userID = userID;
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
        private TextView propertyIDTextView, quantityTextView, status_text_view;
        private View indicator; // Add an indicator view

        public RequestItemViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyIDTextView = itemView.findViewById(R.id.property_id_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            status_text_view = itemView.findViewById(R.id.status_text_view);
            indicator = itemView.findViewById(R.id.indicator_view); // Initialize the indicator view
        }

        public void bind(final RequestItems requestItem) {
            propertyIDTextView.setText(requestItem.getPropertyID());
            quantityTextView.setText(requestItem.getQuantity());

            // Set the status text based on the request status
            final String requestStatus = requestItem.getRequestStatus();
            if (requestStatus.equals("1")) {
                status_text_view.setText("Requesting");
                // Disable click for other statuses
                itemView.setClickable(false);
                itemView.setFocusable(false);
                // Hide the indicator
                indicator.setVisibility(View.GONE);
            } else if (requestStatus.equals("2")) {
                status_text_view.setText("Transferred");
                // Enable click for transferred status
                itemView.setClickable(true);
                itemView.setFocusable(true);
                // Show the indicator
                indicator.setVisibility(View.VISIBLE);

                // Set click listener
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Pass the data when "Transferred" status is clicked
                        Intent intent = new Intent(context, TransferItem.class);
                        intent.putExtra("RequestItemID", requestItem.getRequestItemID());
                        intent.putExtra("PropertyID", requestItem.getPropertyID());
                        intent.putExtra("Quantity", requestItem.getQuantity());
                        intent.putExtra("UserID", userID);
                        context.startActivity(intent);
                    }
                });
            } else if (requestStatus.equals("3")) {
                status_text_view.setText("Pending");
                // Disable click for other statuses
                itemView.setClickable(false);
                itemView.setFocusable(false);
                // Hide the indicator
                indicator.setVisibility(View.GONE);
            } else if (requestStatus.equals("4")) {
                status_text_view.setText("Rejected");
                // Disable click for other statuses
                itemView.setClickable(false);
                itemView.setFocusable(false);
                // Hide the indicator
                indicator.setVisibility(View.GONE);
            } else {
                // Handle other status codes as needed
                status_text_view.setText("Unknown");
                // Disable click for other statuses
                itemView.setClickable(false);
                itemView.setFocusable(false);
                // Hide the indicator
                indicator.setVisibility(View.GONE);
            }
        }
    }

}
