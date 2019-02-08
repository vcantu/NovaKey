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

package viviano.cantu.novakey.view.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/17/2016.
 */
public class Circle implements Shape {

    /**
     * Determines if the given coordinates are inside the shape
     *
     * @param fingX x coordinate to test
     * @param fingY y coordinate to test
     * @param x     x coordinate to test
     * @param y     y coordinate to test
     * @param size
     * @return true if inside the shape
     */
    @Override
    public boolean isInside(float fingX, float fingY, float x, float y, float size) {
        return Util.distance(fingX, fingY, x, y) <= size / 2;
    }


    /**
     * DO NOT CALL THIS call Icons.draw() instead as it does null checks before
     * drawing
     *
     * @param x      x position
     * @param y      y position
     * @param size   size of icon
     * @param p      paint to use
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        canvas.drawCircle(x, y, size / 2, p);
    }
}
