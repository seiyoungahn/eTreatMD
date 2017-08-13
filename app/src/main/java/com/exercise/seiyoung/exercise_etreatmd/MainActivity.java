package com.exercise.seiyoung.exercise_etreatmd;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PatientFetcher().execute();

    }

    private void populateListView() {
        ArrayList<Patient> patients = DataHolder.getPatients();
        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(this, R.layout.listview_item, patients);
        ListView listView = (ListView) findViewById(R.id.id_main_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int pos, long id){
                Patient patient = (Patient) parent.getAdapter().getItem(pos);
                Intent intent = new Intent(MainActivity.this, PatientActivity.class);
                intent.putExtra("patient", patient);
                startActivity(intent);
            }
        });
    }

    private class PatientFetcher extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            ArrayList<Patient> patients = new ArrayList<Patient>();
            try {
                URL url = new URL("http://159.203.62.239:3000/patients.json");
                HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                myConnection.setConnectTimeout(1000);
                myConnection.setRequestMethod("GET");
                if (myConnection.getResponseCode() == 200) {
                    //Successful connection
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        String name = jsonReader.nextName();
                        String id = jsonReader.nextString();
                        patients.add(new Patient(name, id));
                    }
                    DataHolder.setPatients(patients);
                } else {
                    //Connection failed, fetch data locally
                    fetchLocally();
                }
                return null;
            } catch (IOException e){
                //Timeout(or other exceptions), fetch data locally
                e.printStackTrace();
                fetchLocally();
                return null;
            }
        }

        private void fetchLocally(){
            try {
                AssetManager assetManager = getResources().getAssets();
                InputStream inputStream = assetManager.open("patients.json");
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader streamReader = new BufferedReader(isr);
                StringBuilder strBuilder = new StringBuilder();
                String input;
                while ((input = streamReader.readLine()) != null)
                    strBuilder.append(input);
                JSONArray jsonArr = new JSONArray(strBuilder.toString());
                ArrayList<Patient> patients = new ArrayList<Patient>();
                for (int i = 0; i < jsonArr.length(); i++){
                    JSONObject row = jsonArr.getJSONObject(i);
                    String id = row.getString("id");
                    String name = row.getString("name");
                    patients.add(new Patient(name, id));
                }
                DataHolder.setPatients(patients);
                streamReader.close();
                isr.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void param){
            populateListView();
        }
    }

}
