package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Viviano on 6/14/2015.
 */
public class MulticolorTheme extends MulticolorDonutTheme {

    /**
     * @return Its name identifier, if it inherits from another theme
     * it must include its parents name in the format ParentName.ThisName
     */
    @Override
    public String themeName() {
        return super.themeName() + ".Filled";
    }

    /**
     * @return an integer ID unique to this theme
     */
    @Override
    public int themeID() {
        return 5;
    }

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        super.drawBoardBack(x, y, r, sr, canvas);
        pB.setStyle(Paint.Style.FILL);
        pB.setColor(accentColor());
        canvas.drawCircle(x, y, sr, pB);
    }

    @Override
    protected int middleColor() {
        return textColor();
    }
}
