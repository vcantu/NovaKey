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

package viviano.cantu.novakey.core.utils;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Viviano on 1/5/2016.
 */
public interface PickerItem {

    /**
     * Draw method for the picker item
     *
     * @param x        center x position
     * @param y        center y position
     * @param dimen    dimension equivalent to the maximum height
     * @param selected whether it is selected
     * @param index    sub index of picker item
     * @param p        paint used
     * @param canvas   canvas to draw on
     */
    void drawPickerItem(float x, float y, float dimen, boolean selected, int index, Paint p, Canvas canvas);
}
