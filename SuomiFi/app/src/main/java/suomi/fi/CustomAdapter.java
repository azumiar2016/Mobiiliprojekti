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

<<<<<<< HEAD
public class CustomAdapter extends ArrayAdapter<Batch> {
=======
public class CustomAdapter extends ArrayAdapter<Batch>
{
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87

    final static String key = "-";

    public CustomAdapter(Context context, ArrayList<Batch> batches)
    {
        super(context, 0, batches);
    }

<<<<<<< HEAD
=======
    /*
     * Main view for MainActivity
     */
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
<<<<<<< HEAD
        Batch batch = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Button button = (Button)convertView.findViewById(R.id.button);
        button.setText(batch.m_ButtonTag);
        button.setTag(position);
=======
        // initialize a button identifier for current position
        Batch batch = getItem(position);

        // If view doesn't exists, create a new view from xml layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //Empty button
        Button button = (Button)convertView.findViewById(R.id.button);

        // Set button text
        button.setText(batch.m_ButtonTag);
        button.setTag(position);

        // Event listener for the button
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87
        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                int position = (Integer)view.getTag();
                Batch batch = getItem(position);

<<<<<<< HEAD
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
=======
                // Set activity intent for selected button on Main2Activity
                switch (batch.m_ButtonTag)
                {
                    /*case "Palveluluokat":
                        Intent intent = new Intent(view.getContext(), Main2Activity.class);
                        intent.putExtra(key, "KEYpalveluluokat");
                        view.getContext().startActivity(intent);
                        break;*/

                    // List of counties
                    case "Maakunnat":
                        Intent intent1 = new Intent(view.getContext(), Main2Activity.class);
                        intent1.putExtra(key, "KEYmaakunnat");
                        view.getContext().startActivity(intent1);
                        break;

                    // List of minucipalities in the selected county
                    case "Kunnat":
                        Intent intent2 = new Intent(view.getContext(), Main2Activity.class);
                        intent2.putExtra(key, "KEYkunnat");
                        view.getContext().startActivity(intent2);
                        break;

                   /* case "Asiasanat":
                        Intent intent3 = new Intent(view.getContext(), Main2Activity.class);
                        intent3.putExtra(key, "KEYasiasanat");
                        view.getContext().startActivity(intent3);
                        break;*/

>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87

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
<<<<<<< HEAD


                }

            }

=======
                }
            }
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87
        });

        return convertView;
    }
<<<<<<< HEAD

=======
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87
}
