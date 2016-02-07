package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/7/2015.
 */
public class SeparateSectionsTheme extends BaseTheme {

    /**
     * @return Its name identifier, if it inherits from another theme
     * it must include its parents name in the format ParentName.ThisName
     */
    @Override
    public String themeName() {
        return super.themeName() + ".SeparateSections";
    }

    /**
     * @return an integer ID unique to this theme
     */
    @Override
    public int themeID() {
        return 2;
    }

    @Override
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        if (is3D())
            pB.setShadowLayer(r * .025f, 0, r * .025f, 0x80000000);
        //draw background flat color
        pB.setColor(accentColor());
        pB.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, r, pB);//main circle
        
        pB.clearShadowLayer();
    }

    @Override
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(primaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);// * (9 / 5f)));
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        Draw.lines(x, y, r, sr, -1, primaryColor(), pB, canvas);
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
