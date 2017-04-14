package suomi.fi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ville on 27.3.2017.
 */

public class ContentBuilder {


   String url;
   String[] oidURL = new String[5];
   String  apiKEY = "/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
   String m_JsonTAG;

    public String  BuildContent(String contentKey)
    {

        switch (contentKey) {
            case "KEYpalveluluokat":

                url = "http://www.suomi.fi/rest/en/services/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/services/";
                m_JsonTAG = "serviceclass";
                break;

            case "KEYmaakunnat":

                url = "http://www.suomi.fi/rest/en/counties/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/counties/";
                m_JsonTAG = "county";
                break;

            case "KEYasiasanat":

                url = "http://www.suomi.fi/rest/en/keywords/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/keywords/";
                m_JsonTAG = "keyword";
                break;

            case "KEYorganisaatiot":

                url = "http://www.suomi.fi/rest/en/organizations/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/organizations/";
                m_JsonTAG = "organization";
                break;

            case "KEYlomakkeet":

                url = "http://www.suomi.fi/rest/en/forms/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/forms/";
                m_JsonTAG = "form";
                break;

            case "KEYlinkit":

                url = "http://www.suomi.fi/rest/en/links/?apikey=154830edfb48034c6f93c3df976df141f8928cc3&return=json";
                oidURL[0] = "http://www.suomi.fi/rest/en/links/";
                m_JsonTAG = "link";
                break;

        }

        return url;

    }
}
