package suomi.fi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by ville on 27.3.2017.
 */

public class ButtonAdapter extends ArrayAdapter<ButtonHandler>
{

    final static String key = "-";

    public ButtonAdapter(Context context, ArrayList<ButtonHandler> buttons)
    {
        super(context, 0, buttons);
    }

    /*
     * Main view for MainActivity
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // initialize a button identifier for current position
        final ButtonHandler buttonh = getItem(position);

        // If view doesn't exists, create a new view from xml layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //Empty button
        Button button = (Button)convertView.findViewById(R.id.button);

        // Set button text
        button.setText(buttonh.m_ButtonTag);
        button.setTag(buttonh.m_ButtonOid);

        // Event listener for the button
        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                String position = view.getTag().toString();
                Log.d("TAGI", "position: "+position);

                Intent intent1 = new Intent(view.getContext(), MunicipalityActivity.class);
                intent1.putExtra("oid", position);
                view.getContext().startActivity(intent1);
            }
        });

        return convertView;
    }
}
