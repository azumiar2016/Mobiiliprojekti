package suomi.fi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // List of items in MainActivity
    List<String> buttonArray = new ArrayList<String>(
            Arrays.asList("Maakunnat","Organisaatiot","Lomakkeet","Linkit")
    ); //Palveluluokat ja Asiasanat poistettu buttoneista

    // debug message tag
    public static final String EXTRA_MESSAGE = "suomi.fi.jsonlist.MESSAGE";


    // List of button identifiers (Batches)
    ArrayList<Batch> arrayOfBatches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Build a list for button identifiers
        arrayOfBatches = Batch.buildBatches(buttonArray);

        CustomAdapter adapter = new CustomAdapter(MainActivity.this, arrayOfBatches);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

    }

}