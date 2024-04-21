package com.example.bisu_inventoryqrcode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        private View indicator;
        ImageView img;// Add an indicator view

        public RequestItemViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyIDTextView = itemView.findViewById(R.id.property_id_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            status_text_view = itemView.findViewById(R.id.status_text_view);
//            indicator = itemView.findViewById(R.id.indicator_view);
            img = itemView.findViewById(R.id.img);// Initialize the indicator view
        }

        public void bind(final RequestItems requestItem) {
            propertyIDTextView.setText(requestItem.getPropertyID());
            quantityTextView.setText(requestItem.getQuantity());
            String id = requestItem.getRequestItemID();

            // Assuming getRequestItemID() returns the unique ID for the request item

            // Set the status text based on the request status
            final String requestStatus = requestItem.getRequestStatus();
            if (requestStatus.equals("1")) {
                status_text_view.setText("Requesting");
                // Disable click for other statuses
                itemView.setClickable(false);
                itemView.setFocusable(false);
                // Hide the indicator
               // indicator.setVisibility(View.GONE);
            } else if (requestStatus.equals("2")) {
                status_text_view.setText("Transferred");
                // Enable click for transferred status
                itemView.setClickable(true);
                itemView.setFocusable(true);
                // Show the indicator
                //indicator.setVisibility(View.VISIBLE);

                // Set click listener
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                        alertDialogBuilder.setTitle("Return Item");
//                        alertDialogBuilder.setMessage("Are you sure you want to return this item?");
//                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // Pass the data when "Transferred" status is clicked
//                                reportItem(requestItem);
//                            }
//                        });
//                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // Do nothing or handle the cancellation
//                            }
//                        });
//                        // Create and show the AlertDialog
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                    }
//                });

            } else if (requestStatus.equals("3")) {
                status_text_view.setText("Ready for Pickup");
                // Disable click for other statuses
                itemView.setClickable(false);
                itemView.setFocusable(false);
                status_text_view.setBackgroundResource(R.drawable.bg_green);
                // Hide the indicator
               // indicator.setVisibility(View.GONE);
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
               // indicator.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load("http://192.168.1.11/BISU_SupplyManagementQRCode/uploads/pictures/"+requestItem.getImage()) // Assuming getImageURL() returns the image URL
                    .placeholder(R.drawable.placeholder) // Placeholder image while loading
                    .error(R.drawable.errorimage) // Image to show in case of error
                    .into(img);
        }

        private void reportItem(RequestItems requestItem) {
            // Prepare data for POST request to transfer the item
            String[] field = {"PropertyID", "Quantity", "DateReturn", "UserID", "Status", "Time", "AdminID"};
            String[] data = {requestItem.getPropertyName(), requestItem.getQuantity(), getCurrentDateTime(), userID, "1", getCurrentTime(), "1"};

            // Perform POST request using PutData to transfer the item
            PutData putData = new PutData("http://192.168.1.11/LoginRegister/transfer_item.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    // If transferring the item was successful, delete it from requestitems table
                    deleteRequestItem(requestItem.getRequestItemID());
                }
            } else {
                Toast.makeText(context, "Error sending request", Toast.LENGTH_SHORT).show();
            }
        }

        private String getCurrentDateTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
        private String getCurrentTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }

        private void deleteRequestItem(final String requestItemID) {
            // Prepare data for POST request to delete the item from requestitems table
            String[] deleteField = {"RequestItemID"};
            String[] deleteData = {requestItemID};

            // Perform POST request using PutData to delete the item
            PutData deletePutData = new PutData("http://192.168.1.11/LoginRegister/delete_request_item.php", "POST", deleteField, deleteData);
            if (deletePutData.startPut()) {
                if (deletePutData.onComplete()) {
                    String deleteResult = deletePutData.getResult();
                    Log.d("DeleteServerResponse", "Result: " + deleteResult);
                    try {
                        JSONObject deleteJsonObject = new JSONObject(deleteResult);
                        boolean deleteSuccess = deleteJsonObject.getBoolean("success");
                        String deleteMessage = deleteJsonObject.getString("message");
                        if (deleteSuccess) {
                            // If deletion was successful, show AlertDialog
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("Success");
                            alertDialogBuilder.setMessage(deleteMessage);
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Navigate back
                                    ((Activity) context).onBackPressed();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            // If deletion was not successful, show Toast
                            Toast.makeText(context, deleteMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(context, "Error sending delete request", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
