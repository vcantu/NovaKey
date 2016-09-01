package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/14/2015.
 */
public class MulticolorTheme extends MulticolorDonutTheme {

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        super.drawBoardBack(x, y, r, sr, canvas);
        pB.setStyle(Paint.Style.FILL);
        pB.setColor(mParent.getAccentColor());
        canvas.drawCircle(x, y, sr, pB);
    }
}
