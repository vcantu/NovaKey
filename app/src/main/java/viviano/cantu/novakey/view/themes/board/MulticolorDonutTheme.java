package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 6/14/2015.
 */
public class MulticolorDonutTheme extends BaseTheme {

    private int[] colors;
    private boolean autoColor = true;

    public void setColors() {
        if (autoColor) {
            //automatic colors here
            colors = new int[5];
            int addTo = 1;
            for (int i = 0; i < colors.length; i++) {
                int test = Util.colorShade(mParent.getAccentColor(), addTo + i);
                if (test == Color.WHITE) {
                    addTo = colors.length * -1;
                    test = Util.colorShade(mParent.getAccentColor(), addTo + i);
                }
                colors[i] = test;
            }
        }
    }

    //allows speical themes to override the colors
    public void setColors(int[] colors) {
        this.colors = colors;
        autoColor = false;
    }

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        setColors();

        if (mParent.is3D()) {
            pB.setShadowLayer(r * .025f, 0, r * .025f, 0x80000000);
            pB.setStrokeWidth(r - sr);
            pB.setStyle(Paint.Style.STROKE);
            pB.setColor(0);
            canvas.drawCircle(x, y, sr + (r-sr) / 2, pB);
            //reset
            pB.setStrokeWidth(0);
            pB.clearShadowLayer();
        }

        pB.setStyle(Paint.Style.FILL);

        Path path = new Path();
        for (int i=0; i<5; i++) {
//            if (is3D())
//                y -= (r * .01f);
            pB.setColor(colors[i]);
            double angle =  Math.PI / 2 + (i * 2 * Math.PI) / 5;
            angle = (angle > Math.PI * 2 ? angle - Math.PI * 2 : angle);
            angle = -angle;

            path.arcTo(new RectF(x -  r, y - r, x + r, y + r),
                    (float)Math.toDegrees(angle), -360 / 5);
            path.arcTo(new RectF(x - sr, y - sr, x + sr, y + sr),
                    (float)Math.toDegrees(angle) -360 / 5, 360 / 5);
            path.close();

            canvas.drawPath(path, pB);
            path.reset();
        }
    }

    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //do nothing
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
