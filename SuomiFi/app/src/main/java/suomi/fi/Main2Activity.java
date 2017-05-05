package suomi.fi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import static suomi.fi.ButtonAdapter.key;


/*
 * Main2Activity lists the items for selected button
 */
public class Main2Activity extends AppCompatActivity {

    ContentBuilder contentBuilder = new ContentBuilder();
    static ArrayList<Article> arrayOfArticles = new ArrayList<Article>();
    String url;
    String jsonTAG;
    CustomUserAdapter adapter;
    static JSONArray articleArrays;

    private int oidCounty;
    public String intentLock;
    public String titleCounty;
    public boolean fail = false;

    MenuClass mymenu;
    SearchThread mysearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mymenu = new MenuClass(this);
        mysearch=null;


        intentLock = getIntent().getExtras().getString(key);
        Log.d("TAGI", "intentLock:" + intentLock);
        //If key is KEYkunnat get county oid for listing the municipipalities of
        // selected county
        if(intentLock.contains("KEYkunnat")) {
            String[] intentArray = getIntent().getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
            oidCounty =  Integer.parseInt(intentArray[1]);
            titleCounty =  intentArray[2];
            contentBuilder = new ContentBuilder();
            Log.d("TAGI", "oidCounty:" + oidCounty);
        }
        url = contentBuilder.BuildContent(intentLock, oidCounty);
        Log.d("TAGI", "contentbuilder url: " + url);
        jsonTAG = contentBuilder.m_JsonTAG;

        Log.d("TAGI", "jsonTAG: " + jsonTAG);


        new GetJSONData().execute();

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

    private class GetJSONData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main2Activity.this,getString(R.string.Toast_JSON_getting_data),Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            Log.e("TAGI", "Response from url: " + jsonStr);

            //if json string exists
            if(jsonStr != null)
            {
                try {
                    Log.e("TAGI", "trying jsonTAG: " +jsonTAG);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if(jsonTAG.contains("municipalities")){
                        JSONObject jsonObjsub = jsonObj.getJSONObject(jsonTAG);
                        articleArrays = jsonObjsub.getJSONArray("municipality");
                    } else {
                        articleArrays = jsonObj.getJSONArray(jsonTAG);
                    }
                    arrayOfArticles = Article.getArticles(articleArrays);


                }catch (final JSONException e){

                    Log.e("TAGI", "Json parsing error1: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.Toast_JSON_parsing_error) + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            // else set fail trigger on
            else{
                fail = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            TextView tx = (TextView)findViewById(R.id.listTitle);
            //if json string fails
            if(fail == true){

                tx.setText(getString(R.string.Service_unavailable));
            } else {
                Log.d("TAGI", "jsonTAG is:"+jsonTAG);
                switch(jsonTAG){
                    case "county":
                        tx.setText(getString(R.string.Counties));
                        break;
                    case "municipalities":
                        tx.setText(titleCounty + ": " + getString(R.string.Municipalities));
                        break;
                    case "organization":
                        tx.setText(getString(R.string.btn_Organizations));
                        break;
                    case "form":
                        tx.setText(getString(R.string.btn_Forms));
                        break;
                    case "link":
                        tx.setText(getString(R.string.Links));
                        break;
                }
                //tx.setText(getString(R.string.Service_unavailable));
                // Create the adapter to convert the array to views
                adapter = new CustomUserAdapter(Main2Activity.this, arrayOfArticles);
                adapter.passIntentKey(intentLock);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.list2);
                listView.setAdapter(adapter);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        MenuItem lang = menu.findItem(R.id.Settings);

        mymenu.MenuFlag(lang, mymenu.ln);


        SearchView searchView = (SearchView)item.getActionView();
        searchView.setIconified(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Search in Thread
                mysearch = new SearchThread(newText, Main2Activity.this);
                mysearch.start();

                //List results
                ListView listView = (ListView) findViewById(R.id.list2);
                adapter = new CustomUserAdapter(Main2Activity.this, arrayOfArticles);
                adapter.passIntentKey(intentLock);
                listView.setAdapter(adapter);

                if(newText.length() == 0){
                    Intent intent1 = new Intent(Main2Activity.this, Main2Activity.class);
                    intent1.putExtra(key, intentLock);
                    startActivity(intent1);
                    finish();
                }
                return false;
            }
        });


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
