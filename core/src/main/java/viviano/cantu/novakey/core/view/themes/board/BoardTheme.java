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

package viviano.cantu.novakey.core.view.themes.board;

import android.graphics.Canvas;

import viviano.cantu.novakey.core.utils.PickerItem;
import viviano.cantu.novakey.core.utils.drawing.drawables.Drawable;
import viviano.cantu.novakey.core.view.themes.ChildTheme;

/**
 * Created by Viviano on 2/2/2016.
 */
public interface BoardTheme extends PickerItem, ChildTheme {

    /**
     * Draw the center of the board
     *
     * @param x      center X position
     * @param y      center Y position
     * @param r      radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawBoard(float x, float y, float r, float sr, Canvas canvas);


    /**
     * Draws an object, ensuring contrast, on top of the board.
     * Use this to draw thngs like letters, text, icons bmps and such
     *
     * @param drawable drawable object to draw
     * @param x        x position to draw
     * @param y        y position to draw
     * @param size     size of object to draw
     * @param canvas   canvas to draw on
     */
    void drawItem(Drawable drawable, float x, float y, float size, Canvas canvas);
}
