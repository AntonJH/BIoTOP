package com.example.anton.biotop;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class FarmActivity extends AppCompatActivity {
    TextView moistureValue, temperatureValue, phValue, status;
    Button statusButton;
    Switch autoSwitch;
    boolean watering = false;

    String moist;
    String temp;
    String ph;

    private static final String RPI_SCRIPT_PATH = "./iot_project/farm.py ";
    boolean running = true;

    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("temp", test);
        super.onSaveInstanceState(outState);
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        if (savedInstanceState != null) {
            test = savedInstanceState.getString("temp");
            temperatureValue.setText(test);
        }

        */
        setContentView(R.layout.activity_farm);

        moistureValue = (TextView) findViewById(R.id.moistureShow);
        temperatureValue = (TextView) findViewById(R.id.temperatureShow);
        phValue = (TextView) findViewById(R.id.phShow);
        status = (TextView) findViewById(R.id.wateringStatusShow);
        autoSwitch = (Switch) findViewById(R.id.automaticSwitch);
        statusButton = (Button) findViewById(R.id.waterButton);

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!watering)
                    startWatering(true);
                else
                    startWatering(false);
                if (autoSwitch.isChecked())
                    autoSwitch.setChecked(false);
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

        AsyncTask<Integer, Integer, String> sensorDataTask = new AsyncTask<Integer, Integer, String>() {
            Boolean wateringReasoning = false;

            @Override
            protected String doInBackground(Integer... params) {

                /*
                String[] tempData = strBuild.toString().trim().split("\\n");
                for (int i = 0; i < tempData.length; i++) {
                    System.out.println("Array: " + tempData[i].toString());
                }

                String[] rad = tempData[7].split("\t");
                String temperatur = rad[3];

                return temperatur;
                */

                while (running) {
                    moist = run(RPI_SCRIPT_PATH + "moist");
                    temp = run(RPI_SCRIPT_PATH + "temp");
                    ph = run(RPI_SCRIPT_PATH + "ph");

                    wateringReasoning = Boolean.parseBoolean(run(RPI_SCRIPT_PATH + moist + " " + temp + " " + ph).toLowerCase());

                    publishProgress();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null; //test - var return temp innan
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                // super.onProgressUpdate(values);


                moistureValue.setText(moist + " %");
                System.out.println(moist);

                temperatureValue.setText(decimalTrim(temp) + " °C");
                System.out.println(temp);

                phValue.setText(decimalTrim(ph));
                System.out.println(ph);


                System.out.println("result boolean: " + wateringReasoning);
                if (checkAutomaticStatus())
                    startWatering(wateringReasoning);
            }

            protected void onPostExecute(String result) {
                moistureValue.setText(moist + " %");
                temperatureValue.setText(decimalTrim(temp) + " °C");
                phValue.setText(decimalTrim(ph));
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
       /* AsyncTask<Integer, Integer, String> phTask = new AsyncTask<Integer, Integer, String>() {
            Boolean running = true;

            @Override
            protected String doInBackground(Integer... params) {

                while (running) {
                    ph = run(RPI_SCRIPT_PATH + "ph");
                    publishProgress();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(ph);
                return ph;
            }


            @Override
            protected void onProgressUpdate(Integer... values) {
                // super.onProgressUpdate(values);
                phValue.setText(ph);
                System.out.println(ph);
            }

            protected void onPostExecute(String result) {
                phValue.setText(ph);
            }

        }.execute(1);*/


    class ActuatorOnTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            run("tdtool --on 1");
            return null;
        }
    }

    class ActuatorOffTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            run("tdtool --off 1");
            return null;
        }
    }

    void startWatering(boolean b) {
        if (b) {
            watering = true;
            status.setText(R.string.tv_watering_status_show_on);
            status.setTextColor(Color.GREEN);
            statusButton.setText(R.string.tv_watering_button_show_on);

            ActuatorOnTask wateringOnTask = new ActuatorOnTask();
            wateringOnTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//            new AsyncTask<Integer, Void, Void>() {
//                @Override
//                protected Void doInBackground(Integer... params) {
//                    run("tdtool --on 1");
//                    return null;
//                }
//
//            }.execute(1);
        } else {
            watering = false;
            status.setText(R.string.tv_watering_status_show_off);
            status.setTextColor(Color.RED);
            statusButton.setText(R.string.tv_watering_button_show_off);

            ActuatorOffTask wateringOffTask = new ActuatorOffTask();
            wateringOffTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//            new AsyncTask<Integer, Void, Void>() {
//                @Override
//                protected Void doInBackground(Integer... params) {
//                    run("tdtool --off 1");
//                    return null;
//                }
//
//            }.execute(1);
        }
    }

    boolean checkAutomaticStatus() {
        return autoSwitch.isChecked();
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