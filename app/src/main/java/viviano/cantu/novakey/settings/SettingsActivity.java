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

package viviano.cantu.novakey.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.drawing.Font;
import viviano.cantu.novakey.view.drawing.Icons;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.setup.SetupActivity;
import viviano.cantu.novakey.view.themes.AppTheme;

public class SettingsActivity extends PreferenceActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(NovaKey.MY_PREFERENCES, MODE_PRIVATE);

        //LoadStuff
        Colors.initialize();
        AppTheme.load(this, getResources());
        Font.create(getApplicationContext());
        Icons.load(getApplicationContext());

        Settings.setPrefs(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Settings.update();

        if (!pref.getBoolean("has_setup", false)) {
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }
}

