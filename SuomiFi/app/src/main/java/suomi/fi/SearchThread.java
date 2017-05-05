package suomi.fi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static suomi.fi.Main2Activity.articleArrays;
import static suomi.fi.Main2Activity.arrayOfArticles;

/**
 * Created by jarno on 4.5.2017.
 */

public class SearchThread extends Thread{

    String newText;
    Activity m_activity;
    CustomUserAdapter adapter;
    public String intentLock;
    public SearchThread(String text, Activity activity){
        newText = text;
        m_activity = activity;
    }

    public void run(){

        if(newText.length() > 1) {

            //katkaise hakumerkkijono valilyöntien kohdalta
            // ja muodosta taulukko

            String searchArray[] = newText.split("\\s");



            //reset array list


            arrayOfArticles = new ArrayList<Article>();

            //loop all articles
            for(int i = 0; i < articleArrays.length(); i++) {
                try {

                    //get single article object
                    JSONObject json_data = articleArrays.getJSONObject(i);

                    //get article title
                    //note: change this string to lower case to avoid case sensitive issue
                    String title = json_data.getString("@title").toLowerCase();

                    boolean trigger = true;

                    //if title contains the search string...
                    //note: change this string to lower case too
                    for(int i2 = 0; i2 < searchArray.length; i2++) {
                        if (title.contains(searchArray[i2].toLowerCase()) && trigger == true) {
                            trigger = true;

                        } else {
                            trigger = false;
                        }
                    }
                    if(trigger == true){
                        arrayOfArticles.add(Article.getJSONobject(json_data));
                    }

                }catch (final JSONException e){
                    Log.e("DEBUG", "Json parsing error: " + e.getMessage());
                }
            }


        } 
    }
}
