package saa.ohjelma;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherActivty extends AppCompatActivity {

    private String TAG = WeatherActivty.class.getSimpleName();
    private ListView lv;
    String muncipality;

    ArrayList<HashMap<String, String>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent intent = getIntent();
         muncipality = intent.getStringExtra("muncipality");
        datalist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WeatherActivty.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String url = "http://api.apixu.com/v1/current.json?key=068da12c8e90483fad3230136171503&q="+muncipality;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject cities = jsonObj.getJSONObject("location");
                    String name ="Kaupunki: " + muncipality;
                    String time ="Aika: " + cities.getString("localtime");

                        HashMap<String, String> data = new HashMap<>();

                    JSONObject temps = jsonObj.getJSONObject("current");
                    String temperature = "Lämpötila: " + temps.getString("temp_c");
                    String humidity = "kosteus: " + temps.getString("humidity");
                    String Wind = "Tuulen nopeus: " + temps.getString("wind_kph")+ " km/h";

                    data.put("name", name);
                    data.put("time", time);
                    data.put("wind", Wind);
                    data.put("humidity", humidity);
                    data.put("temp_c", temperature);


                        // adding contact to contact list
                        datalist.add(data);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(WeatherActivty.this, datalist,
                    R.layout.list_item, new String[]{ "name", "time", "temp_c","wind","humidity"},
                    new int[]{R.id.cityName, R.id.aika, R.id.temperature,R.id.rainprob,R.id.humidity});
            lv.setAdapter(adapter);
        }
    }
}