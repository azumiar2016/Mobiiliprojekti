package suomi.fi;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private String TAG = WeatherActivity.class.getSimpleName();
    private ListView lv;
    String muncipality;
    String wind;

    ArrayList<HashMap<String, String>> datalist;
    HashMap<String, String> data;

    MenuClass mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mymenu = new MenuClass(this);

        Intent intent = getIntent();
        muncipality = intent.getStringExtra("municipality");
        datalist = new ArrayList<>();
        data = new HashMap<>();
        wind = "";
        lv = (ListView) findViewById(R.id.list);

        new GetData().execute();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null) {
            String lang= savedInstanceState.getString("lang");
            if(lang!=null) {
                Locale locale = new Locale(lang);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        String lang=mymenu.ln;
        outState.putString("lang",lang);
        super.onSaveInstanceState(outState);

    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WeatherActivity.this, getString(R.string.Toast_JSON_getting_data), Toast.LENGTH_LONG).show();
        }
            @Override
            protected Void doInBackground (Void...arg0){
                HttpHandler sh = new HttpHandler();

                String url = "http://api.apixu.com/v1/current.json?key=068da12c8e90483fad3230136171503&q=" + muncipality;
                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        JSONObject cities = jsonObj.getJSONObject("location");
                        String name = getString(R.string.Weather_Location) + cities.getString("name");
                        String time = getString(R.string.Weather_Time) + cities.getString("localtime");



                        JSONObject temps = jsonObj.getJSONObject("current");
                        String temperature = getString(R.string.Weather_Temperature) + temps.getString("temp_c") + getString(R.string.Weather_Temperature_2);
                        String humidity = getString(R.string.Weather_Humidity) + temps.getString("humidity") + getString(R.string.Weather_Humidity_2);
                        wind = temps.getString("wind_kph");
                        data.put("name", name);
                        data.put("time", time);
                        //data.put("wind", wind);
                        data.put("humidity", humidity);
                        data.put("temperature", temperature);


                        // adding data to data list
                      //  datalist.add(data);

                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.Toast_JSON_parsing_error) + e.getMessage(),
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
            void wind(){
                double value = Double.parseDouble(wind);
                value = value/3.6;
                value = Math.round( value * 10.0 ) / 10.0;
               wind = getString(R.string.Weather_Wind) + String.valueOf(value) + getString(R.string.Weather_Wind_2);
                data.put("wind",wind);
                datalist.add(data);

            }

            @Override
            protected void onPostExecute (Void result){
                super.onPostExecute(result);
                wind();
                ListAdapter adapter = new SimpleAdapter(WeatherActivity.this, datalist,
                        R.layout.weatherlist_item, new String[]{"name", "time", "temperature", "wind", "humidity"},
                        new int[]{R.id.cityName, R.id.aika, R.id.temperature, R.id.wind, R.id.humidity});
                lv.setAdapter(adapter);
            }
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            MenuItem lang = menu.findItem(R.id.Settings);
            menu.findItem(R.id.menuSearch).setVisible(false);
            mymenu.MenuFlag(lang, mymenu.ln);
            return super.onCreateOptionsMenu(menu);
        }

        // When menu item is selected
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);
            mymenu.SelectItem(item.getItemId());
            return true;
        }

    }

