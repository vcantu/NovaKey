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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.Toast;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.core.utils.Colors;
import viviano.cantu.novakey.core.model.MainModel;
import viviano.cantu.novakey.core.model.Model;
import viviano.cantu.novakey.core.model.Settings;
import viviano.cantu.novakey.core.model.factories.ThemeFactory;
import viviano.cantu.novakey.core.model.loaders.ThemeLoader;
import viviano.cantu.novakey.widgets.NovaKeyPreview;
import viviano.cantu.novakey.widgets.pickers.ColorPicker;
import viviano.cantu.novakey.widgets.pickers.ReleasePicker;
import viviano.cantu.novakey.widgets.pickers.ThemePicker;
import viviano.cantu.novakey.core.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 1/5/2016.
 * <p>
 * TODO: once model can properly update the view(even when the theme updates)
 * remove any invalidate calls
 */
public class StylePreferenceActivity extends AbstractPreferenceActivity {

    private boolean mIsAuto = false;
    private NovaKeyPreview mPreview;
    private Model mModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ReleasePicker releasePicker = (ReleasePicker) findViewById(R.id.releasePick);

        mPreview = (NovaKeyPreview) findViewById(R.id.preview);
        mModel = new MainModel(this);
        mPreview.setModel(mModel);

        final CheckBox autoCheck = (CheckBox) findViewById(R.id.autoColor);
        autoCheck.setChecked(mIsAuto);
        autoCheck.setOnCheckedChangeListener((buttonView, isChecked) -> mIsAuto = isChecked);

        final CheckBox _3dCheck = (CheckBox) findViewById(R.id.threeDee);
        _3dCheck.setChecked(mModel.getTheme().is3D());
        _3dCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            System.out.println("3d triggered");
            mModel.getTheme().set3D(isChecked);
            mPreview.invalidate();
        });

        final ThemePicker themePicker = (ThemePicker) findViewById(R.id.themePicker);
        themePicker.setItem(ThemeFactory.getBoardNum(mModel.getTheme().getBoardTheme()));
        themePicker.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setBoardTheme((BoardTheme) item);
            mPreview.invalidate();
        });

        final ColorPicker primaryColor = (ColorPicker) findViewById(R.id.primaryColor);
        primaryColor.setReleasePicker(releasePicker);
        primaryColor.setItem(Colors.path(mModel.getTheme().getPrimaryColor())[0]);
        primaryColor.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setPrimaryColor(((Colors) item).shade(subIndex));
            mPreview.invalidate();
        });

        final ColorPicker secondaryColor = (ColorPicker) findViewById(R.id.secondaryColor);
        secondaryColor.setReleasePicker(releasePicker);
        secondaryColor.setItem(Colors.path(mModel.getTheme().getAccentColor())[0]);
        secondaryColor.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setAccentColor(((Colors) item).shade(subIndex));
            mPreview.invalidate();
        });

        final ColorPicker ternaryPicker = (ColorPicker) findViewById(R.id.ternaryColor);
        ternaryPicker.setReleasePicker(releasePicker);
        ternaryPicker.setItem(Colors.path(mModel.getTheme().getContrastColor())[0]);
        ternaryPicker.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setContrastColor(((Colors) item).shade(subIndex));
            mPreview.invalidate();
        });
    }


    @Override
    int getLayoutId() {
        return R.layout.style_preference_layout;
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

            editor.putBoolean(Settings.pref_auto_color, mIsAuto);
            editor.commit();

            new ThemeLoader(this).save(mModel.getTheme());

            Toast t = Toast.makeText(this, "Style Saved.", Toast.LENGTH_SHORT);
            t.show();
        }
    }
}