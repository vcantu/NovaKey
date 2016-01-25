package viviano.cantu.novakey.setup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.settings.Settings;

public class SetupActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar

		SharedPreferences pref = getApplicationContext().getSharedPreferences(NovaKey.MY_PREFERENCES, MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean("has_setup", false);
		editor.commit();
		
		setContentView(new SetupView(this));
	}
}
