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

package viviano.cantu.novakey.view.drawing.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import viviano.cantu.novakey.view.drawing.Draw;

/**
 * Created by Viviano on 6/21/2016.
 */
public class TextDrawable implements Drawable {

    private String mText;
    private Typeface mFont;


    public TextDrawable(String text, Typeface font) {
        mText = text;
        mFont = font;
    }


    public TextDrawable(String text) {
        this(text, null);
    }


    /**
     * Interface for any kind of drawable
     *
     * @param x      x position
     * @param y      y position
     * @param size   size of icon
     * @param p      paint to use
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        if (mFont != null)
            p.setTypeface(mFont);
        Draw.text(mText, x, y, size, p, canvas);
    }


    /**
     * Sets this drawables font
     *
     * @param font null will use the font given by the paint
     */
    public void setFont(Typeface font) {
        mFont = font;
    }


    /**
     * Sets this drawables text
     *
     * @param text cannot be null
     */
    public void setText(String text) {
        if (text == null)
            throw new NullPointerException("Text cannot be null");
        mText = text;
    }
}
