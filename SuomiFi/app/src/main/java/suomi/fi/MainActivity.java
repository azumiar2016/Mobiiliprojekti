package suomi.fi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;



public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }
    public void Organisaatiotbutton(View v){
        Button b = (Button) findViewById(R.id.Organisaatiot);
        Intent intent= new Intent(this, kohde.class);
        startActivity(intent);

    }

    public void Kunnatbutton(View v){
        Button b = (Button) findViewById(R.id.Kunnat);
        Intent intent= new Intent(this, kohde.class);
        startActivity(intent);

    }

    public void Palvelutbutton(View v){
        Button b = (Button) findViewById(R.id.Palvelut);
        Intent intent= new Intent(this, kohde.class);
        startActivity(intent);

    }

}