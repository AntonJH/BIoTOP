package com.example.anton.biotop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AnimalActivity extends AppCompatActivity implements AnimalAdapter.ItemClickListener {
    private List<Animal> animalList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        recyclerView = (RecyclerView) findViewById(R.id.rvAnimals);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        animalAdapter = new AnimalAdapter(animalList);
        animalAdapter.setClickListener(this);
        recyclerView.setAdapter(animalAdapter);

        prepareAnimalData();
    }

    private void prepareAnimalData() {
        Animal animal = new Animal("000", "Ko");
        animalList.add(animal);
        animal = new Animal("001", "Häst");
        animalList.add(animal);
        animal = new Animal("002", "Får");
        animalList.add(animal);

        animalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {

        Toast.makeText(this, "Du valde " + animalAdapter.getItem(position).getID() + " med hälsa: " + animalAdapter.getItem(position).getHealth(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, AnimalHealthActivity.class);
        intent.putExtra("id", animalAdapter.getItem(position).getID());
        intent.putExtra("type", animalAdapter.getItem(position).getType());
        startActivity(intent);
    }
}