package com.example.anton.biotop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Animal> animalList = new ArrayList<>();

    private ActionBar toolbar;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonFarm = (Button) findViewById(R.id.buttonFarm);
        buttonFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFarmActivity();
            }
        });

        Button  buttonAnimal = (Button) findViewById(R.id.buttonAnimal);
        buttonAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnimalActivity();
            }
        });

        if (getIntent().hasExtra("list")) {
            animalList = (List<Animal>) getIntent().getSerializableExtra("list");

            System.out.println("lista: " + animalList);
        }

        toolbar = getSupportActionBar();
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setSelectedItemId(R.id.navigation_home);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_farming:
                    openFarmActivity();
                    return true;
                case R.id.navigation_animal:
                    openAnimalActivity();
                    return true;
            }
            return false;
        }
    };

    public void openFarmActivity() {
        Intent intent = new Intent(this, FarmActivity.class);
        startActivity(intent);
    }

    public void openAnimalActivity() {
        Intent intent = new Intent(this, AnimalActivity.class);
        if (animalList != null || !animalList.isEmpty()) {
            intent.putExtra("list", (Serializable) animalList);
        }
        startActivity(intent);
    }
}