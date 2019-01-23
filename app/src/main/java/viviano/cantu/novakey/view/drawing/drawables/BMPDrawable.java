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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.drawing.Draw;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 12/26/2015.
 */
public class BMPDrawable implements Drawable {

    Bitmap bmp;

    public BMPDrawable(Resources res, int id) {
        bmp = BitmapFactory.decodeResource(res, id);
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Draw.bitmap(bmp, x, y, size / bmp.getWidth(), p, canvas);
    }
}
