package viviano.cantu.novakey.settings;

import android.content.SharedPreferences;

import java.util.ArrayList;

import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.themes.BaseTheme;
import viviano.cantu.novakey.themes.Theme;
import viviano.cantu.novakey.themes.ThemeBuilder;

/**
 * Created by Viviano on 6/22/2015.
 */
public class Settings {
    private static final int CURR_VERSION = 21;

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
            pref_start_version = "pref_start_version",
            //NovaKey 0.3.4
            pref_beta_test = "pref_beta_test",
            //NovaKey 0.3.5
            pref_long_press_time = "pref_long_press_time";

    //Settings
    public static String DEFAULT = "DEFAULT";
    public static boolean hideLetters, hidePassword, vibrate, quickInsert, autoCorrect, quickClose;
    public static boolean hasSpaceBar;

    public static int startVersion = CURR_VERSION, longPressTime = 500;
    public static float smallRadius;

    //Theme settings
    public static Theme theme;
    public static boolean autoColor;
    public static ArrayList<Btn> btns;

    /**
     * Pulls all values from the shared preferences & updates teh static fields
     */
    public static void update() {
        //Boolean settings
        hideLetters = sharedPref.getBoolean(pref_hide_letters, false);
        hidePassword = sharedPref.getBoolean(pref_hide_password, false);
        vibrate = sharedPref.getBoolean(pref_vibrate, false);
        quickInsert = sharedPref.getBoolean(pref_quick_insert, false);

        autoCorrect = sharedPref.getBoolean(pref_auto_correct, false);
        quickClose = sharedPref.getBoolean(pref_quick_close, false);

        hasSpaceBar = sharedPref.getBoolean(pref_space_bar, false);

        //Ints
        //this will only default to the given number if the person has never had this preference
        startVersion = sharedPref.getInt(pref_start_version, CURR_VERSION);
        longPressTime = sharedPref.getInt(pref_long_press_time, 500);

        theme = themeFromString(sharedPref.getString(pref_theme, Settings.DEFAULT));
        btns = Btn.btnsFromString(sharedPref.getString(pref_btns, Settings.DEFAULT));
        commit();
    }

    //TODO: Note if anyone's version is > 20 show them the message about the new auto feature
    /**
     * Will save the settings that aren't saved in other classes
     */
    public static void commit() {
        SharedPreferences.Editor edit = sharedPref.edit();
        if (startVersion == CURR_VERSION)
            edit.putInt(pref_start_version, startVersion);
        edit.commit();
    }

    /**
     * Builds a theme from the sharedPref string
     *
     * theme will be a String that can be translated into a Theme, with colors and other data
     * it will have the following format:
     *
     * t = a number representing a theme id
     *
     * numbers represent the color so that:
     * c1 = primaryColor, c2 = accentColor, c3 = contrastColor
     *
     * 'A' or 'X' if theme has auto color enabled
     *
     * '3d' or 'X' if the theme has 3d enabled
     *
     * format:
     *     t,1,2,3,A,3d
     *
     * @param s string to build theme from
     * @return theme built from a string using the above format
     */
    private static Theme themeFromString(String s) {
        if (s.equals(DEFAULT)) {
            BaseTheme res = new BaseTheme();
            res.setColors(0xFF616161, 0xFFF5F5F5, 0xFFF5F5F5);
            return res;
        }
        String[] params = s.split(",");
        Theme res = ThemeBuilder.getTheme(Integer.valueOf(params[0]));
        res.setColors(Integer.valueOf(params[1]),
                Integer.valueOf(params[2]),
                Integer.valueOf(params[3]));
        if (params.length >= 5)
            autoColor = params[4].equalsIgnoreCase("A");
        if (params.length >= 6)
            res.set3D(params[5].equalsIgnoreCase("3d"));

        return res;
    }


    /**
     * Shared preferences
     */
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