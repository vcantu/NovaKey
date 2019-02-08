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

package viviano.cantu.novakey.view.themes.background;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 8/14/2016.
 */
public class FlatBackgroundTheme implements BackgroundTheme {

    private final Paint p;
    private MasterTheme mParent;


    public FlatBackgroundTheme() {
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);//smooth edges and Never changes
    }


    /**
     * Draw background of keyboard
     *
     * @param l      left position
     * @param t      top position
     * @param rt     right position
     * @param b      bottom position
     * @param x      center X position
     * @param y      center Y position
     * @param r      radius of keyboard
     * @param sr     small radius of keyboard
     * @param canvas canvas to draw on
     */
    @Override
    public void drawBackground(float l, float t, float rt, float b,
                               float x, float y, float r, float sr, Canvas canvas) {
        p.setStyle(Paint.Style.FILL);
        p.setColor(mParent.getPrimaryColor());
        canvas.drawRect(l, t, rt, b, p);
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
