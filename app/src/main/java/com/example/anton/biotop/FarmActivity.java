package com.example.anton.biotop;

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
    TextView moistureValue, temperatureValue, status;
    Button statusButton, upButton, downButton;
    Switch autoSwitch;
    boolean watering = false;
    int n = 0;
    String test;

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
        status = (TextView) findViewById(R.id.wateringStatusShow);
        autoSwitch = (Switch) findViewById(R.id.automaticSwitch);
        statusButton = (Button) findViewById(R.id.waterButton);
        upButton = (Button) findViewById(R.id.buttonUp);
        downButton = (Button) findViewById(R.id.buttonDown);

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

        new AsyncTask<Integer, Integer, String>() {

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

                int i = 0;
                while (i <= 50) {
                    test = run("./iot_project/test.py");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(i);
                }

                System.out.println(test);
                return test;
            }



            @Override
            protected void onProgressUpdate(Integer...  values) {
               // super.onProgressUpdate(values);
                temperatureValue.setText(test);
                System.out.println(test);

            }

            protected void onPostExecute(String result) {
                temperatureValue.setText(result);
            }

        }.execute(1);

    }


    void startWatering(boolean b) {
        if (b) {
            watering = true;
            status.setText(R.string.tv_watering_status_show_on);
            statusButton.setText(R.string.tv_watering_button_show_on);
            new AsyncTask<Integer, Void, Void>() {
                @Override
                protected Void doInBackground(Integer... params) {
                    run("tdtool --on 1");
                    return null;
                }

            }.execute(1);
        } else {
            watering = false;
            status.setText(R.string.tv_watering_status_show_off);
            statusButton.setText(R.string.tv_watering_button_show_off);
            new AsyncTask<Integer, Void, Void>() {
                @Override
                protected Void doInBackground(Integer... params) {
                    run("tdtool --off 1");
                    return null;
                }

            }.execute(1);
        }
    }

    void checkMoisture() {
        if (autoSwitch.isChecked()) {
            if (n < 10)
                startWatering(true);
            else
                startWatering(false);
        }
    }

    public String run(String command) {
       // String strBuild = new StringBuilder();
        String strBuild = "";

        String hostname = "169.254.224.24";
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
            ses.close(); // Close this session
            con.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
        return strBuild;
    }
}