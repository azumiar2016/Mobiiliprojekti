package suomi.fi;

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



public class kohde extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "suomi.fi.MESSAGE";
    public static final String WEBSITE_API = "/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
    public static final String WEBSITE_URL = "http://www.suomi.fi/rest/en/services/";
    CustomUserAdapter adapter;
    ArrayList<Article> arrayOfArticles = new ArrayList<Article>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kohde);

        new GetJSONData().execute();


    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(kohde.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            HttpHandler sh = new HttpHandler();
            String url = "http://www.suomi.fi/rest/en/services/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null)
            {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray articleArrays = jsonObj.getJSONArray("serviceclass");
                    arrayOfArticles = Article.getArticles(articleArrays);


                }catch (final JSONException e){

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
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // Create the adapter to convert the array to views

            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.lvUsers);
            adapter = new CustomUserAdapter(kohde.this, arrayOfArticles);
            listView.setAdapter(adapter);

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
                adapter.getFilter().filter(newText);

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
            case (R.id.Organisaatiot):
                Toast.makeText(this, "Organisaatiot selected", Toast.LENGTH_LONG).show();
                return true;
            case (R.id.Kunnat):
                Toast.makeText(this, "Kunnat selected", Toast.LENGTH_LONG).show();
                return true;
            case (R.id.Palvelut):
                Toast.makeText(this, "Palvelut selected", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }
}
