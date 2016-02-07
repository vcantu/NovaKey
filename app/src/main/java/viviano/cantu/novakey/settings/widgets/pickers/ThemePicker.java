package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.util.AttributeSet;

import viviano.cantu.novakey.themes.BaseTheme;
import viviano.cantu.novakey.themes.Theme;
import viviano.cantu.novakey.themes.ThemeBuilder;

/**
 * Created by Viviano on 1/24/2016.
 */
public class ThemePicker extends HorizontalPicker {

    /**
     * Constructor used by XML layout.
     *
     * @param context
     * @param attrs
     */
    public ThemePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Will be called during the constructor to initialize the picker items
     *
     * @return the array that will be set to the pickerItems
     */
    @Override
    protected PickerItem[] initializeItems() {
        PickerItem[] arr = new PickerItem[ThemeBuilder.THEME_COUNT];
        for (Theme t : ThemeBuilder.allThemes()) {
            arr[t.themeID()] = t;
        }
        return arr;
    }

    /**
     * Will be called when the given item is long pressed. Used to communicate,
     * with the ReleasePicker
     *
     * @param index  index of item which has been long pressed
     * @param startX corrected finger X position
     * @param startY corrected finger Y position
     */
    @Override
    protected void onItemLongPress(int index, float startX, float startY) {
        //Do nothing
    }
}
