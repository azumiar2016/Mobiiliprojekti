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
import static suomi.fi.ButtonAdapter.key;

public class MunicipalityActivity extends AppCompatActivity {

    public String title;
    public JSONObject details;
    public JSONObject statistics;
    public JSONObject financial;
    public JSONObject comparison;
    MunicipalityDetail municDetails, municStatistics;

    TextView txtTitle;
    TextView txtAddress;
    TextView txtPhone;
    TextView txtEmail;
    TextView txtWebsite;
    Button btnEmail;
    Button btnWebsite;
    Button btnGeneral;
    Button btnFinancial;
    Button btnMap;
    Button btnCall;
    Button btnWeather;
    String m_UrlOID;
    String m_ArticleString;
    String m_IntentKEYType;
    String url;

    ContentBuilder contentBuilder = new ContentBuilder();

    MenuClass mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

        mymenu = new MenuClass(this);

        txtTitle = (TextView)findViewById(R.id.textTitle);
        btnEmail = (Button)findViewById(R.id.btnEmail);
        txtAddress = (TextView)findViewById(R.id.textAddress);
        txtPhone = (TextView)findViewById(R.id.textPhone);
        txtEmail = (TextView)findViewById(R.id.textEmail);
        txtWebsite = (TextView)findViewById(R.id.textWebsite);
        btnWebsite = (Button)findViewById(R.id.btnWebsite);
        btnGeneral = (Button)findViewById(R.id.btnGeneral);
        btnFinancial = (Button)findViewById(R.id.btnFinancial);
        btnMap = (Button)findViewById(R.id.btnMap);
        btnCall = (Button)findViewById(R.id.btnPhoneCall);
        btnWeather = (Button)findViewById(R.id.btnweather);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        m_UrlOID = intent.getStringExtra("oid");

        //m_IntentKEYType = intentArray[0];
        //m_UrlOID =  intentArray[1];

        contentBuilder.BuildContent("KEYkunnat", 0);
        url = contentBuilder.oidURL[0] + m_UrlOID + contentBuilder.apiKEY;

        Log.d("TAGI", "MunicipalityActivity started with url: " +url );

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
            if(!jsonStr.contains("No municipalities found"))
            {
                try {

                    jsonObj = new JSONObject(jsonStr);
                    title = jsonObj.getString("title");
                    details = new JSONObject(jsonObj.getString("details"));
                    statistics = new JSONObject(jsonObj.getString("statistics"));

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
                if(title != null) {
                    municDetails = new MunicipalityDetail(details, "details");
                    municStatistics = new MunicipalityDetail(statistics, "statistics");
                    Log.d("TAGI", "municDetails: " + details);
                    Log.d("TAGI", "municStatistics: " + statistics);
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
                txtAddress.setText(municDetails.address);
                txtPhone.setText(municDetails.phone);
                txtEmail.setText(municDetails.email);
                txtWebsite.setText(municDetails.website);


                btnWebsite.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(municDetails.website));
                        startActivity(intent);
                    }
                });
                btnEmail.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+municDetails.email));
                        startActivity(intent);
                    }
                });

                btnGeneral.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(municStatistics.gen_url));
                        startActivity(intent);
                    }
                });

                btnFinancial.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(municStatistics.fin_url));
                        startActivity(intent);
                    }
                });

                btnMap.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+title));
                        startActivity(intent);
                    }
                });

                btnCall.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + municDetails.phone));
                        startActivity(intent);
                    }
                });

                btnWeather.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MunicipalityActivity.this, WeatherActivity.class);
                        intent.putExtra("municipality",title);
                        startActivity(intent);
                    }
                });
            }
            else
            {
                txtTitle.setText(R.string.municipality_not_found);
                findViewById(R.id.municContent).setVisibility(View.INVISIBLE);
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
