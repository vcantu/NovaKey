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
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import viviano.cantu.novakey.EmojiSettingActivity;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.core.model.Settings;
import viviano.cantu.novakey.tutorial.TutorialActivity;

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Preference stylePref = findPreference(Settings.pref_style);
        stylePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity().getApplicationContext(), StylePreferenceActivity.class));
                return true;
            }
        });

        Preference ratePref = findPreference(Settings.pref_rate);
        ratePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=viviano.cantu.novakey"));
                startActivity(intent);
                return true;
            }
        });

        Preference tutPref = findPreference(Settings.pref_tut);
        tutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity().getApplicationContext(),
                        TutorialActivity.class));
                return true;
            }
        });

        Preference betaTestPref = findPreference(Settings.pref_beta_test);
        betaTestPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/apps/testing/viviano.cantu.novakey")));
                return true;
            }
        });

        Preference subredditPref = findPreference(Settings.pref_subreddit);
        subredditPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.reddit.com/r/NovaKey/")));
                return true;
            }
        });

        try {
            Preference testPref = findPreference("pref_test");
            testPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity().getApplicationContext(), EmojiSettingActivity.class));
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }
}
