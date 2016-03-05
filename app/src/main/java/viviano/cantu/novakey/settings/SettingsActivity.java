package viviano.cantu.novakey.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import viviano.cantu.novakey.Font;
import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.setup.SetupActivity;
import viviano.cantu.novakey.themes.AppTheme;

public class SettingsActivity extends PreferenceActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(NovaKey.MY_PREFERENCES, MODE_PRIVATE);

        //LoadStuff
        KeyLayout.CreateKeyboards(getResources());
        Colors.initialize();
        AppTheme.load(this, getResources());
        Font.create(getApplicationContext());
        Icon.load(getApplicationContext());

        Settings.setSharedPref(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Settings.update();

        if (!pref.getBoolean("has_setup", false)) {
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }
}

