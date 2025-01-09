package com.example.repcommissiontracker.Adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.repcommissiontracker.Classes.SalesRepresentative;
import com.example.repcommissiontracker.R;

import java.util.List;

public class RepAdapter extends RecyclerView.Adapter<RepAdapter.RepViewHolder> {
    private Context context;
    private List<SalesRepresentative> repList;

    public RepAdapter(Context context, List<SalesRepresentative> repList) {
        this.context = context;
        this.repList = repList;
    }
    public interface OnRepClickListener {
        void onRepClick(SalesRepresentative representative);
    }

    private OnRepClickListener onRepClickListener;

    public void setOnRepClickListener(OnRepClickListener listener) {
        this.onRepClickListener = listener;
    }

// In onBindViewHolder


    @NonNull
    @Override
    public RepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rep_card, parent, false);
        return new RepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepViewHolder holder, int position) {
        SalesRepresentative rep = repList.get(position);

        holder.nameTextView.setText(rep.getName());
        holder.phoneTextView.setText("Phone: " + rep.getPhoneNumber());
        holder.startDateTextView.setText("Start Date: " + rep.getStartDate());
        holder.locationTextView.setText("Location ID: " + rep.getSupervisedLocName());
        holder.itemView.setOnClickListener(v -> {
            if (onRepClickListener != null) {
                onRepClickListener.onRepClick(rep);
            }
        });
        Bitmap image = rep.getImageBitmap(); // Assuming `imagePath` contains the image Bitmap
        if (image != null) {
            holder.imageView.setImageBitmap(image);
        } else {
           // holder.imageView.setImageResource(R.drawable.default_rep_image); // Fallback image
        }
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }
    // Method to update the dataset and refresh the RecyclerView
    public void updateData(List<SalesRepresentative> newRepList) {
        this.repList = newRepList; // Update the dataset
        notifyDataSetChanged();   // Notify the RecyclerView to refresh
    }
    public static class RepViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView, startDateTextView, locationTextView;
        ImageView imageView;

        public RepViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.rep_name);
            phoneTextView = itemView.findViewById(R.id.rep_phone);
            startDateTextView = itemView.findViewById(R.id.rep_start_date);
            locationTextView = itemView.findViewById(R.id.rep_location);
            imageView = itemView.findViewById(R.id.rep_image);
        }
    }
}