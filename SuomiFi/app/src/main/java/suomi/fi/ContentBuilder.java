package suomi.fi;

import android.app.Service;
import android.content.ContentValues;
import android.util.Log;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by ville on 27.3.2017.
 *
 * ContentBuilder identifies the REST API query
 */

public class ContentBuilder {

    // REST API resource location
   String url;

   String[] oidURL = new String[5];
    String Language = Locale.getDefault().getLanguage();

    // Suomi.fi API key
   String  apiKEY = "/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";

    String apiURI = "http://www.suomi.fi/rest/"+Language+"/";

   String m_JsonTAG;

    public String  BuildContent(String contentKey, int County)
    {

        switch (contentKey) {

            case "KEYmaakunnat":

                url = apiURI + "counties" +apiKEY;
                oidURL[0] = "http://www.suomi.fi/rest/en/counties/";
                m_JsonTAG = "county";
                break;

            case "KEYkunnat":

                url = apiURI + "counties/" + County + apiKEY;
                oidURL[0] = apiURI + "municipalities/";
                m_JsonTAG = "municipalities";
                break;

            case "KEYorganisaatiot":

                url = apiURI + "organizations" +apiKEY;
                oidURL[0] = apiURI + "organizations/";
                m_JsonTAG = "organization";
                break;

            case "KEYlomakkeet":
                url = apiURI + "forms" +apiKEY;
                oidURL[0] = apiURI + "forms/";
                m_JsonTAG = "form";
                break;

            case "KEYlinkit":

                url = apiURI + "links" +apiKEY;
                oidURL[0] = apiURI + "links/";
                m_JsonTAG = "link";
                break;

        }

        return url;

    }
}
