package viviano.cantu.novakey.settings;

import android.content.SharedPreferences;

import java.util.ArrayList;

import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 6/22/2015.
 */
public class Settings {
    //KEYS
    public static String
            //NovaKey 0.1
            pref_hide_letters = "pref_hide_letters",
            pref_hide_password = "pref_hide_password",
            pref_vibrate = "pref_vibrate",
            pref_quick_insert = "pref_quick_insert",
            //pref_color = "pref_color",(REMOVED)
            //NovaKey 0.2
            pref_auto_correct = "pref_auto_correct",
            pref_quick_close = "pref_quick_close",
            //NovaKey 0.3
            pref_theme = "pref_theme",
            pref_btns = "pref_btns",
            pref_rate = "pref_rate",
            pref_tut = "pref_tut",
            pref_style = "pref_style",
            pref_space_bar = "pref_space_bar",
            pref_start_version = "pref_start_version";

    //Settings
    public static String DEFAULT = "DEFAULT";
    public static boolean hideLetters, hidePassword, vibrate, quickInsert, autoCorrect, quickClose;
    public static boolean hasSpaceBar;
    public static int startVersion;
    /*
        theme will be a String that can be translated into a Theme, with colors and other data
         it will have the following format:
         t = a number representing a theme id
         numbers represent the color so that 1 = primary color, 2 = secondary color and so on

         format:
         t,1,2,3
     */
    public static Theme theme;
    public static ArrayList<Btn> btns;

    public static void update() {
        hideLetters = sharedPref.getBoolean(pref_hide_letters, false);
        hidePassword = sharedPref.getBoolean(pref_hide_password, false);
        vibrate = sharedPref.getBoolean(pref_vibrate, false);
        quickInsert = sharedPref.getBoolean(pref_quick_insert, false);

        autoCorrect = sharedPref.getBoolean(pref_auto_correct, false);
        quickClose = sharedPref.getBoolean(pref_quick_close, false);

        hasSpaceBar = sharedPref.getBoolean(pref_space_bar, false);

        //this will only default to the given number if the person has never had this preference
        startVersion = sharedPref.getInt(pref_start_version, 9);

        theme = Theme.fromString(sharedPref.getString(pref_theme, Settings.DEFAULT));

        btns = Btn.btnsFromString(sharedPref.getString(pref_btns, Settings.DEFAULT));
    }

    public static SharedPreferences sharedPref;
    public static void setSharedPref(SharedPreferences pref) {
        sharedPref = pref;
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                update();
            }
        });
    }

}
