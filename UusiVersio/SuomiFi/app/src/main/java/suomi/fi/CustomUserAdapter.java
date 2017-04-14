package suomi.fi;

/**
 * Created by ville on 18.3.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomUserAdapter extends ArrayAdapter<Article> {
    public CustomUserAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    String m_IntentKey;
    String m_ArticleOID;

    public void passIntentKey(String key)
    {
        this.m_IntentKey = key;
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
                Intent intent = new Intent(view.getContext(), Main3Activity.class);
                Article article = getItem(position);
                m_ArticleOID = article.articleOId;
                String[] intentPacket = new String[] {m_IntentKey, m_ArticleOID};
                intent.putExtra(MainActivity.EXTRA_MESSAGE, intentPacket);
                view.getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}
