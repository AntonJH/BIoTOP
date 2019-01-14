package com.example.anton.biotop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnimalActivity extends AppCompatActivity implements AnimalAdapter.ItemClickListener {
    private List<Animal> animalList;
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;

//    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        recyclerView = (RecyclerView) findViewById(R.id.rvAnimals);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        if (animalList == null || animalList.isEmpty()) {
            animalList = new ArrayList<>();
        }

        if (getIntent().hasExtra("list")) {
            animalList = (List<Animal>) getIntent().getSerializableExtra("list");

            System.out.println("lista: " + animalList);
        }
        animalAdapter = new AnimalAdapter(animalList);
        animalAdapter.setClickListener(this);
        recyclerView.setAdapter(animalAdapter);

        if (getIntent().hasExtra("status") && getIntent().hasExtra("id") && getIntent().hasExtra("temp")) {
            String status = getIntent().getStringExtra("status");
            String id = getIntent().getStringExtra("id");
            String temp = getIntent().getStringExtra("temp");
            String pulse = getIntent().getStringExtra("pulse");
            String pressureS = getIntent().getStringExtra("pressureS");
            String pressureD = getIntent().getStringExtra("pressureD");
            String healthDesc = getIntent().getStringExtra("desc");

            for (Animal animal : animalList) {
                if (animal.getID().equals(id)) {
                    animal.setHealthStatus(status);
                    animal.setBodyTemperature(temp);
                    animal.setPulse(pulse);
                    animal.setBloodPressureSystolic(pressureS);
                    animal.setBloodPressureDiastolic(pressureD);
                    animal.setHealthDesc(healthDesc);
                    animalAdapter.notifyDataSetChanged();
                }
            }
        } else if (animalList == null || animalList.isEmpty()) {
            prepareAnimalData();
        }

        // Test av bottom navbar
//        toolbar = getSupportActionBar();
//        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
//        navigationView.setSelectedItemId(R.id.navigation_animal);
//        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//
//        Menu menu = navigationView.getMenu();
//        MenuItem menuItem = menu.getItem(2);
//        menuItem.setChecked(true);
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
////        @Override
////        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
////            Fragment fragment;
////
////            switch (menuItem.getItemId()) {
////                case R.id.navigation_farming:
//////                    openFarmActivity();
////                    return true;
////                case R.id.navigation_animal:
////                    return true;
////                case R.id.navigation_home:
////                    Intent intent = new Intent(AnimalActivity.this, MainActivity.class);
////                    intent.putExtra("list", (Serializable) animalList);
////                    startActivity(intent);
////                    return true;
////            }
////            return false;
////        }
////    };

    private void prepareAnimalData() {
        Animal animal = new Animal("000", "Ko", "NEGATIV", "44.000000000", "70", "130", "80");
        animalList.add(animal);
        animal.setHealthDesc("-Kroppstemperaturen är på allvarligt hög/låg nivå.\n-Pulsen är inom normal nivå.\n-Blodtrycket är inom normal nivå.");
        animal = new Animal("001", "Häst", "POSITIV", "37.20000000", "76", "120", "75");
        animalList.add(animal);
        animal.setHealthDesc("-Kroppstemperaturen är inom normal nivå.\n-Pulsen är inom normal nivå.\n-Blodtrycket är inom normal nivå.");
        animal = new Animal("002", "Får", "RISK","35.80000000", "62", "145", "99");
        animalList.add(animal);
        animal.setHealthDesc("-Kroppstemperaturen är ej inom normal nivå.\n-Pulsen är inom normal nivå.\n-Blodtrycket är inom normal nivå.");

        animalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {

        Toast.makeText(this, "Du valde " + animalAdapter.getItem(position).getID() + " med hälsa: " + animalAdapter.getItem(position).getHealthStatus(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, AnimalHealthActivity.class);
        intent.putExtra("id", animalAdapter.getItem(position).getID());
        intent.putExtra("type", animalAdapter.getItem(position).getType());
        intent.putExtra("status", animalAdapter.getItem(position).getHealthStatus());

        intent.putExtra("temp", animalAdapter.getItem(position).getBodyTemperature());
        intent.putExtra("pulse", animalAdapter.getItem(position).getPulse());
        intent.putExtra("pressureS", animalAdapter.getItem(position).getBloodPressureSystolic());
        intent.putExtra("pressureD", animalAdapter.getItem(position).getBloodPressureDiastolic());

        intent.putExtra("desc", animalAdapter.getItem(position).getHealthDesc());
        intent.putExtra("list", (Serializable) animalList);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("list", (Serializable) animalList);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("list", (Serializable) animalList);
                startActivity(intent);

                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }
}