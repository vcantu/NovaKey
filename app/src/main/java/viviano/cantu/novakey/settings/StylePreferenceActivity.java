package viviano.cantu.novakey.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.Toast;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.model.MainModel;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.model.TrueModel;
import viviano.cantu.novakey.model.loaders.ThemeFactory;
import viviano.cantu.novakey.settings.widgets.pickers.ColorPicker;
import viviano.cantu.novakey.settings.widgets.pickers.HorizontalPicker;
import viviano.cantu.novakey.settings.widgets.pickers.PickerItem;
import viviano.cantu.novakey.settings.widgets.pickers.ReleasePicker;
import viviano.cantu.novakey.settings.widgets.pickers.ThemePicker;
import viviano.cantu.novakey.view.MainView;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 1/5/2016.
 *
 * TODO: once model can properly update the view(even when the theme updates)
 * remove any invalidate calls
 */
public class StylePreferenceActivity extends AbstractPreferenceActivity {

    private boolean mIsAuto = false;
    private MainView mPreview;
    private Model mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ReleasePicker releasePicker = (ReleasePicker)findViewById(R.id.releasePick);

        mPreview = (MainView) findViewById(R.id.preview);
        initializeModel();
        mPreview.setModel(mModel);

        final CheckBox autoCheck = (CheckBox)findViewById(R.id.autoColor);
        autoCheck.setChecked(mIsAuto);
        autoCheck.setOnCheckedChangeListener((buttonView, isChecked) -> mIsAuto = isChecked);

        final CheckBox _3dCheck = (CheckBox)findViewById(R.id.threeDee);
        _3dCheck.setChecked(mModel.getTheme().is3D());
        _3dCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mModel.getTheme().set3D(isChecked);
            mPreview.invalidate();
        });

        final ThemePicker themePicker = (ThemePicker)findViewById(R.id.themePicker);
        themePicker.setItem(ThemeFactory.getBoardNum(mModel.getTheme().getBoardTheme()));
        themePicker.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setBoardTheme((BoardTheme)item);
            mPreview.invalidate();
        });

        final ColorPicker primaryColor = (ColorPicker)findViewById(R.id.primaryColor);
        primaryColor.setReleasePicker(releasePicker);
        primaryColor.setItem(Colors.path(mModel.getTheme().getPrimaryColor())[0]);
        primaryColor.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setPrimaryColor(((Colors)item).shade(subIndex));
            mPreview.invalidate();
        });

        final ColorPicker secondaryColor = (ColorPicker)findViewById(R.id.secondaryColor);
        secondaryColor.setReleasePicker(releasePicker);
        secondaryColor.setItem(Colors.path(mModel.getTheme().getAccentColor())[0]);
        secondaryColor.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setAccentColor(((Colors)item).shade(subIndex));
            mPreview.invalidate();
        });

        final ColorPicker ternaryPicker = (ColorPicker)findViewById(R.id.ternaryColor);
        ternaryPicker.setReleasePicker(releasePicker);
        ternaryPicker.setItem(Colors.path(mModel.getTheme().getContrastColor())[0]);
        ternaryPicker.setOnItemSelectedListener((item, subIndex) -> {
            mModel.getTheme().setContrastColor(((Colors)item).shade(subIndex));
            mPreview.invalidate();
        });
    }

    private void initializeModel() {
        mModel = new MainModel(this);

        float w = mPreview.getWidth();
        float h = mPreview.getHeight();
        float r = Math.min(h - mPreview.getPaddingTop() - mPreview.getPaddingBottom(),
                w - mPreview.getPaddingRight() - mPreview.getPaddingLeft());
        r /= 2;
        float x = w / 2;
        float y = mPreview.getPaddingTop() + r;
        float sr = r / 3;

        mModel.setWidth((int)w);
        mModel.setHeight((int)h);
        mModel.setRadius(r);
        mModel.setSmallRadius(sr);
        mModel.setPadding(mPreview.getPaddingTop());
        mModel.setX(x);
        mModel.setY(y);
    }

    @Override
    int getLayoutId() {
        return R.layout.layout_style_preference;
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

            new TrueModel(this).setTheme(mModel.getTheme());

            Toast t = Toast.makeText(this, "Style Saved.", Toast.LENGTH_SHORT);
            t.show();
        }
    }
}