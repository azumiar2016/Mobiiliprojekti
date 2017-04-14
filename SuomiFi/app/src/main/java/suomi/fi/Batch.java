package suomi.fi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ville on 23.3.2017.
 */

public class Batch {

<<<<<<< HEAD


=======
    //button identifier
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87
    String m_ButtonTag;


    public Batch(String buttonTag)
    {
<<<<<<< HEAD
    this.m_ButtonTag = buttonTag;
    }

=======
       this.m_ButtonTag = buttonTag;
    }

    /*
     * Create ArrayList for the button identifiers (Batches)
     * and add the given button identifiers to the list
     */
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87
    public static ArrayList<Batch> buildBatches(List<String> buttonContent)
    {
        ArrayList<Batch> batches = new ArrayList<>();

        for(int i = 0; i < buttonContent.size(); i++)
        {
<<<<<<< HEAD

            batches.add(new Batch(buttonContent.get(i)));

        }

    return batches;

    }


=======
            batches.add(new Batch(buttonContent.get(i)));
        }
        return batches;
    }
>>>>>>> 0c3db74bdd769648c6e1fa365b23a185fe2efc87
}
