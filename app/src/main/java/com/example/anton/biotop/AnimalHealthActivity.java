package com.example.anton.biotop;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class AnimalHealthActivity extends AppCompatActivity {
    private TextView animalType, animalID, bodyTempValue, pulseValue, pressureValue, statusValue, statusDescValue;

    private String temp, pulse, bloodPressureSystolic, bloodPressureDiastolic, healthStatus;
    StringBuilder healthDesc = new StringBuilder();

    private List<Animal> animalList = new ArrayList<>();

    private static final String RPI_SCRIPT_PATH = "./iot_project/animal.py ";
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_health);

        bodyTempValue = (TextView) findViewById(R.id.body_temperature_show);
        pulseValue = (TextView) findViewById(R.id.pulse_show);
        pressureValue = (TextView) findViewById(R.id.blood_pressure_show);
        statusValue = (TextView) findViewById(R.id.animal_status_show);
        statusDescValue = (TextView) findViewById(R.id.animal_status_desc);
        healthDesc = new StringBuilder();

        getIncomingIntent();

        AsyncTask<Integer, Integer, String> sensorDataTask = new AsyncTask<Integer, Integer, String>() {
            @Override
            protected String doInBackground(Integer... params) {
                while (running) {
                    if (running) {
                        temp = run(RPI_SCRIPT_PATH + "temp");
                    } else {
                        break;
                    }


                    if (running) {
                        pulse = run(RPI_SCRIPT_PATH + "pulse");
                    } else {
                        break;
                    }


                    String bloodPressure = "";
                    if (running) {
                        bloodPressure = run(RPI_SCRIPT_PATH + "pressure");
                    } else {
                        break;
                    }

                    String[] bloodPressureTypes = bloodPressure.split(" ");
                    bloodPressureSystolic = bloodPressureTypes[0];
                    bloodPressureDiastolic = bloodPressureTypes[1];

                    String healthReasoning = run(RPI_SCRIPT_PATH + temp + " " + pulse + " " + bloodPressureSystolic + " " + bloodPressureDiastolic);
                    String[] reasoningData = healthReasoning.split("-");


                    if (reasoningData[0].equals("terrible")) {
                        healthStatus = "NEGATIV";
                    } else if (reasoningData[0].equals("bad")) {
                        healthStatus = "RISK";
                    } else {
                        healthStatus = "POSITIV";
                    }

                    healthDesc.setLength(0);

                    for (int i = 1; i < reasoningData.length; i++) {
                        healthDesc = healthDesc.append("- " + reasoningData[i] + "\n");
                    }

                    if (running) {
                        publishProgress();
                    } else {
                        break;
                    }

                    SystemClock.sleep(5000);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                bodyTempValue.setText(decimalTrim(temp) + " °C");
                System.out.println(temp);

                pulseValue.setText(pulse + " BPM");
                System.out.println(pulse);

                pressureValue.setText(bloodPressureSystolic + "/" + bloodPressureDiastolic + " mm Hg");
                System.out.println(bloodPressureSystolic + "/" + bloodPressureDiastolic);

                statusValue.setText(healthStatus);
                if (healthStatus.equals("POSITIV")) {
                    statusValue.setTextColor(Color.GREEN);
                } else if (healthStatus.equals("RISK")) {
                    statusValue.setTextColor(Color.YELLOW);
                } else {
                    statusValue.setTextColor(Color.RED);
                }

                System.out.println("result boolean: " + healthStatus);
                statusDescValue.setText(healthDesc);
            }

            protected void onPostExecute(String result) {
                try {
                    bodyTempValue.setText(decimalTrim(temp) + " °C");
                    pulseValue.setText(pulse + " BPM");
                    pressureValue.setText(bloodPressureSystolic + "/" + bloodPressureDiastolic + " mm Hg");
                    statusValue.setText(healthStatus);
                    if (healthStatus.equals("POSITIV")) {
                        statusValue.setTextColor(Color.GREEN);
                    } else if (healthStatus.equals("RISK")) {
                        statusValue.setTextColor(Color.YELLOW);
                    } else {
                        statusValue.setTextColor(Color.RED);
                    }
                    statusDescValue.setText(healthDesc);
                } catch (NullPointerException e) {
                    System.out.println("Null varning.");
                }
            }

        }.execute(1);
    }

    String decimalTrim(String floatValue) {
        if (floatValue.charAt(1) == '.')
            return floatValue.substring(0, 3);
        return floatValue.substring(0, 4);
    }

    protected void onPause() {
        super.onPause();
        finish();
        System.out.println("Pause");
        running = false;
    }

    protected void onStop() {
        super.onStop();
        System.out.println("Stop");
        running = false;
    }

    protected void onDestroy() {
        super.onDestroy();
        running = false;
        System.out.println("Destroy");
    }

    Intent createIntent() {
        Intent intent = new Intent(this, AnimalActivity.class);
        intent.putExtra("status", statusValue.getText());
        intent.putExtra("id", animalID.getText());
        intent.putExtra("temp", temp);
        intent.putExtra("pulse", pulse);
        intent.putExtra("pressureS", bloodPressureSystolic);
        intent.putExtra("pressureD", bloodPressureDiastolic);
        intent.putExtra("desc", healthDesc.toString());
        intent.putExtra("list", (Serializable) animalList);

        return intent;
    }

    @Override
    public void onBackPressed() {
        running = false;
        startActivity(createIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                running = false;
                startActivity(createIntent());

                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("type") && getIntent().hasExtra("status")) {
            String id = getIntent().getStringExtra("id");
            String type = getIntent().getStringExtra("type");
            String status = getIntent().getStringExtra("status");

            temp = getIntent().getStringExtra("temp");
            pulse = getIntent().getStringExtra("pulse");
            bloodPressureSystolic = getIntent().getStringExtra("pressureS");
            bloodPressureDiastolic = getIntent().getStringExtra("pressureD");

            healthDesc.setLength(0);
            healthDesc.append(getIntent().getStringExtra("desc"));

            animalList = (List<Animal>) getIntent().getSerializableExtra("list");

            setView(id, type, status);
        }
    }

    private void setView(String id, String type, String status) {
        animalType = findViewById(R.id.animal_type);
        animalType.setText(type);
        animalID = findViewById(R.id.animal_id);
        animalID.setText(id);

        bodyTempValue.setText(decimalTrim(temp) + " °C");
        pulseValue.setText(pulse + " BPM");
        pressureValue.setText(bloodPressureSystolic + "/" + bloodPressureDiastolic + " mm Hg");
        statusDescValue.setText(healthDesc);


        if (status.equals("POSITIV")) {
            statusValue.setTextColor(Color.GREEN);
        } else if (status.equals("RISK")) {
            statusValue.setTextColor(Color.YELLOW);
        } else {
            statusValue.setTextColor(Color.RED);
        }

        statusValue.setText(status);
    }

    public String run(String command) {
        String str = "";

        String hostname = "192.168.1.10"; //169.254.224.24
        String username = "pi";
        String password = "raspberry";

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Connection con = new Connection(hostname);
            con.connect();
            boolean isAuthenticated = con.authenticateWithPassword(username, password);
            if (!isAuthenticated)
                throw new IOException("Authentication failed.");
            Session ses = con.openSession();
            ses.execCommand(command);
            InputStream stdout = new StreamGobbler(ses.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            str = br.readLine();

            System.out.println("ExitCode: " + ses.getExitStatus());
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
        return str;
    }
}