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
import android.graphics.Paint;

import viviano.cantu.novakey.core.utils.drawing.Draw;

/**
 * Created by Viviano on 6/6/2015.
 */
public class MaterialTheme extends BaseTheme {

    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        if (mParent.is3D())
            pB.setShadowLayer(r / 72f / 2, 0, r / 72f / 2, 0x80000000);
        //draw lines and circle
        pB.setColor(mParent.getAccentColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        Draw.lines(x, y, r, sr, (r - sr) / 10, mParent.getAccentColor(), pB, canvas);

        pB.clearShadowLayer();
    }
}
