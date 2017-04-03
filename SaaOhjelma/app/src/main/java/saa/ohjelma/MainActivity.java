package saa.ohjelma;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {
    String EXTRA_MESSAGE = "";
    public static final String WEBSITE_API = "/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
    public static final String WEBSITE_URL = "http://www.suomi.fi/rest/municipalities/";
    ArrayList<Article> arrayOfArticles = new ArrayList<Article>();
    JSONArray articleArrays;
    CustomUserAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetJSONData().execute();


    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String url = "http://www.suomi.fi/rest/municipalities/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    articleArrays = jsonObj.getJSONArray("municipality");
                    arrayOfArticles = Article.getArticles(articleArrays);


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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Create the adapter to convert the array to views
            adapter = new CustomUserAdapter(MainActivity.this, arrayOfArticles);
            // Attach the adapter to a ListView
            listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // if search string length > 1
                if (newText.length() > 1) {

                    //reset array list
                    arrayOfArticles = new ArrayList<Article>();

                    //loop all articles
                    for (int i = 0; i < articleArrays.length(); i++) {
                        try {

                            //get single article object
                            JSONObject json_data = articleArrays.getJSONObject(i);

                            //get article title
                            //note: change this string to lower case to avoid case sensitive issue
                            String title = json_data.getString("@title").toLowerCase();

                            //if title contains the search string...
                            //note: change this string to lower case too
                            if (title.contains(newText.toLowerCase())) {

                                // add article on the ArrayList
                                arrayOfArticles.add(Article.getJSONobject(json_data));
                            }
                        } catch (final JSONException e) {
                            Log.e("DEBUG", "Json parsing error: " + e.getMessage());
                        }
                    }

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) findViewById(R.id.list);
                    adapter = new CustomUserAdapter(MainActivity.this, arrayOfArticles);

                    listView.setAdapter(adapter);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}