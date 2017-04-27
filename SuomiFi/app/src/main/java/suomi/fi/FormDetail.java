package suomi.fi;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johanna on 16.4.2017.
 */
public class FormDetail {
    public String title;
    public String corporate;
    public String description;
    public String form;

    public FormDetail(JSONObject details, String target) {
        SetDatamembers(details, target);
    }


    private void SetDatamembers(JSONObject forms , String target)
    {
        try{
            switch(target) {
                case "forms":
                    title = forms.getString("title");
                    corporate = forms.getString("corporate");
                    description = forms.getString("description");
                    form = forms.getString("url");
                    break;
            }

        }catch (final JSONException e) {
            Log.e("TAGI", "Json parsing error: " + e.getMessage());
        }
    }
}

