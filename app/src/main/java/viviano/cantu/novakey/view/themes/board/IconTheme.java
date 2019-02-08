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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.drawing.ShadowDimens;

/**
 * Created by Viviano on 3/11/2016.
 */
public class IconTheme extends BaseTheme {

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        pB.setStyle(Paint.Style.FILL);

        RectF rect = new RectF(x - r, y - r, x + r, y + r);
        Path p = new Path();

        boolean leftIsDark = false;
        int c = Util.colorShade(mParent.getPrimaryColor(), 4);
        if (c == Color.WHITE) {
            leftIsDark = true;
            c = Util.colorShade(mParent.getPrimaryColor(), -4);
        }

        p.addArc(rect, 90, 180 * (leftIsDark ? -1 : 1));
        p.close();
        pB.setColor(mParent.getPrimaryColor());
        canvas.drawPath(p, pB);

        p.reset();
        p.addArc(rect, 90, 180 * (leftIsDark ? 1 : -1));
        p.close();
        pB.setColor(c);
        canvas.drawPath(p, pB);
    }


    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        float sw = r * w * 4;
        ShadowDimens sd = ShadowDimens.fromAngle(270, sw / 4);

        if (mParent.is3D()) {
            //pB.setStrokeWidth(sw);
            //Draw.shadedLines(x, y + sw * 2 / 3f, r - (sr * 0.1f), sr * 1.1f, 0x80000000, pB, canvas);
            pB.setShadowLayer(sd.r, sd.x, sd.y, 0x80000000);
        }
        //draw lines and circle
        pB.setColor(mParent.getAccentColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(sw);
        //draw circles
        canvas.drawCircle(x, y, sr, pB);
        canvas.drawCircle(x, y, r, pB);

        //draw lines
        Path p = new Path();
        RectF rect = new RectF(x - sw / 4, y - r + (r - sr) / 6f,
                x + sw / 4, y - sr - (r - sr) / 6f);
        p.addOval(rect, Path.Direction.CW);

        pB.setStyle(Paint.Style.FILL_AND_STROKE);
        pB.setStrokeWidth(sw / 2);
        pB.setStrokeCap(Paint.Cap.ROUND);
        try {
            canvas.save();
            canvas.drawPath(p, pB);
            for (int i = 1; i <= 4; i++) {
                if (mParent.is3D()) {
                    sd = sd.fromAngle(270 + (360 / 5 * i));
                    pB.setShadowLayer(sd.r, sd.x, sd.y, 0x80000000);
                }
                canvas.rotate(360 / 5, x, y);
                canvas.drawPath(p, pB);
            }
            canvas.restore();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        pB.clearShadowLayer();
    }
}
