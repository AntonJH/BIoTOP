package com.example.anton.biotop;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class AnimalHealthActivity extends AppCompatActivity {
    private TextView animalType, animalID, bodyTempValue, pulseValue, pressureValue, statusValue, statusDescValue;

    private String temp, pulse, bloodPressureSystolic, bloodPressureDiastolic;

    private static final String RPI_SCRIPT_PATH = "./iot_project/animal.py ";
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_health);
        getIncomingIntent();

        bodyTempValue = (TextView) findViewById(R.id.body_temperature_show);
        pulseValue = (TextView) findViewById(R.id.pulse_show);
        pressureValue = (TextView) findViewById(R.id.blood_pressure_show);
        statusValue = (TextView) findViewById(R.id.animal_status_show);
        statusDescValue = (TextView) findViewById(R.id.animal_status_desc);

        AsyncTask<Integer, Integer, String> sensorDataTask = new AsyncTask<Integer, Integer, String>() {
            String healthStatus = "";
            StringBuilder healthDesc = new StringBuilder();

            @Override
            protected String doInBackground(Integer... params) {
                while (running) {
                    temp = run(RPI_SCRIPT_PATH + "temp");
                    pulse = run(RPI_SCRIPT_PATH + "pulse");
                    String bloodPressure = run(RPI_SCRIPT_PATH + "pressure");

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

                    publishProgress();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        System.out.println("Pause");
    }

    protected void onStop() {
        super.onStop();
        System.out.println("Stop");
    }

    protected void onDestroy() {
        super.onDestroy();
        running = false;
        System.out.println("Destroy");

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("type")) {
            String id = getIntent().getStringExtra("id");
            String type = getIntent().getStringExtra("type");
            setView(id, type);
        }
    }

    private void setView(String id, String type) {
        animalType = findViewById(R.id.animal_type);
        animalType.setText(type);
        animalID = findViewById(R.id.animal_id);
        animalID.setText(id);
    }

    public String run(String command) {
        // String strBuild = new StringBuilder();
        String strBuild = "";

        String hostname = "192.168.1.10"; //169.254.224.24
        String username = "pi";
        String password = "raspberry";

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Connection con = new Connection(hostname); //init connection
            con.connect(); //start connection to the hostname
            boolean isAuthenticated = con.authenticateWithPassword(username, password);
            if (!isAuthenticated)
                throw new IOException("Authentication failed.");
            Session ses = con.openSession();
            ses.execCommand(command);
            InputStream stdout = new StreamGobbler(ses.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout)); //reads text

            /*
            while (true) {
                String line = br.readLine(); // read line
                if (line == null)
                    break;
                strBuild.append(line + "\n");
                System.out.println(line);
            }
            */

            strBuild = br.readLine();

            System.out.println("ExitCode: " + ses.getExitStatus());
            // ses.close(); // Close this session
            // con.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
        return strBuild;
    }
}

/*

package com.example.anhu6690.iotlab2app;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class MainActivity extends AppCompatActivity {

    TextView txv_temp_indoor = null;
    TextView txv_outdoor_light_on_temp = null;
    Switch btnToggle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv_temp_indoor = (TextView) findViewById(R.id.indoorTempShow);

        btnToggle = (Switch) findViewById(R.id.btnToggle);

        txv_outdoor_light_on_temp = (TextView) findViewById(R.id.outdoorLightShow);

        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { //do something if checked
                    new AsyncTask<Integer, Void, Void>(){
                        @Override
                        protected Void doInBackground(Integer... params)
                        {
                            run("tdtool --on 1");
                            return null;
                        }

                    }.execute(1);

                    txv_outdoor_light_on_temp.setText(R.string.txv_outdoor_light_on);
                } else { // to do something if not checked
                    new AsyncTask<Integer, Void, Void>(){
                        @Override
                        protected Void doInBackground(Integer... params)
                        {
                            run("tdtool --off 1");
                            return null;
                        }

                    }.execute(1);
                }
            }
        });


        new AsyncTask<Integer, Void, String>(){
            @Override
            protected String doInBackground(Integer... params)
            {
                //your code to fetch results via SSH
                StringBuilder strngBuild = run("tdtool -l");


                String[] tempData = strngBuild.toString().trim().split("\\n");
                for (int i = 0; i < tempData.length; i++) {
                    System.out.println("Array: " + tempData[i].toString());
                }

                String[] rad = tempData[7].split("\t");
                String temperatur = rad[3];

                return temperatur;
            }

            protected void onPostExecute(String result) {
                txv_temp_indoor.setText(result);
            }

        }.execute(1);

    }


    public StringBuilder run(String command) {
        StringBuilder strgBuild = new StringBuilder();

        String hostname = "130.237.177.214";
        String username = "pi";
        String password = "raspberry";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Connection conn = new Connection(hostname); //init connection
            conn.connect(); //start connection to the hostname
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout)); //reads text


            while (true) {
                String line = br.readLine(); // read line
                if (line == null)
                    break;
                strgBuild.append(line + "\n");
                System.out.println(line);
            }
*/

            /*
            Show exit status, if available (otherwise "null")
            System.out.println("ExitCode: " + sess.getExitStatus());
                    sess.close(); // Close this session
                    conn.close();
                    } catch (IOException e) {
                    e.printStackTrace(System.err);
                    System.exit(2);
                    }
                    return strgBuild;
                    }


                    }
 */