package suomi.fi;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static suomi.fi.CustomAdapter.key;

public class LinkActivity extends AppCompatActivity {

    public String oid;
    String url;
    TextView textTitle;
    Button btnLink;
    String m_UrlOID;
    LinkDetail Links;
    public JSONObject links;
    ContentBuilder contentBuilder = new ContentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        textTitle = (TextView) findViewById(R.id.textTitle);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String[] intentArray = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
        m_UrlOID = intent.getStringExtra("oid");

        contentBuilder.BuildContent("KEYlinkit", 0);
        url = contentBuilder.oidURL[0] + m_UrlOID + contentBuilder.apiKEY;

        Log.d("TAGI", "LinkActivity started with url: " + url);

        new GetJSONData().execute();

    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            JSONObject jsonObj = new JSONObject();
            Log.e("TAGI", "Response from url: " + jsonStr);

            //if json string exists
            if(jsonStr != null)
                try {
                    jsonObj = new JSONObject(jsonStr);
                    oid = jsonObj.getString("oid");
                    links = new JSONObject(jsonObj.getString("links"));
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

                    if(oid != null) {
                        Links = new LinkDetail(links, "links");
                        Log.d("TAGI", "links: " + links);
                    }
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("TAGI", "oid: " + oid);
            super.onPostExecute(result);
            if (oid != null) {
                textTitle.setText(Links.title);
                btnLink.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Links.link));
                        startActivity(intent);
                    }
                });
            } else {
                textTitle.setText("Links not found");
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TAGI", "Form Menu: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem lang = menu.findItem(R.id.Settings);
        menu.findItem(R.id.menuSearch).setVisible(false);


        Log.d("TAGI", "Form Menu: ");
        switch(Locale.getDefault().getLanguage()){
            case "sv":
                lang.setIcon(getResources().getDrawable(R.mipmap.ruotsi_item));
                break;
            case "en":
                lang.setIcon(getResources().getDrawable(R.mipmap.englanti_item));
                break;
            default:
                lang.setIcon(getResources().getDrawable(R.mipmap.suomi_item_2));
                break;
        }
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
