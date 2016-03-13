package viviano.cantu.novakey.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.settings.widgets.pickers.ColorPicker;
import viviano.cantu.novakey.settings.widgets.pickers.HorizontalPicker;
import viviano.cantu.novakey.settings.widgets.pickers.PickerItem;
import viviano.cantu.novakey.settings.widgets.ThemePreview;
import viviano.cantu.novakey.settings.widgets.pickers.ReleasePicker;
import viviano.cantu.novakey.settings.widgets.pickers.ThemePicker;
import viviano.cantu.novakey.themes.Theme;
import viviano.cantu.novakey.themes.ThemeBuilder;

/**
 * Created by Viviano on 1/5/2016.
 */
public class StylePreferenceActivity extends AbstractPreferenceActivity {

    private int mTheme, mColor1, mColor2, mColor3;
    private boolean mIsAuto = false, mIs3d = false;
    private ThemePreview mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();

        final ReleasePicker releasePicker = (ReleasePicker)findViewById(R.id.releasePick);

        mPreview = (ThemePreview)findViewById(R.id.preview);
        updatePreview();
        mPreview.invalidate();

        final CheckBox autoCheck = (CheckBox)findViewById(R.id.autoColor);
        autoCheck.setChecked(mIsAuto);
        autoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsAuto = isChecked;
            }
        });

        final CheckBox _3dCheck = (CheckBox)findViewById(R.id.threeDee);
        _3dCheck.setChecked(mIs3d);
        _3dCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIs3d = isChecked;
                updatePreview();
            }
        });

        final ThemePicker themePicker = (ThemePicker)findViewById(R.id.themePicker);
        themePicker.setItem(mTheme);
        themePicker.invalidate();
        themePicker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(PickerItem item, int subIndex) {
                int index = ((Theme)item).themeID();
                mTheme = index;
                updatePreview();
            }
        });

        final ColorPicker primaryColor = (ColorPicker)findViewById(R.id.primaryColor);
        primaryColor.setReleasePicker(releasePicker);
        primaryColor.setItem(Colors.path(mColor1)[0]);
        primaryColor.setOnItemSelectedListener(new HorizontalPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(PickerItem item, int subIndex) {
                mColor1 = ((Colors)item).shade(subIndex);
                updatePreview();
            }
        });

        final ColorPicker secondaryColor = (ColorPicker)findViewById(R.id.secondaryColor);
        secondaryColor.setReleasePicker(releasePicker);
        secondaryColor.setItem(Colors.path(mColor2)[0]);
        secondaryColor.setOnItemSelectedListener(new HorizontalPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(PickerItem item, int subIndex) {
                mColor2 = ((Colors)item).shade(subIndex);
                updatePreview();
            }
        });

        final ColorPicker ternaryPicker = (ColorPicker)findViewById(R.id.ternaryColor);
        ternaryPicker.setReleasePicker(releasePicker);
        ternaryPicker.setItem(Colors.path(mColor3)[0]);
        ternaryPicker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(PickerItem item, int subIndex) {
                mColor3 = ((Colors)item).shade(subIndex);
                updatePreview();
            }
        });
    }

    @Override
    int getLayoutId() {
        return R.layout.layout_style_preference;
    }

    /**
     * Will update the theme preview
     */
    private void updatePreview() {
        Theme t = ThemeBuilder.getTheme(mTheme);
        t.setColors(mColor1, mColor2, mColor3);
        t.set3D(mIs3d);
        mPreview.set(t);
        if (mIsAuto) {
            //todo: add something that lets user know auto is selected
        }
        mPreview.invalidate();
    }

    /**
     * Will save the preference pref_theme, as a String so that the keyboard can
     * generate a theme from it if positiveResult is true
     *
     * @param positiveResult whether to save it or not
     */
    @Override
    void onActivityClosed(boolean positiveResult) {
        if (positiveResult) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(Settings.pref_theme, mTheme +
                    "," + mColor1 + "," + mColor2 + "," + mColor3 +
                    "," + (mIsAuto ? "A" : "X") + "," + (mIs3d ? "3d" : "X"));
            editor.commit();

            Toast t = Toast.makeText(this, "Style Saved.", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    /**
     * Will load the necessary data from the preference: pref_theme, and use that
     * data to display the activity.
     *
     * Will also set the default theme if it has not previously been entered
     */
    private void loadData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String str = sharedPref.getString(Settings.pref_theme, Settings.DEFAULT);

        if (str.equals(Settings.DEFAULT)) {
             str = "0," +
                    String.valueOf(0xFF616161) + "," +
                    String.valueOf(0xFFF5F5F5) + "," +
                    String.valueOf(0xFFF5F5F5) + "," +
                    "X," + "X";//no auto & no 3d by default
        }
        String[] params = str.split(",");

        mTheme = Integer.valueOf(params[0]);
        mColor1 = Integer.valueOf(params[1]);
        mColor2 =Integer.valueOf(params[2]);
        mColor3 = Integer.valueOf(params[3]);
        if (params.length >= 5) {
            if (params[4].equalsIgnoreCase("A"))
                mIsAuto = true;
        }
        if (params.length >= 6) {
            if (params[5].equalsIgnoreCase("3d"))
                mIs3d = true;
        }
    }
}