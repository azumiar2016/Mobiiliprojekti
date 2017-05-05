package suomi.fi;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class OrganizationActivity extends AppCompatActivity {

    MenuClass mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        mymenu = new MenuClass(this);

        Button btnWebsite = (Button)findViewById(R.id.btnWebsite);
        Button btnGeneral = (Button)findViewById(R.id.btnGeneral);
        TextView title = (TextView)findViewById(R.id.textTitle);
        TextView textHomePage = (TextView)findViewById(R.id.textHomePage);
        TextView textInfo = (TextView)findViewById(R.id.textInfo);

        Intent intent = getIntent();


        final String[] organizationURLS  = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        title.setText(organizationURLS[0]);
        textHomePage.setText(organizationURLS[1]);
        textInfo.setText(organizationURLS[2]);

        btnWebsite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(organizationURLS[1]));
                startActivity(intent);
            }
        });

        btnGeneral.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(organizationURLS[2]));
                startActivity(intent);
            }
        });


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