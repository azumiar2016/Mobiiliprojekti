package suomi.fi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ville on 23.3.2017.
 */

public class ButtonHandler {

    //button identifier
    String m_ButtonTag;
    int m_ButtonOid;


    public ButtonHandler(String buttonTag, int buttonOid)
    {
        m_ButtonTag = buttonTag;
        m_ButtonOid = buttonOid;
    }

    /*
     * Create ArrayList for the button identifiers (Batches)
     * and add the given button identifiers to the list
     */
    public static ArrayList<ButtonHandler> buildBatches(List<String> buttonContent, List<Integer> buttonOid)
    {
        ArrayList<ButtonHandler> buttons = new ArrayList<>();

        for(int i = 0; i < buttonContent.size(); i++)
        {
            buttons.add(new ButtonHandler(buttonContent.get(i), buttonOid.get(i)));
        }
        return buttons;
    }
}
