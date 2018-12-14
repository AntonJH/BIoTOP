package com.example.anton.biotop;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.MyViewHolder> {

    private List<Animal> animalList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, health;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            health = (TextView) view.findViewById(R.id.health);
        }
    }

    public AnimalAdapter(List<Animal> animalList) {
        this.animalList = animalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animal_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.id.setText(animal.getID());
        holder.health.setText(animal.getHealth());
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }
}