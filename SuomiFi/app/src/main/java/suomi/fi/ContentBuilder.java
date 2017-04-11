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

   String m_JsonTAG;

    public String  BuildContent(String contentKey, int County)
    {

        switch (contentKey) {
            /*case "KEYpalveluluokat":

                url = "http://www.suomi.fi/rest/en/services/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/services/";
                m_JsonTAG = "serviceclass";
                break;*/

            case "KEYmaakunnat":

                url = "http://www.suomi.fi/rest/"+Language+"/counties/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/counties/";
                m_JsonTAG = "county";
                break;

            case "KEYkunnat":

                url = "http://www.suomi.fi/rest/" + Language + "/counties/" + County + "/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/municipalities/";
                m_JsonTAG = "municipalities";
                break;

            case "KEYasiasanat":

                url = "http://www.suomi.fi/rest/"+ Language +"/keywords/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/keywords/";
                m_JsonTAG = "keyword";
                break;

            case "KEYorganisaatiot":

                url = "http://www.suomi.fi/rest/"+Language+"/organizations/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/organizations/";
                m_JsonTAG = "organization";
                break;

            case "KEYlomakkeet":
                url = "http://www.suomi.fi/rest/" + Language + "/forms/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/forms/";
                m_JsonTAG = "form";
                break;

            case "KEYlinkit":

                url = "http://www.suomi.fi/rest/"+Language+"/links/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/links/";
                m_JsonTAG = "link";
                break;

        }

        return url;

    }
}
