package suomi.fi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


import static android.content.ContentValues.TAG;

public class Main3Activity extends AppCompatActivity {

    String m_UrlOID;
    String m_ArticleString;
    String m_IntentKEYType;
    String url;
    String m_Title;
    ContentBuilder contentBuilder = new ContentBuilder();


    String[] urlSTRINGS = new String[255];
    String[] buttonArray = new String[255];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        String[] intentArray = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        m_IntentKEYType = intentArray[0];
        m_UrlOID =  intentArray[1];
        m_Title = intentArray[1];

        contentBuilder.BuildContent(m_IntentKEYType,0);
        url = contentBuilder.oidURL[0] + m_UrlOID + contentBuilder.apiKEY;


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

            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null)
            {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    switch (m_IntentKEYType)
                    {
                        case "KEYpalveluluokat":
                            m_ArticleString = Article.getArticle("content",jsonObj);
                            break;

                        case "KEYmaakunnat":
                            m_ArticleString = Article.getArticle("title",jsonObj);
                            break;

                        case "KEYorganisaatiot":
                            m_ArticleString = Article.getArticle("contacturl",jsonObj);
                            m_ArticleString = Article.getArticle("contacturl",jsonObj);
                            urlSTRINGS[0] = Article.getArticle("title", jsonObj);
                            urlSTRINGS[1] = Article.getArticle("externalurl", jsonObj);
                            urlSTRINGS[2] = Article.getArticle("contacturl", jsonObj);

                            buttonArray[0] = "Kotisivut";
                            buttonArray[1] = "Yhteystiedot";

                            break;


                    }


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
            super.onPostExecute(result);

            Intent intent1 = null;

            switch (m_IntentKEYType) {
                case "KEYorganisaatiot":
                    intent1 = new Intent(Main3Activity.this, OrganizationActivity.class);
                    intent1.putExtra(MainActivity.EXTRA_MESSAGE, urlSTRINGS);
                    break;
                case "KEYlomakkeet":
                    intent1 = new Intent(Main3Activity.this, FormActivity.class);
                    intent1.putExtra("oid", m_UrlOID);
                    break;

                case "KEYlinkit":
                    intent1 = new Intent(Main3Activity.this, LinkActivity.class);
                    intent1.putExtra("oid", m_UrlOID);
                    break;

                case "KEYkunnat":
                    intent1 = new Intent(Main3Activity.this, MunicipalityActivity.class);
                    intent1.putExtra("oid", m_UrlOID);
                    break;
            }
           // intent1.putExtra(MainActivity.EXTRA_MESSAGE2, buttonArray);
            if(intent1 != null) {
                startActivity(intent1);
                finish();
            }



        }

    }
}