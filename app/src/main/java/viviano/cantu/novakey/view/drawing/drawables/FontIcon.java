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
 * Created by Viviano on 12/26/2015.
 */
public class FontIcon implements Drawable {

    private String name, code;
    private Typeface font;


    public FontIcon(String name, String code, Typeface font) {
        this.name = name;
        this.code = code;
        this.font = font;
    }


    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Typeface tempTTF = p.getTypeface();
        float tempSize = p.getTextSize();

        p.setTypeface(font);
        p.setTextSize(size);
        Draw.text(code, x, y, p, canvas);

        p.setTypeface(tempTTF);
        p.setTextSize(tempSize);
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof String) {
            String str = (String) o;
            return name.equalsIgnoreCase(str) || code.equalsIgnoreCase(str);
        }
        return super.equals(o);
    }
}
