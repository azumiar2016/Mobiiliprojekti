package suomi.fi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class FormActivity extends AppCompatActivity {

    public String title;
    public JSONObject details;
    String url;
    String m_ArticleString;
    FormDetail Details;
    TextView textTitle;
    TextView textDescription;
    Button btnCorporate;
    Button btnForm;
    ContentBuilder contentBuilder = new ContentBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        textTitle = (TextView) findViewById(R.id.textTitle);
        textDescription = (TextView) findViewById(R.id.textDescription);
        btnCorporate = (Button) findViewById(R.id.btnCorporate);
        btnForm = (Button) findViewById(R.id.btnForm);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        m_ArticleString = intent.getStringExtra("oid");

        contentBuilder.BuildContent("KEYlomakkeet", 0);
        url = contentBuilder.oidURL[0] + m_ArticleString + contentBuilder.apiKEY;

        Log.d("TAGI", "FormActivity started with url: " + url);

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
            if (!jsonStr.contains("No forms found")) {
                try {

                    title = jsonObj.getString("title");
                    details = new JSONObject(jsonObj.getString("details"));

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

                }
                Details = new FormDetail(details, "details");
                Log.d("TAGI", "Details: " + details);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("TAGI", "title: " + title);
            super.onPostExecute(result);
            if (title != null) {
                textTitle.setText(title);
                textDescription.setText(Details.description);

                btnCorporate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Details.cor_url));
                        startActivity(intent);
                    }
                });
                btnForm.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Details.for_url));
                        startActivity(intent);
                    }
                });
            } else {
                textTitle.setText("Form not found");
            }
        }


    }
}
