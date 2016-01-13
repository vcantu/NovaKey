package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Viviano on 6/14/2015.
 */
public class ThemeMulticolor extends ThemeMulticolorDonut {

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        super.drawBoardBack(x, y, r, sr, canvas);
        pB.setStyle(Paint.Style.FILL);
        pB.setColor(secondaryColor());
        canvas.drawCircle(x, y, sr, pB);
    }

    @Override
    protected int middleColor() {
        return textColor();
    }
}
