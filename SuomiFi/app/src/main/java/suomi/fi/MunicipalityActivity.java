package suomi.fi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

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
            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null)
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
                municDetails = new MunicipalityDetail(details, "details");
                municStatistics = new MunicipalityDetail(statistics, "statistics");
                Log.d("TAGI", "municDetails: " + details );
                Log.d("TAGI", "municStatistics: " + statistics );

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
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+municDetails.address));
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
        }

    }
}
