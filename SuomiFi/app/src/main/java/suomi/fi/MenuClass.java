package suomi.fi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import static suomi.fi.ButtonAdapter.key;

/**
 * Created by jarno on 3.5.2017.
 */

public class MenuClass {

    Activity m_activity;
    public static String ln;

    public MenuClass(Activity activity){
        m_activity = activity;
        if(ln == null)ln = Locale.getDefault().getLanguage();

    }

    public void MenuFlag(MenuItem lang, String ln ) {
        switch(ln){
            case "sv":
                lang.setIcon(m_activity.getResources().getDrawable(R.mipmap.ruotsi_item));
                break;
            case "en":
                lang.setIcon(m_activity.getResources().getDrawable(R.mipmap.englanti_item));
                break;
            default:
                lang.setIcon(m_activity.getResources().getDrawable(R.mipmap.suomi_item_2));
                break;
        }
    }

    // When menu item is selected
    public void SelectItem(int id) {
        switch (id) {
            case (R.id.Organizations):
                Intent intent1 = new Intent(m_activity, Main2Activity.class);
                intent1.putExtra(key, "KEYorganisaatiot");
                m_activity.startActivity(intent1);
                break;
            case (R.id.Municipalities):
                intent1 = new Intent(m_activity, Main2Activity.class);
                intent1.putExtra(key, "KEYmaakunnat");
                m_activity.startActivity(intent1);
                break;
            case (R.id.Forms):
                intent1 = new Intent(m_activity, Main2Activity.class);
                intent1.putExtra(key, "KEYlomakkeet");
                m_activity.startActivity(intent1);
                break;
            case (R.id.Links):
                intent1 = new Intent(m_activity, Main2Activity.class);
                intent1.putExtra(key, "KEYlinkit");
                m_activity.startActivity(intent1);
                break;
            case (R.id.MainMenu):
                intent1 = new Intent(m_activity, MainActivity.class);
                m_activity.startActivity(intent1);
                break;
            case (R.id.Settings):
                break;

            case (R.id.en_language):
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                m_activity.getBaseContext().getResources().updateConfiguration(config,
                        m_activity.getBaseContext().getResources().getDisplayMetrics());
                ln = "en";
                m_activity.recreate();
                break;

            case (R.id.fi_language):
                locale = new Locale("fi");
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                m_activity.getBaseContext().getResources().updateConfiguration(config,
                        m_activity.getBaseContext().getResources().getDisplayMetrics());
                ln = "fi";
                m_activity.recreate();
                break;

            case (R.id.sv_language):
                locale = new Locale("sv");
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                m_activity.getBaseContext().getResources().updateConfiguration(config,
                        m_activity.getBaseContext().getResources().getDisplayMetrics());
                ln = "sv";
                m_activity.recreate();
                break;
            case (R.id.About):
                intent1 = new Intent(m_activity, AboutActivity.class);
                m_activity.startActivity(intent1);
                break;
        }
    }
}
