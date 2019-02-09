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

package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.core.utils.Util;
import viviano.cantu.novakey.core.utils.drawing.Draw;
import viviano.cantu.novakey.core.utils.drawing.drawables.Drawable;

/**
 * Created by Viviano on 6/10/2015.
 */
public class DonutTheme extends BaseTheme {

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        if (mParent.is3D())
            pB.setShadowLayer(r * .025f, 0, r * .025f, 0x80000000);
        //draw background flat color
        pB.setColor(mParent.getAccentColor());
        pB.setStyle(Paint.Style.STROKE);
        float mem = pB.getStrokeWidth();
        pB.setStrokeWidth(r - sr);
        canvas.drawCircle(x, y, sr + (r - sr) / 2, pB);//main circle
        pB.setStrokeWidth(mem);
        pB.clearShadowLayer();
    }


    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(mParent.getPrimaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);
        //draw circles & lines
        Draw.lines(x, y, r, sr, (r - sr) / 10, mParent.getPrimaryColor(), pB, canvas);
    }


    @Override
    public void drawItem(Drawable drawable, float x, float y, float size, Canvas canvas) {
        //        menu.draw(view, this, canvas);
//        if (outerColor() != textColors()[0]) {
//            try {
//                canvas.save();
//                Path p = new Path();
//                p.addCircle(x, y, sr + 2, Path.Direction.CW);
//                canvas.clipPath(p);
//
//                pT.setColor(textColors()[0]);
//                menu.draw(view, this, canvas);
//                canvas.restore();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//        }
        //TODO: multi color for donut themes
        super.drawItem(drawable, x, y, size, canvas);
    }


    protected int outerColor() {
        return Util.bestColor(
                mParent.getPrimaryColor(),
                mParent.getContrastColor(),
                mParent.getAccentColor());
    }


    protected int centerColor() {
        return Util.bestColor(
                mParent.getContrastColor(),
                mParent.getAccentColor(),
                mParent.getPrimaryColor());
    }
}
