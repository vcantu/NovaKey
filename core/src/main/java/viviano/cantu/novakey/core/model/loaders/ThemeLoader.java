/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.core.model.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import viviano.cantu.novakey.core.model.Settings;
import viviano.cantu.novakey.core.model.factories.ThemeFactory;
import viviano.cantu.novakey.core.view.themes.MasterTheme;

/**
 * Created by Viviano on 8/14/2016.
 */
public class ThemeLoader implements Loader<MasterTheme> {

    private final SharedPreferences mSharedPref;


    public ThemeLoader(Context context) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        ;
    }


    /**
     * Loads the element from where ever this interface saved it from
     *
     * @return a new loaded object
     */
    @Override
    public MasterTheme load() {
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
