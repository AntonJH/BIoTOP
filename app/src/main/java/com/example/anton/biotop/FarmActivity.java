package com.example.anton.biotop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

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
        auto = findViewById(R.id.automaticSwitch);
        statusButton = findViewById(R.id.waterButton);
        upButton = findViewById(R.id.buttonUp);
        downButton = findViewById(R.id.buttonDown);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                moistureValue.setText(n + "");
                checkMoisture();
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n--;
                moistureValue.setText(n + "");
                checkMoisture();
            }
        });

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!watering)
                    startWatering(true);
                else
                    startWatering(false);
                if (auto.isChecked())
                    auto.setChecked(false);
            }
        });
/*
        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    upButton.setEnabled(true);
                    downButton.setEnabled(true);
                } else {
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                }
            }
        });
*/
    }

    void startWatering(boolean b) {
        if (b) {
            watering = true;
            status.setText(R.string.tv_watering_status_show_on);
            statusButton.setText(R.string.tv_watering_button_show_on);
        } else {
            watering = false;
            status.setText(R.string.tv_watering_status_show_off);
            statusButton.setText(R.string.tv_watering_button_show_off);
        }
    }

    void checkMoisture() {
        if (auto.isChecked()) {
            if (n < 10)
                startWatering(true);
            else
                startWatering(false);
        }
    }
}