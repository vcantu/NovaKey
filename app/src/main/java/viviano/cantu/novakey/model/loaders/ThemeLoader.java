package viviano.cantu.novakey.model.loaders;

import android.content.SharedPreferences;

import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 8/14/2016.
 */
public class ThemeLoader implements Loader<MasterTheme> {

    private final SharedPreferences mSharedPref;

    public ThemeLoader(SharedPreferences sharedPreferences) {
        mSharedPref = sharedPreferences;
    }

    /**
     * Loads the element from where ever this interface saved it from
     *
     * @return a new loaded object
     */
    @Override
    public MasterTheme load() {
        System.out.println("loading...");
        System.out.println(mSharedPref.getString(
                Settings.pref_theme, Settings.DEFAULT));
        return ThemeFactory.themeFromString(mSharedPref.getString(
                Settings.pref_theme, Settings.DEFAULT));
    }

    /**
     * Saves the element to be loaded later
     *
     * @param theme object to be saved
     */
    @Override
    public void save(MasterTheme theme) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(Settings.pref_theme, ThemeFactory.stringFromTheme(theme));
        editor.commit();
    }
}
