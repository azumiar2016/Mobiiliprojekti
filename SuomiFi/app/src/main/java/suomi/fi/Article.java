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

    public Article(JSONObject object) {
        try{
            this.articleName = object.getString("@title");
            this.articleLink = object.getString("$");
            this.articleOId  = object.getString("@oid");
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

    public static Article getJSONobject(JSONObject object) {
        Article article = new Article(object);
        return article;
    }

    public static String getArticle(String contentKEY, JSONObject jsonObjects) {
        String articleString = "null";
        try {
            articleString = jsonObjects.getString(contentKEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return articleString;
    }

}