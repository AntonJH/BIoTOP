package com.example.anton.biotop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnimalHealthActivity extends AppCompatActivity {
    private TextView animalType, animalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_health);
        getIncomingIntent();
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