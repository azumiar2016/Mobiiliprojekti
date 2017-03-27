package suomi.fi;

/**
 * Created by ville on 18.3.2017.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Article {
    public String articleName;
    public String articleLink;
    public String articleOId;
    public String articleContent;
    public String articleLang;
    public String articleLevel;
    public String articleCurl;
    public String articleCorp;
    public String articleExurl;
    public String articleDesc;


    public Article(JSONObject object) {
        try{
            this.articleName = object.getString("@title");
            this.articleLink = object.getString("$");
            this.articleOId  = object.getString("@oid");
            this.articleCurl = object.getString("@corporateurl1");
            this.articleCorp = object.getString("@corporate1");
            this.articleExurl = object.getString("@externalurl");
            this.articleDesc = object.getString("@description");
            this.articleLang = object.getString("@lang");

            this.articleContent = object.getString("content");


        }catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> getArticles(JSONArray jsonArrays) {
        ArrayList<Article> articles = new ArrayList<Article>();
        for(int i = 0; i < jsonArrays.length(); i++)
        {

            try {
                articles.add(new Article(jsonArrays.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return articles;
    }

    public static String getArticle(JSONObject jsonObjects) {
            String articleString = "null";
            try {
                articleString = jsonObjects.getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }




        return articleString;
    }

}