package com.example.anton.biotop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnimalHealthActivity extends AppCompatActivity {

    Bundle b = getIntent().getExtras();
    int i = b.getInt("id");

    TextView id = (TextView) findViewById(R.id.test1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_health);

        id.setText(i+"");

    }
}
