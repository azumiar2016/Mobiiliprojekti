package suomi.fi;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johanna on 16.4.2017.
 */
public class LinkDetail {
    public String title;
    public String link;

    public LinkDetail(JSONObject details, String target) {
        SetDatamembers(details, target);
    }


    private void SetDatamembers(JSONObject links , String target)
    {
        try{
            switch(target) {
                case "links":
                    title = links.getString("title");
                    link = links.getString("externalurl");
                    break;
            }

        }catch (final JSONException e) {
            Log.e("TAGI", "Json parsing error: " + e.getMessage());
        }
    }
}