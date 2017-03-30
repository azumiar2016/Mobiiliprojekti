package suomi.fi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by ville on 27.3.2017.
 */

public class CustomAdapter extends ArrayAdapter<Batch> {

    final static String key = "-";

    public CustomAdapter(Context context, ArrayList<Batch> batches)
    {
        super(context, 0, batches);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Batch batch = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Button button = (Button)convertView.findViewById(R.id.button);
        button.setText(batch.m_ButtonTag);
        button.setTag(position);
        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                int position = (Integer)view.getTag();
                Batch batch = getItem(position);

                switch (batch.m_ButtonTag)
                {
                    case "Palveluluokat":
                        Intent intent = new Intent(view.getContext(), Main2Activity.class);
                        intent.putExtra(key, "KEYpalveluluokat");
                        view.getContext().startActivity(intent);
                        break;

                    case "Maakunnat":
                        Intent intent2 = new Intent(view.getContext(), Main2Activity.class);
                        intent2.putExtra(key, "KEYmaakunnat");
                        view.getContext().startActivity(intent2);
                        break;

                    case "Asiasanat":
                        Intent intent3 = new Intent(view.getContext(), Main2Activity.class);
                        intent3.putExtra(key, "KEYasiasanat");
                        view.getContext().startActivity(intent3);
                        break;

                    case "Organisaatiot":
                        Intent intent4 = new Intent(view.getContext(), Main2Activity.class);
                        intent4.putExtra(key, "KEYorganisaatiot");
                        view.getContext().startActivity(intent4);
                        break;

                    case "Lomakkeet":
                        Intent intent5 = new Intent(view.getContext(), Main2Activity.class);
                        intent5.putExtra(key, "KEYlomakkeet");
                        view.getContext().startActivity(intent5);
                        break;

                    case "Linkit":
                        Intent intent6 = new Intent(view.getContext(), Main2Activity.class);
                        intent6.putExtra(key, "KEYlinkit");
                        view.getContext().startActivity(intent6);
                        break;


                }

            }

        });

        return convertView;
    }

}
