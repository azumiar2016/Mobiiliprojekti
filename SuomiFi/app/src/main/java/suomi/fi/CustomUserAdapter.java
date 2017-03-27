package suomi.fi;

/**
 * Created by ville on 18.3.2017.
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class CustomUserAdapter extends ArrayAdapter<Article> {
    public CustomUserAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Article article = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        final TextView tvArticle = (TextView) convertView.findViewById(R.id.tvArticle);
        // Populate the data into the template view using the data object
        tvArticle.setText(article.articleName);


        Button btButton = (Button)convertView.findViewById(R.id.button);
        btButton.setTag(position);
        btButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                int position = (Integer)view.getTag();
                Intent intent = new Intent(view.getContext(), Main2Activity.class);
                Article article = getItem(position);
                intent.putExtra(kohde.EXTRA_MESSAGE, kohde.WEBSITE_URL+article.articleOId+kohde.WEBSITE_API);
                view.getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


    }

