package com.example.anton.biotop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class FarmActivity extends AppCompatActivity {
    Button statusButton;
    TextView moistureValue;
    TextView status;
    Switch auto;
    boolean watering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);

        moistureValue = (TextView) findViewById(R.id.moistureShow);
        status = (TextView) findViewById(R.id.wateringStatusShow);

        auto = (Switch) findViewById(R.id.autoSwitch);

        statusButton = findViewById(R.id.waterButton);
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!watering) {
                    Random rand = new Random();
                    int n = rand.nextInt(50) + 1;
                    if (n > 25) {
                        auto.setChecked(false);
                    } else{
                        auto.setChecked(true);
                    }

                    moistureValue.setText(n+"");
                    status.setText("On");
                    statusButton.setText("Avbryt");
                    watering = true;
                }
                else {
                    moistureValue.setText("8");
                    status.setText("Off");
                    statusButton.setText("Starta");
                    watering = false;
                }
            }
        });

        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    statusButton.setEnabled(true);
                else
                    statusButton.setEnabled(false);
            }
        });
    }
}
