package suomi.fi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Main2Activity extends AppCompatActivity {

    String m_Url;
    String m_ArticleString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        String message = intent.getStringExtra(kohde.EXTRA_MESSAGE);

        m_Url =  message;
        // Capture the layout's TextView and set the string as its text

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
            String jsonStr = sh.makeServiceCall(m_Url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null)
            {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    m_ArticleString = Article.getArticle(jsonObj);

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
            textView.setText(m_ArticleString);

        }

    }

}