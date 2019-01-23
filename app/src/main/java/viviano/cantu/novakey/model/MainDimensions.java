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

package viviano.cantu.novakey.model;

/**
 * Created by viviano on 11/26/2017.
 *
 * Main dimensions of the keyboard
 */

public class MainDimensions extends Dimensions{

    public static String
            X = "x",
            Y = "y",
            RADIUS = "radius",
            SMALL_RADIUS = "smallRadius",
            PADDING = "padding",
            WIDTH = "width",
            HEIGHT = "height";

    public MainDimensions(float x, float y, float radius, float smallRadius,
                          float padding, int width, int height) {
        set(X, x);
        set(Y, y);
        set(RADIUS, radius);
        set(SMALL_RADIUS, smallRadius);
        set(PADDING, padding);
        set(WIDTH, width);
        set(HEIGHT, height);
    }

    public float getX() {
        return getF(X);
    }

    public void setX(float x) {
        set(X, x);
    }

    public float getY() {
        return getF(Y);
    }

    public void setY(float y) {
        set(Y, y);
    }

    public float getRadius() {
        return getF(RADIUS);
    }

    public void setRadius(float radius) {
       set(RADIUS, radius);
    }

    public float getSmallRadius() {
        return getF(SMALL_RADIUS);
    }

    public void setSmallRadius(float smallRadius) {
        set(SMALL_RADIUS, smallRadius);
    }

    public float getPadding() {
        return getF(PADDING);
    }

    public void setPadding(float padding) {
        set(PADDING, padding);
    }

    public int getWidth() {
        return getI(WIDTH);
    }

    public void setWidth(int width) {
        set(WIDTH, width);
    }

    public int getHeight() {
        return getI(HEIGHT);
    }

    public void setHeight(int height) {
        set(HEIGHT, height);
    }
}
