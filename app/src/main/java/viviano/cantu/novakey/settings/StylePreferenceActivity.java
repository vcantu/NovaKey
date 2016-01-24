package viviano.cantu.novakey.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.settings.widgets.pickers.ColorPicker;
import viviano.cantu.novakey.settings.widgets.pickers.HorizontalPicker;
import viviano.cantu.novakey.settings.widgets.pickers.PickerItem;
import viviano.cantu.novakey.settings.widgets.ThemePicker;
import viviano.cantu.novakey.settings.widgets.ThemePreview;
import viviano.cantu.novakey.settings.widgets.pickers.ReleasePicker;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 1/5/2016.
 */
public class StylePreferenceActivity extends AbstractPreferenceActivity {

    private int theme, one, two, three;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        final ThemePreview preview = (ThemePreview)findViewById(R.id.preview);
        updatePreview(preview);
        preview.invalidate();

        ThemePicker themePicker = (ThemePicker)findViewById(R.id.themePicker);
        themePicker.set(theme);
        themePicker.invalidate();
        themePicker.setOnChangeListener(new ThemePicker.OnChangeListener() {
            @Override
            public void onChange(int newIndex) {
                if (newIndex == Theme.COUNT - 1 && theme > 0)
                    theme *= -1;
                else
                    theme = newIndex;
                updatePreview(preview);
            }
        });

        ReleasePicker releasePicker = (ReleasePicker)findViewById(R.id.releasePick);

        ColorPicker primaryColor = (ColorPicker)findViewById(R.id.primaryColor);
        primaryColor.setReleasePicker(releasePicker);
        primaryColor.setItem(Colors.path(one)[0]);
        primaryColor.setOnItemSelectedListener(new HorizontalPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(PickerItem item, int subIndex) {
                one = ((Colors)item).shade(subIndex);
                updatePreview(preview);
            }
        });

        ColorPicker secondaryColor = (ColorPicker)findViewById(R.id.secondaryColor);
        secondaryColor.setReleasePicker(releasePicker);
        secondaryColor.setItem(Colors.path(two)[0]);
        secondaryColor.setOnItemSelectedListener(new HorizontalPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(PickerItem item, int subIndex) {
                two = ((Colors)item).shade(subIndex);
                updatePreview(preview);
            }
        });

        ColorPicker ternaryPicker = (ColorPicker)findViewById(R.id.ternaryColor);
        ternaryPicker.setReleasePicker(releasePicker);
        ternaryPicker.setItem(Colors.path(three)[0]);
        ternaryPicker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(PickerItem item, int subIndex) {
                three = ((Colors)item).shade(subIndex);
                updatePreview(preview);
            }
        });
    }

    @Override
    int getLayoutId() {
        return R.layout.layout_style_preference;
    }

    private void updatePreview(ThemePreview preview) {
        boolean isAuto = false;
        int tNum = theme;
        if (theme < 0) {
            tNum *= -1;
            isAuto = true;
        }
        Theme t = Theme.getTheme(tNum);
        t.setColor(one, two, three);
        preview.set(t);
        if (isAuto) {
            //todo: add something that lets user know auto is selected
        }
        preview.invalidate();
    }

    @Override
    void onActivityClosed(boolean positiveResult) {
        if (positiveResult) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();

            boolean auto = false;
            if (theme < 0) {
                theme *= -1;
                auto = true;
            }
            editor.putString(Settings.pref_theme, theme + "," + one + "," + two + "," + three + (auto ? ",A" : ""));
            editor.commit();

            Toast t = Toast.makeText(this, "Style Saved.", Toast.LENGTH_SHORT);
            t.show();
        }
    }
    private void loadData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String str = sharedPref.getString(Settings.pref_theme, Settings.DEFAULT);
        String[] params;
        if (str.equals(Settings.DEFAULT)) {
            params = ("0,"+String.valueOf(0xFF616161)+","+String.valueOf(0xFFF5F5F5)+","+String.valueOf(0xFFF5F5F5)).split(",");
        }
        else
            params = str.split(",");
        theme = Integer.valueOf(params[0]);
        one = Integer.valueOf(params[1]);
        two =Integer.valueOf(params[2]);
        three = Integer.valueOf(params[3]);
        if (params.length >= 5) {
            if (params[4].equalsIgnoreCase("A"))
                theme *= -1;
        }
    }
}
