package suomi.fi;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johanna on 16.4.2017.
 */
public class FormDetail {
    public String corporate;
    public String description;
    public String form;
    public String cor_url;
    public String for_url;

    public FormDetail(JSONObject details, String target) {
        SetDatamembers(details, target);
    }

    private void SetDatamembers(JSONObject details, String target) {
        try {
            switch (target) {
                case "details":
                    description = details.getString("description");

                    JSONObject Corporate = details.getJSONObject("corporate");
                    corporate = Corporate.getString("corporate");
                    cor_url = Corporate.getString("corporateurl");

                    JSONObject Form = details.getJSONObject("form");
                    form = Form.getString("form");
                    for_url = Form.getString("url");
                    break;
            }

        } catch (final JSONException e) {
            Log.e("TAGI", "Json parsing error: " + e.getMessage());
        }
    }
}
