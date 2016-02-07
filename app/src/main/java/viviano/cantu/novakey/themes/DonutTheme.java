package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/10/2015.
 */
public class DonutTheme extends BaseTheme {

    /**
     * @return Its name identifier, if it inherits from another theme
     * it must include its parents name in the format ParentName.ThisName
     */
    @Override
    public String themeName() {
        return super.themeName() + ".Donut";
    }

    /**
     * @return an integer ID unique to this theme
     */
    @Override
    public int themeID() {
        return 3;
    }

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        if (is3D())
            pB.setShadowLayer(r * .025f, 0, r * .025f, 0x80000000);
        //draw background flat color
        pB.setColor(accentColor());
        pB.setStyle(Paint.Style.STROKE);
        float mem = pB.getStrokeWidth();
        pB.setStrokeWidth(r-sr);
        canvas.drawCircle(x, y, r-sr, pB);//main circle
        pB.setStrokeWidth(mem);
        pB.clearShadowLayer();
    }

    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(primaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);
        //draw circles & lines
        Draw.lines(x, y, r, sr, (r - sr) / 10, primaryColor(), pB, canvas);
    }

    @Override
    public void setTextColors() {
        super.setTextColors();
        textColors[0] = buttonColor();
    }
    @Override
    public int textColor() {
        return Util.bestColor(primaryColor(), contrastColor(), accentColor());
    }
    @Override
    public int buttonColor() {
        return Util.bestColor(contrastColor(), accentColor(), primaryColor());
    }
}
