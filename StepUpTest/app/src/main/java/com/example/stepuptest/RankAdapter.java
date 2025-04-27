package com.example.stepuptest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private List<RankItem> rankList;

    public RankAdapter(List<RankItem> rankList) {
        this.rankList = rankList;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        RankItem item = rankList.get(position);
        holder.dateTextView.setText(item.getDate());
        holder.stepsTextView.setText(String.valueOf(item.getSteps()));
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    public static class RankViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView stepsTextView;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.tv_date);
            stepsTextView = itemView.findViewById(R.id.tv_steps);
        }
    }
}
