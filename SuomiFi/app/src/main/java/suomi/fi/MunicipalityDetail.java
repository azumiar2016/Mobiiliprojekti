package suomi.fi;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by jarno on 9.4.2017.
 */

public class MunicipalityDetail
{
    public String address;
    public String email;
    public String fax;
    public String phone;
    public String website;
    public String gen_description;
    public String gen_url;
    public String fin_description;
    public String fin_url;
    public MunicipalityDetail(JSONObject details, String target)
    {
        SetDatamembers(details, target);
    }

    private void SetDatamembers(JSONObject details , String target)
    {
        try{
            switch(target) {
                case "details":
                    address = details.getString("address");
                    email = details.getString("email");
                    fax = details.getString("fax");
                    phone = details.getString("phone");
                    website = details.getString("website");
                    break;
                case "statistics":
                    JSONObject general = details.getJSONObject("general");
                    gen_description = general.getString("description");
                    gen_url = general.getString("url");
                    JSONObject financial = details.getJSONObject("financial");
                    fin_description = financial.getString("description");
                    fin_url = financial.getString("url");
                    break;
            }

        }catch (final JSONException e) {
            Log.e("TAGI", "Json parsing error: " + e.getMessage());
        }
    }

}
