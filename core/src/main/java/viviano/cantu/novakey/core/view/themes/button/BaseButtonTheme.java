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

package viviano.cantu.novakey.core.view.themes.button;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.core.utils.drawing.Font;
import viviano.cantu.novakey.core.utils.drawing.drawables.Drawable;
import viviano.cantu.novakey.core.utils.drawing.shapes.Shape;
import viviano.cantu.novakey.core.view.themes.MasterTheme;

/**
 * Created by Viviano on 9/10/2016.
 */
public class BaseButtonTheme implements ButtonTheme {

    private MasterTheme mParent;
    private final Paint p;


    public BaseButtonTheme() {
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);//smooth edges and Never changes
        p.setTypeface(Font.SANS_SERIF_LIGHT);
    }


    /**
     * Draws an object, ensuring contrast, on top of the board.
     * Use this to draw thngs like letters, text, icons bmps and such
     *
     * @param shape  drawable object to draw
     * @param x      x position to draw
     * @param y      y position to draw
     * @param size   size of object to draw
     * @param canvas canvas to draw on
     */
    @Override
    public void drawBack(Shape shape, float x, float y, float size, Canvas canvas) {
        //no back for base theme
    }


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
    @Override
    public void drawIcon(Drawable drawable, float x, float y, float size, Canvas canvas) {
        if (mParent.is3D() && false)
            p.setShadowLayer(50, 0, 50, 0x80000000);//TODO: globalize shadow height
        p.setStyle(Paint.Style.FILL);
        p.setColor(mParent.getContrastColor());
        drawable.draw(x, y, size, p, canvas);
        p.clearShadowLayer();
    }


    /**
     * Sets this child's master theme for reference
     *
     * @param masterTheme this theme's parent
     */
    @Override
    public void setParent(MasterTheme masterTheme) {
        mParent = masterTheme;
    }
}
