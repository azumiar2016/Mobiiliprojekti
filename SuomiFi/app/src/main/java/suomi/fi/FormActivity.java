package suomi.fi;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static suomi.fi.ButtonAdapter.key;

public class FormActivity extends AppCompatActivity {

    public String m_UrlOID;
    public String title;
    public String corporate;
    public String corporateurl;
    public String url;
    public String description;
    ContentBuilder contentBuilder = new ContentBuilder();
    TextView txtTitle;
    TextView txtCorporate;
    WebView htmlDescription;
    Button btnCorporateurl;
    Button btnForm;

    MenuClass mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mymenu = new MenuClass(this);

        txtTitle = (TextView)findViewById(R.id.textTitle);
        txtCorporate = (TextView)findViewById(R.id.txtCorporate);
        htmlDescription = (WebView)findViewById(R.id.htmlDescription);
        btnCorporateurl = (Button)findViewById(R.id.btnWebsite);
        btnForm = (Button)findViewById(R.id.btnForm);

        Intent intent = getIntent();
        m_UrlOID = intent.getStringExtra("oid");
        Log.d("TAGI", "m_UrlOID: " +m_UrlOID) ;
        contentBuilder.BuildContent("KEYlomakkeet", 0);
        url = contentBuilder.oidURL[0] + m_UrlOID + contentBuilder.apiKEY;
        Log.d("TAGI", "FormURL: "+ url);
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

        }

        @Override
        protected Void doInBackground(Void... arg0)
        {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            JSONObject jsonObj = new JSONObject();
            Log.e("TAGI", "Response from url: " + jsonStr);
            if(jsonStr != null)
            {
                try {

                    jsonObj = new JSONObject(jsonStr);
                    title = jsonObj.getString("title");
                    corporate = jsonObj.getString("corporate");
                    corporateurl = jsonObj.getString("corporateurl");
                    description = jsonObj.getString("description");
                    url = jsonObj.getString("url");

                }catch (final JSONException e){

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


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.d("TAGI", "title: " + title );
            super.onPostExecute(result);
            if(title != null) {
                txtTitle.setText(title);
                txtCorporate.setText(corporate);
                description = description.replace("]]>", "");
                description = description.replace("<![CDATA[", "");
                htmlDescription.loadData(description, "text/html; charset=UTF-8", null);
                btnCorporateurl.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(corporateurl));
                        startActivity(intent);
                    }
                });
                btnForm.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
            else
            {
                txtTitle.setText(R.string.form_not_found);
            }

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