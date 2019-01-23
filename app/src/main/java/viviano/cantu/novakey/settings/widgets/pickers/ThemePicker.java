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

package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.util.AttributeSet;

import viviano.cantu.novakey.view.themes.BaseMasterTheme;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.model.factories.ThemeFactory;

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
     * Will be called during the constructor to start the picker items
     *
     * @return the array that will be set to the pickerItems
     */
    @Override
    protected PickerItem[] initializeItems() {
        MasterTheme base = new BaseMasterTheme();
        PickerItem[] arr = new PickerItem[ThemeFactory.BOARDS.size()];
        int i=0;
        for (BoardTheme t : ThemeFactory.BOARDS) {
            arr[i] = t;
            t.setParent(base);
            i++;
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
