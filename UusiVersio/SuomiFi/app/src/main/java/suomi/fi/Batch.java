package suomi.fi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ville on 23.3.2017.
 */

public class Batch {



    String m_ButtonTag;


    public Batch(String buttonTag)
    {
    this.m_ButtonTag = buttonTag;
    }

    public static ArrayList<Batch> buildBatches(List<String> buttonContent)
    {
        ArrayList<Batch> batches = new ArrayList<>();

        for(int i = 0; i < buttonContent.size(); i++)
        {

            batches.add(new Batch(buttonContent.get(i)));

        }

    return batches;

    }


}
