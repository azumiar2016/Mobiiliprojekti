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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LinkActivity extends AppCompatActivity {

    public String m_UrlOID;
    public String title;
    public String url;
    public String description;
    public String externalurl;
    public String organizationOid;
    public String organizationName;
    public String organizationContactUrl;
    public String organizationUrl;
    public String municipalityOid;
    public String municipalityTitle;
    ContentBuilder contentBuilder = new ContentBuilder();
    JSONArray municipalityArray;
    JSONObject municipalityObject;
    TextView txtTitle;
    WebView htmlDescription;
    TextView txtOrganizationTitle;
    TextView txtOrganizationName;
    TextView txtMunicipalityName;
    Button btnExternalurl;
    Button btnOrganization;
    Button btnMunicipality;


    List<String> buttonArray = new ArrayList<String>();
    List<Integer> buttonOidArray = new ArrayList<Integer>();
    ArrayList<ButtonHandler> arrayOfBatches = new ArrayList<>();

    MenuClass mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        mymenu = new MenuClass(this);

        Button btnWebsite = (Button)findViewById(R.id.btnWebsite);
        Button btnGeneral = (Button)findViewById(R.id.btnGeneral);
        txtTitle = (TextView)findViewById(R.id.textTitle);
        txtOrganizationTitle = (TextView)findViewById(R.id.organizationTitle);
        txtOrganizationName = (TextView)findViewById(R.id.organizationName);
        txtMunicipalityName = (TextView)findViewById(R.id.municipalityName);
        htmlDescription = (WebView)findViewById(R.id.htmlDescription);
        btnExternalurl = (Button)findViewById(R.id.btnExternalurl);
        btnOrganization = (Button)findViewById(R.id.btnOrganization);
        btnMunicipality = (Button)findViewById(R.id.btnMunicipality);

        Intent intent = getIntent();
        m_UrlOID = intent.getStringExtra("oid");
        Log.d("TAGI", "m_UrlOID: " +m_UrlOID) ;
        contentBuilder.BuildContent("KEYlinkit", 0);
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
                    description = jsonObj.getString("description");
                    externalurl = jsonObj.getString("externalurl");
                    JSONObject organization = jsonObj.getJSONObject("organizations").getJSONObject("organization");
                    organizationOid = organization.getString("@oid");
                    organizationName = organization.getString("@title");
                    organizationContactUrl = organization.getString("@contacturl");
                    organizationUrl = organization.getString("@externalurl");

                    // get municipality data and check if it is array (multiple items) or object
                    String municipality = jsonObj.getJSONObject("municipalities").getString("municipality");
                    Object json = new JSONTokener(municipality).nextValue();
                    if(json instanceof JSONObject){
                        municipalityObject = jsonObj.getJSONObject("municipalities").getJSONObject("municipality");
                        municipalityOid = municipalityObject.getString("@oid");
                        municipalityTitle = municipalityObject.getString("@title");
                    } else {
                        municipalityArray = jsonObj.getJSONObject("municipalities").getJSONArray("municipality");
                    }

                    //municipalityOid = municipality.getString("@oid");
                    //municipalityTitle = municipality.getString("@title");


                }catch (final JSONException e){

                    // no error handling here but elsewhere

                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.d("TAGI", "organization: " + organizationOid + "+" +  organizationName);
            super.onPostExecute(result);
            if(title != null) {
                txtTitle.setText(title);
                htmlDescription.loadData(description, "text/html; charset=UTF-8", null);
                txtOrganizationName.setText(organizationName);

                btnExternalurl.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(externalurl));
                        startActivity(intent);
                    }
                });
                btnOrganization.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String[] urlSTRINGS = new String[3];
                        urlSTRINGS[0] = organizationName;
                        urlSTRINGS[1] = organizationContactUrl;
                        urlSTRINGS[2] = organizationUrl;
                        Intent intent1 = new Intent(LinkActivity.this, OrganizationActivity.class);
                        intent1.putExtra(MainActivity.EXTRA_MESSAGE, urlSTRINGS);
                        startActivity(intent1);
                    }
                });

                // if multiple mucipalities
                if(municipalityArray != null){
                    Log.d("TAGI", "municipality on array: " + municipalityArray);
                    txtMunicipalityName.setVisibility(View.GONE);
                    btnMunicipality.setVisibility(View.GONE);

                    try{
                        for(int i = 0; i < municipalityArray.length(); i++){
                            Log.d("TAGI","kohde nro "+i+": "+municipalityArray.getString(i));
                            municipalityObject =  municipalityArray.getJSONObject(i);
                            municipalityOid = municipalityObject.getString("@oid");
                            municipalityTitle = municipalityObject.getString("@title");
                            txtMunicipalityName.setText(municipalityTitle);
                            buttonArray.add(municipalityTitle);
                            buttonOidArray.add(Integer.parseInt(municipalityOid));

                        }
                    } catch(final JSONException e){}
                    arrayOfBatches = ButtonHandler.buildBatches(buttonArray, buttonOidArray);
                    ButtonAdapter adapter = new ButtonAdapter(LinkActivity.this, arrayOfBatches);
                    ListView listView = (ListView)findViewById(R.id.list);
                    listView.setAdapter(adapter);

                } else
                // if single municipality
                if(municipalityObject != null) {
                    Log.d("TAGI", "municipality on object: " + municipalityObject);
                    txtMunicipalityName.setText(municipalityTitle);
                    btnMunicipality.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            Intent intent1 = new Intent(LinkActivity.this, MunicipalityActivity.class);
                            intent1.putExtra("oid", municipalityOid);
                            startActivity(intent1);
                        }
                    });
                }  else {
                    findViewById(R.id.municipalityLayout).setVisibility(View.GONE);;
                }


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