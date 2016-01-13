package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.Util;

/**
 * Created by Viviano on 6/7/2015.
 */
public class ThemeSeparate extends Theme {

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        //draw background flat color
        pB.setColor(secondaryColor());
        pB.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, r, pB);//main circle
    }

    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(primaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * (w));// * (9 / 5f)));
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        Draw.lines(x, y, r, sr, -1, primaryColor(), pB, canvas);
    }

    @Override
    public int textColor() {
        return Util.bestColor(primaryColor(), ternaryColor(), secondaryColor());
    }
    @Override
    public int buttonColor() {
        return Util.bestColor(ternaryColor(), secondaryColor(), primaryColor());
    }
}
