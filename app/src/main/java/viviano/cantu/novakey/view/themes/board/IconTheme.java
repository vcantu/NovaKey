package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import viviano.cantu.novakey.view.drawing.ShadowDimens;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 3/11/2016.
 */
public class IconTheme extends BaseTheme {

    /**
     * @return Its name identifier, if it inherits from another theme
     * it must include its parents name in the format ParentName.ThisName
     */
    @Override
    public String themeName() {
        return super.themeName() + ".Icon";
    }

    /**
     * @return an integer ID unique to this theme
     */
    @Override
    public int themeID() {
        return 6;
    }


    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        pB.setStyle(Paint.Style.FILL);

        RectF rect = new RectF(x - r, y - r, x + r, y + r);
        Path p = new Path();

        boolean leftIsDark = false;
        int c = Util.colorShade(primaryColor(), 4);
        if (c == Color.WHITE) {
            leftIsDark = true;
            c = Util.colorShade(primaryColor(), -4);
        }

        p.addArc(rect, 90, 180 * (leftIsDark ? -1 : 1));
        p.close();
        pB.setColor(primaryColor());
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

        if (is3D()) {
            //pB.setStrokeWidth(sw);
            //Draw.shadedLines(x, y + sw * 2 / 3f, r - (sr * 0.1f), sr * 1.1f, 0x80000000, pB, canvas);
            pB.setShadowLayer(sd.r, sd.x, sd.y, 0x80000000);
        }
        //draw lines and circle
        pB.setColor(accentColor());
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
                if (is3D()) {
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
