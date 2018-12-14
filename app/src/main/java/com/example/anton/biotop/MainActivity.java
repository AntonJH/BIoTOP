package com.example.anton.biotop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
    }

    public void openFarmActivity() {
        Intent intent = new Intent(this, FarmActivity.class);
        startActivity(intent);
    }

    public void openAnimalActivity() {
        Intent intent = new Intent(this, AnimalActivity.class);
        startActivity(intent);
    }
}