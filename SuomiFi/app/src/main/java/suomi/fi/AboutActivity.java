package suomi.fi;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity {
    TextView txtTitle, authorsTitle, authorsText, copyrightTitle, copyrightText;
    Button back;
    MenuClass mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txtTitle = (TextView)findViewById(R.id.aboutTitle);
        authorsTitle = (TextView)findViewById(R.id.authorsTitle);
        authorsText = (TextView)findViewById(R.id.authorsText);
        copyrightTitle = (TextView)findViewById(R.id.copyrightText);
        copyrightText = (TextView)findViewById(R.id.copyrightText);
        back = (Button)findViewById(R.id.btnBack);
        mymenu = new MenuClass(this);
    }

    protected void goBack(View v){
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
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
