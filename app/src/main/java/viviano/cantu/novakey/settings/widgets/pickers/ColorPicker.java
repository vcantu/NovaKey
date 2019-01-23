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

import viviano.cantu.novakey.settings.Colors;

/**
 * Created by Viviano on 1/8/2016.
 */
public class ColorPicker extends HorizontalPicker {

    /**
     * Constructor used by XML layout.
     * Will also set all the subIndexes to the defaults
     *
     * @param context
     * @param attrs
     */
    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSubIndexes = new int[Colors.ALL.length];
        for (int i=0; i<mSubIndexes.length; i++) {
            mSubIndexes[i] = Colors.ALL[i].mainIndex();
        }
    }

    /**
     * Will be called during the constructor to start the picker items
     *
     * @return the array that will be set to the pickerItems
     */
    @Override
    protected PickerItem[] initializeItems() {
        return Colors.ALL;
    }

    /**
     * Will start the release picker if the selected island has more than one shade
     * it will do nothing
     *
     * @param index index of item which has been long pressed
     * @param startX corrected finger X position
     * @param startY corrected finger Y position
     */
    @Override
    protected void onItemLongPress(int index, float startX, float startY) {
        if (mReleasePicker != null) {
            Colors color = (Colors)mItems[index];

            if (color.size() > 1) {
                mOnReleasePicker = true;
                getParent().requestDisallowInterceptTouchEvent(true);
                mReleasePicker.setVisibility(VISIBLE);
                int[] subIndex = new int[color.size()];
                for (int i = 0; i < color.size(); i++) {
                    subIndex[i] = i;
                }
                mReleasePicker.onStart(startX, startY, mItems[index], subIndex);
            }
        }
    }
}
