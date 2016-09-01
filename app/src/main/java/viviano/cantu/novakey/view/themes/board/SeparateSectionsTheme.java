package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.drawing.Draw;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 6/7/2015.
 */
public class SeparateSectionsTheme extends BaseTheme {

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        if (mParent.is3D())
            pB.setShadowLayer(r * .025f, 0, r * .025f, 0x80000000);
        //draw background flat color
        pB.setColor(mParent.getAccentColor());
        pB.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, r, pB);//main circle

        pB.clearShadowLayer();
    }

    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(mParent.getPrimaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);// * (9 / 5f)));
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        Draw.lines(x, y, r, sr, -1, mParent.getPrimaryColor(), pB, canvas);
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
