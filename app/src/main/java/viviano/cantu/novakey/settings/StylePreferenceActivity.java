package viviano.cantu.novakey.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.settings.widgets.ColorPicker;
import viviano.cantu.novakey.settings.widgets.ShadePicker;
import viviano.cantu.novakey.settings.widgets.ThemePicker;
import viviano.cantu.novakey.settings.widgets.ThemePreview;
import viviano.cantu.novakey.settings.widgets.pickers.ReleasePicker;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 1/5/2016.
 */
public class StylePreferenceActivity extends PreferenceActivity {

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
                if (newIndex == Theme.COUNT-1 && theme > 0)
                    theme *= -1;
                else
                    theme = newIndex;
                updatePreview(preview);
            }
        });

        ShadePicker shadePicker = (ShadePicker)findViewById(R.id.shadePicker);

        ColorPicker primaryColor = (ColorPicker)findViewById(R.id.primaryColor);
        primaryColor.setShadePicker(shadePicker);
        primaryColor.setColor(one);
        primaryColor.setOnChangeListener(new ColorPicker.OnChangeListener() {
            @Override
            public void onChange(int newColor) {
                one = newColor;
                updatePreview(preview);
            }
        });
        ColorPicker secondaryColor = (ColorPicker)findViewById(R.id.secondaryColor);
        secondaryColor.setShadePicker(shadePicker);
        secondaryColor.setColor(two);
        secondaryColor.setOnChangeListener(new ColorPicker.OnChangeListener() {
            @Override
            public void onChange(int newColor) {
                two = newColor;
                updatePreview(preview);
            }
        });
        ColorPicker ternaryColor = (ColorPicker)findViewById(R.id.ternaryColor);
        ternaryColor.setShadePicker(shadePicker);
        ternaryColor.setColor(three);
        ternaryColor.setOnChangeListener(new ColorPicker.OnChangeListener() {
            @Override
            public void onChange(int newColor) {
                three = newColor;
                updatePreview(preview);
            }
        });

        viviano.cantu.novakey.settings.widgets.pickers.ColorPicker test
                = (viviano.cantu.novakey.settings.widgets.pickers.ColorPicker)findViewById(R.id.testPicker);
        ReleasePicker releasePicker = (ReleasePicker)findViewById(R.id.releasePick);

        test.setReleasePicker(releasePicker);

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
