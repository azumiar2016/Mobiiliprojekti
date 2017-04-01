package suomi.fi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class Main3Activity extends AppCompatActivity {

    String m_UrlOID;
    String m_ArticleString;
    String m_IntentKEYType;
    String url;
    ContentBuilder contentBuilder = new ContentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        String[] intentArray = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        m_IntentKEYType = intentArray[0];
        m_UrlOID =  intentArray[1];

        contentBuilder.BuildContent(m_IntentKEYType);
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
                            break;

                        case "KEYlinkit":
                            m_ArticleString = Article.getArticle("externalurl",jsonObj);
                            break;

                        case "KEYlomakkeet":
                            m_ArticleString = Article.getArticle("url",jsonObj);
                            break;

                            //dismissingASIASANAT

                    }


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
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setText(m_ArticleString);

        }

    }
}
