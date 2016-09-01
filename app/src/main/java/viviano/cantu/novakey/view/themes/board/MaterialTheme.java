package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.drawing.Draw;

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
