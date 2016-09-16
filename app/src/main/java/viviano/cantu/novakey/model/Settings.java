package viviano.cantu.novakey.model;

import android.content.SharedPreferences;

import viviano.cantu.novakey.model.loaders.ThemeFactory;
import viviano.cantu.novakey.view.themes.MasterTheme;

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
            pref_theme_legacy = "pref_theme",//(DEPRECATED)
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
            pref_auto_color = "pref_auto_color",
            pref_theme = "pref_master_theme";

    //Global Settings
    public static String DEFAULT = "DEFAULT";
    public static boolean hideLetters, hidePassword, vibrate, quickInsert, autoCorrect, quickClose;
    public static boolean hasSpaceBar;

    public static int startVersion, longPressTime, vibrateLevel;

    //Theme flag
    public static boolean autoColor;

    /**
     * Shared preferences
     */
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor edit;

    public static void setPrefs(SharedPreferences pref) {
        prefs = pref;
        edit = prefs.edit();
        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> update());
    }

    /**
     * Pulls all values from the shared preferences & updates the static fields
     */
    public static void update() {
        //Boolean Flag settings
        hideLetters = prefs.getBoolean(pref_hide_letters, false);
        hidePassword = prefs.getBoolean(pref_hide_password, false);
        vibrate = prefs.getBoolean(pref_vibrate, false);
        quickInsert = prefs.getBoolean(pref_quick_insert, false);

        autoCorrect = prefs.getBoolean(pref_auto_correct, false);
        quickClose = prefs.getBoolean(pref_quick_close, false);

        hasSpaceBar = prefs.getBoolean(pref_space_bar, false);

        autoColor = prefs.getBoolean(pref_auto_color, false);

        //Integer settings
        //this will only default to the given number if the person has never had this preference
        startVersion = prefs.getInt(pref_start_version, CURR_VERSION);
        if (startVersion == CURR_VERSION)
            edit.putInt(pref_start_version, startVersion);

        longPressTime = prefs.getInt(pref_long_press_time, 500);
        vibrateLevel = prefs.getInt(pref_vibrate_level, 50);

        fixLegacyPrefs();
        edit.commit();
    }

    private static void fixLegacyPrefs() {
        fixLegacyThemeing();
    }

    private static void fixLegacyThemeing() {
        String str = prefs.getString(pref_theme_legacy, DEFAULT);

        if (!str.equals(DEFAULT)) {
            //set new from old
            String newStr = prefs.getString(pref_theme, DEFAULT);
            if (newStr == DEFAULT) {//if new theme doesn't exist otherwise don't change
                MasterTheme theme = ThemeFactory.themeFromLegacyString(str);
                edit.putString(pref_theme, ThemeFactory.stringFromTheme(theme));
            }
            //set auto color
            autoColor = str.split(",")[4].equalsIgnoreCase("A");
            edit.putBoolean(pref_auto_color, autoColor);
            //delete old
            edit.remove(pref_theme_legacy);
        }
    }
}