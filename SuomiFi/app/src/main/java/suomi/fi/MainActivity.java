package suomi.fi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static suomi.fi.CustomAdapter.key;

public class MainActivity extends AppCompatActivity {

    // List of items in MainActivity
   List<String> buttonArray;

    // debug message tag
    public static final String EXTRA_MESSAGE = "suomi.fi.jsonlist.MESSAGE";


    // List of button identifiers (Batches)
    ArrayList<Batch> arrayOfBatches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jarno);

        buttonArray = new ArrayList<String>(
             Arrays.asList(
                     getResources().getString(R.string.btn_Counties_and_Municipaties),
                     getResources().getString(R.string.btn_Organizations),
                     getResources().getString(R.string.btn_Links),
                     getResources().getString(R.string.btn_Forms)
             )
        ); //Palveluluokat ja Asiasanat poistettu buttoneista

        //Build a list for button identifiers
        arrayOfBatches = Batch.buildBatches(buttonArray);

        CustomAdapter adapter = new CustomAdapter(MainActivity.this, arrayOfBatches);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem lang = menu.findItem(R.id.Settings);
        menu.findItem(R.id.menuSearch).setVisible(false);


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