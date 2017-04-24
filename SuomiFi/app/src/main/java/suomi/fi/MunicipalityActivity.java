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
    String m_UrlOID;
    String m_ArticleString;
    String m_IntentKEYType;
    String url;
    ContentBuilder contentBuilder = new ContentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

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
                                    "Json parsing error: " + e.getMessage(),
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
        Log.d("TAGI", "Municipality Menu: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem lang = menu.findItem(R.id.Settings);
        menu.findItem(R.id.menuSearch).setVisible(false);


        Log.d("TAGI", "Municipality Menu: ");
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
