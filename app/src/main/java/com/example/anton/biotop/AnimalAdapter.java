package com.example.anton.biotop;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    private List<Animal> animalList;
    private ItemClickListener mClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView id, health;

        public ViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.list_id);
            health = (TextView) view.findViewById(R.id.list_health);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public AnimalAdapter(List<Animal> animalList) {
        this.animalList = animalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animal_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.id.setText(animal.getID());
        String healthText = animal.getHealthStatus();

        if (healthText.equals("POSITIV")) {
            holder.health.setTextColor(Color.GREEN);
        } else if (healthText.equals("RISK")) {
            holder.health.setTextColor(Color.YELLOW);
        } else {
            holder.health.setTextColor(Color.RED);
        }

        holder.health.setText(healthText);
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    // convenience method for getting data at click position
    Animal getItem(int id) {
        return animalList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}