package suomi.fi;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static suomi.fi.CustomAdapter.key;

/*
 * Main2Activity lists the items for selected button
 */
public class Main2Activity extends AppCompatActivity {

    ContentBuilder contentBuilder = new ContentBuilder();
    ArrayList<Article> arrayOfArticles = new ArrayList<Article>();
    String url;
    String jsonTAG;
    CustomUserAdapter adapter;
    JSONArray articleArrays;

    private int oidCounty;
    public String intentLock;
    public boolean fail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        intentLock = getIntent().getExtras().getString(key);
        Log.d("TAGI", "intentLock:" + intentLock);
        //If key is KEYkunnat get county oid for listing the municipipalities of
        // selected county
        if(intentLock.contains("KEYkunnat")) {
            String[] intentArray = getIntent().getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
            oidCounty =  Integer.parseInt(intentArray[1]);
            contentBuilder = new ContentBuilder();
            Log.d("TAGI", "oidCounty:" + oidCounty);
        }
        url = contentBuilder.BuildContent(intentLock, oidCounty);
        Log.d("TAGI", "contentbuilder url: " + url);
        jsonTAG = contentBuilder.m_JsonTAG;

        Log.d("TAGI", "jsonTAG: " + jsonTAG);


        new GetJSONData().execute();

    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main2Activity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

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
                                    "Json parsing error: " + e.getMessage(),
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

            //if json string fails
            if(fail == true){
                TextView tx = (TextView)findViewById(R.id.listTitle);
                tx.setText(getString(R.string.Service_unavailable));
            } else {
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
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // if search string length > 1
                if(newText.length() > 1) {

                    //reset array list
                    arrayOfArticles = new ArrayList<Article>();

                    //loop all articles
                    for(int i = 0; i < articleArrays.length(); i++) {
                        try {

                            //get single article object
                            JSONObject json_data = articleArrays.getJSONObject(i);

                            //get article title
                            //note: change this string to lower case to avoid case sensitive issue
                            String title = json_data.getString("@title").toLowerCase();

                            //if title contains the search string...
                            //note: change this string to lower case too
                            if(title.contains(newText.toLowerCase())) {

                                // add article on the ArrayList
                                arrayOfArticles.add( Article.getJSONobject(json_data));
                            }
                        }catch (final JSONException e){
                            Log.e("DEBUG", "Json parsing error: " + e.getMessage());
                        }
                    }

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) findViewById(R.id.list2);
                    adapter = new CustomUserAdapter(Main2Activity.this, arrayOfArticles);
                    adapter.passIntentKey(intentLock);
                    listView.setAdapter(adapter);
                }
                Log.d("TAGI", "arrayOfArticles: " + arrayOfArticles);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    // When menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case (R.id.Organizations):
                Toast.makeText(this, getString(R.string.Organizations)+" selected", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this, Main2Activity.class);
                intent1.putExtra(key, "KEYorganisaatiot");
                startActivity(intent1);
                return true;
            case (R.id.Municipalities):
                Toast.makeText(this, getString(R.string.Municipalities)+ " selected", Toast.LENGTH_LONG).show();
                intent1 = new Intent(this, Main2Activity.class);
                intent1.putExtra(key, "KEYmaakunnat");
                startActivity(intent1);
                return true;
            case (R.id.Forms):
                Toast.makeText(this, getString(R.string.Forms)+ " selected", Toast.LENGTH_LONG).show();
                intent1 = new Intent(this, Main2Activity.class);
                intent1.putExtra(key, "KEYlomakkeet");
                startActivity(intent1);
                return true;
            case (R.id.Links):
                Toast.makeText(this, getString(R.string.Links)+ " selected", Toast.LENGTH_LONG).show();
                intent1 = new Intent(this, Main2Activity.class);
                intent1.putExtra(key, "KEYlinkit");
                startActivity(intent1);
                return true;
            case (R.id.Settings):
                Toast.makeText(this, getString(R.string.Settings)+ " selected", Toast.LENGTH_LONG).show();
                return true;

            case (R.id.en_language):
                Toast.makeText(this, getString(R.string.Forms)+ " selected", Toast.LENGTH_LONG).show();
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                recreate();
                return true;

            case (R.id.fi_language):
                Toast.makeText(this, getString(R.string.Forms)+ " selected", Toast.LENGTH_LONG).show();
                locale = new Locale("fi");
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                recreate();
                return true;

            case (R.id.sv_language):
                Toast.makeText(this, getString(R.string.Forms)+ " selected", Toast.LENGTH_LONG).show();
                locale = new Locale("sv");
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                recreate();
                return true;
            /*case (R.id.Palvelut):
                Toast.makeText(this, "Palvelut selected", Toast.LENGTH_LONG).show();
                return true;
                */
        }
        return false;
    }


}
