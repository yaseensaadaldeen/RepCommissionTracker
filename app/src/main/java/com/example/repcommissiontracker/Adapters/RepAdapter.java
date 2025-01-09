package com.example.repcommissiontracker.Adapters;
import android.content.Context;
import android.content.Intent;
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

    @NonNull
    @Override
    public RepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rep_card, parent, false);
        return new RepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepViewHolder holder, int position) {
        SalesRepresentative currentRep = repList.get(position);
        holder.repName.setText(currentRep.getName());
        holder.repPhone.setText(currentRep.getPhoneNumber());
        holder.repStartDate.setText(currentRep.getStartDate());
        holder.repLocation.setText(currentRep.getSupervisedLocId());

        // Info button click listener to navigate to RepDetailsActivity
        holder.repInfoIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(context, RepDetailsActivity.class);
//            intent.putExtra("rep_name", currentRep.getName());
//            intent.putExtra("rep_phone", currentRep.getPhone());
//            intent.putExtra("rep_start_date", currentRep.getStartDate());
//            intent.putExtra("rep_location", currentRep.getLocation());
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    public static class RepViewHolder extends RecyclerView.ViewHolder {

        TextView repName, repPhone, repStartDate, repLocation;
        ImageView repInfoIcon;

        public RepViewHolder(View itemView) {
            super(itemView);
            repName = itemView.findViewById(R.id.rep_name);
            repPhone = itemView.findViewById(R.id.rep_phone);
            repStartDate = itemView.findViewById(R.id.rep_start_date);
            repLocation = itemView.findViewById(R.id.rep_location);
            repInfoIcon = itemView.findViewById(R.id.rep_info_icon);
        }
    }
}
