package com.example.anton.biotop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnimalActivity extends AppCompatActivity {
    private List<Animal> animalList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AnimalAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        recyclerView = (RecyclerView) findViewById(R.id.rvAnimals);

        mAdapter = new AnimalAdapter(animalList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        prepareAnimalData();
    }

    private void prepareAnimalData() {
        Animal animal = new Animal("000", "OK");
        animalList.add(animal);
        animal = new Animal("001", "OK");
        animalList.add(animal);
        animal = new Animal("002", "OK");
        animalList.add(animal);
        animal = new Animal("003", "OK");
        animalList.add(animal);
        animal = new Animal("004", "Warning");
        animalList.add(animal);
        animal = new Animal("005", "OK");
        animalList.add(animal);
        animal = new Animal("006", "Danger");
        animalList.add(animal);
        animal = new Animal("007", "OK");
        animalList.add(animal);
        animal = new Animal("008", "OK");
        animalList.add(animal);
        animal = new Animal("009", "OK");
        animalList.add(animal);

        mAdapter.notifyDataSetChanged();
    }
}