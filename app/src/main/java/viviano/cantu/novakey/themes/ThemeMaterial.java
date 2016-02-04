package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.drawing.Draw;

/**
 * Created by Viviano on 6/6/2015.
 */
public class ThemeMaterial extends BaseTheme {
    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(secondaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        Draw.lines(x, y, r, sr, (r - sr) / 10, secondaryColor(), pB, canvas);
    }
}
