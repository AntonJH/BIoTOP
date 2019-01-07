package com.example.anton.biotop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Animal> animalList = new ArrayList<>();

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
    }

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

//    Intent createIntent() {
//            Intent intent = new Intent(this, AnimalActivity.class);
//            intent.putExtra("list", (Serializable) animalList);
//
//            return intent;
//    }
}