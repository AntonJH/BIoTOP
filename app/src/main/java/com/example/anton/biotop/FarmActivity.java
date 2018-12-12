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
    Button statusButton, upButton, downButton;
    TextView moistureValue;
    TextView status;
    Switch auto;
    boolean watering = false;
    int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);

        moistureValue = (TextView) findViewById(R.id.moistureShow);
        status = findViewById(R.id.wateringStatusShow);
        auto = findViewById(R.id.autoSwitch);
        statusButton = findViewById(R.id.waterButton);
        upButton = findViewById(R.id.buttonUp);
        downButton = findViewById(R.id.buttonDown);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                moistureValue.setText(n + "");
                checkMoist();
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n--;
                moistureValue.setText(n + "");
                checkMoist();
            }
        });

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!watering) {
                    Random rand = new Random();
                    n = rand.nextInt(50) + 1;
                    checkMoist();

                    moistureValue.setText(n + "");
                    status.setText(R.string.txv_watering_status_show_on);
                    statusButton.setText(R.string.txv_watering_button_show_off);
                    watering = true;
                } else {
                    moistureValue.setText("8");
                    status.setText(R.string.txv_watering_status_show_off);
                    statusButton.setText(R.string.txv_watering_button_show_off);
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

    void checkMoist(){
        if (n > 25) {
            auto.setChecked(false);
        } else {
            auto.setChecked(true);
        }
    }
}