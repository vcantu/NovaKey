package viviano.cantu.novakey.model;

import android.content.SharedPreferences;

import viviano.cantu.novakey.elements.buttons.ButtonFactory;
import viviano.cantu.novakey.view.themes.board.BaseTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.view.themes.ThemeFactory;

/**
 * Created by Viviano on 6/22/2015.
 */
public class Settings {
    private static final int CURR_VERSION = 24;

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
            pref_theme = "pref_theme",//(DEPRECATED)
            //pref_btns = "pref_btns",//(REMOVED)
            pref_rate = "pref_rate",//INTENT
            pref_tut = "pref_tut",//INTENT
            pref_style = "pref_style",//INTENT
            pref_space_bar = "pref_space_bar",//(DEPRECATED)
            pref_start_version = "pref_start_version",
            //NovaKey 0.3.4
            pref_beta_test = "pref_beta_test",//INTENT
            //NovaKey 0.3.5
            pref_long_press_time = "pref_long_press_time",
            //Novakey 0.3.7
            pref_vibrate_level = "pref_vibrate_level",
            //NovaKey 1.0
            pref_auto_color = "pref_auto_color";

    //Global Settings
    public static String DEFAULT = "DEFAULT";
    public static boolean hideLetters, hidePassword, vibrate, quickInsert, autoCorrect, quickClose;
    public static boolean hasSpaceBar;

    public static int startVersion, longPressTime, vibrateLevel;

    //Theme flag
    public static boolean autoColor;

    /**
     * Pulls all values from the shared preferences & updates teh static fields
     */
    public static void update() {
        //Boolean Flag settings
        hideLetters = sharedPref.getBoolean(pref_hide_letters, false);
        hidePassword = sharedPref.getBoolean(pref_hide_password, false);
        vibrate = sharedPref.getBoolean(pref_vibrate, false);
        quickInsert = sharedPref.getBoolean(pref_quick_insert, false);

        autoCorrect = sharedPref.getBoolean(pref_auto_correct, false);
        quickClose = sharedPref.getBoolean(pref_quick_close, false);

        hasSpaceBar = sharedPref.getBoolean(pref_space_bar, false);

        autoColor = sharedPref.getBoolean(pref_auto_color, false);

        //Ints
        //this will only default to the given number if the person has never had this preference
        startVersion = sharedPref.getInt(pref_start_version, CURR_VERSION);

        longPressTime = sharedPref.getInt(pref_long_press_time, 500);
        vibrateLevel = sharedPref.getInt(pref_vibrate_level, 50);

        commit();
    }

    /**
     * Will save the settings that aren't saved in other classes
     */
    public static void commit() {
        SharedPreferences.Editor edit = sharedPref.edit();
        if (startVersion == CURR_VERSION)
            edit.putInt(pref_start_version, startVersion);
        edit.commit();
    }

//    /**
//     * Builds a theme from the sharedPref string
//     *
//     * theme will be a String that can be translated into a BoardTheme, with colors and other data
//     * it will have the following format:
//     *
//     * t = the number theme id
//     *
//     * numbers represent the color so that:
//     * c1 = primaryColor, c2 = accentColor, c3 = contrastColor
//     *
//     * 'A' or 'X' if theme has auto color enabled
//     *
//     * '3d' or 'X' if the theme has 3d enabled
//     *
//     * format:
//     *     t,c1,c2,c3,A,3d
//     *
//     * @param s string to build theme from
//     * @return theme built from a string using the above format
//     */
//    private static BoardTheme themeFromString(String s) {
//        if (s.equals(DEFAULT)) {
//            BaseTheme res = new BaseTheme();
//            res.setColors(0xFF616161, 0xFFF5F5F5, 0xFFF5F5F5);
//            return res;
//        }
//        String[] params = s.split(",");
//        BoardTheme res = ThemeFactory.createTheme(Integer.valueOf(params[0]));
//        res.setColors(Integer.valueOf(params[1]),
//                Integer.valueOf(params[2]),
//                Integer.valueOf(params[3]));
//        if (params.length >= 5)
//            autoColor = params[4].equalsIgnoreCase("A");
//        if (params.length >= 6)
//            res.set3D(params[5].equalsIgnoreCase("3d"));
//
//        return res;
//    }


    /**
     * Shared preferences
     */
    public static SharedPreferences sharedPref;
    public static void setSharedPref(SharedPreferences pref) {
        sharedPref = pref;
        sharedPref.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> update());
    }
}