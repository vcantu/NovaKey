package viviano.cantu.novakey.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.setup.TutorialActivity;

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
                         Uri.parse("https://plus.google.com/communities/106258064775616773864")));
                 return true;
             }
         });
//         Preference testPref = findPreference("pref_test");
//         testPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//             @Override
//             public boolean onPreferenceClick(Preference preference) {
//                 startActivity(new Intent(getActivity().getApplicationContext(), EmojiSettingActivity.class));
//                 return true;
//             }
//         });
	}
}
