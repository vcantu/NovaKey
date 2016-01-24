package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/10/2015.
 */
public class ThemeDonut extends Theme {

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        //draw background flat color
        pB.setColor(secondaryColor());
        pB.setStyle(Paint.Style.STROKE);
        float mem = pB.getStrokeWidth();
        pB.setStrokeWidth(r-sr);
        canvas.drawCircle(x, y, r-sr, pB);//main circle
        pB.setStrokeWidth(mem);
    }

    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(primaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        Draw.lines(x, y, r, sr, (r - sr) / 10, primaryColor(), pB, canvas);
    }

    @Override
    public void setTextColors() {
        super.setTextColors();
        textColors[0] = buttonColor();
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
