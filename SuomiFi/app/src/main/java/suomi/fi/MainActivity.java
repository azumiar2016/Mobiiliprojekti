package suomi.fi;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.Locale;

import static suomi.fi.ButtonAdapter.key;

public class MainActivity extends AppCompatActivity {

    // debug message tag
    public static final String EXTRA_MESSAGE = "suomi.fi.jsonlist.MESSAGE";
    MenuClass mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mymenu = new MenuClass(this);

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

    protected void intentCounties(View view){
        Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
        intent1.putExtra(key, "KEYmaakunnat");
        view.getContext().startActivity(intent1);
    }

    protected void intentOrganization(View view){
        Intent intent4 = new Intent(MainActivity.this, Main2Activity.class);
        intent4.putExtra(key, "KEYorganisaatiot");
        view.getContext().startActivity(intent4);
    }

    protected void intentForms(View view){
        Intent intent5 = new Intent(MainActivity.this, Main2Activity.class);
        intent5.putExtra(key, "KEYlomakkeet");
        view.getContext().startActivity(intent5);
    }

    protected void intentLinks(View view){
        Intent intent6 = new Intent(MainActivity.this, Main2Activity.class);
        intent6.putExtra(key, "KEYlinkit");
        view.getContext().startActivity(intent6);
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