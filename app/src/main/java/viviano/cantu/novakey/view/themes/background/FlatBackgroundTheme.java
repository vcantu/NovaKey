package viviano.cantu.novakey.view.themes.background;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 8/14/2016.
 */
public class FlatBackgroundTheme implements BackgroundTheme {

    private final Paint p;
    private MasterTheme mParent;

    public FlatBackgroundTheme() {
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);//smooth edges and Never changes
    }

    /**
     * Draw background of keyboard
     *
     * @param l      left position
     * @param t      top position
     * @param rt     right position
     * @param b      bottom position
     * @param x      center X position
     * @param y      center Y position
     * @param r      radius of keyboard
     * @param sr     small radius of keyboard
     * @param canvas canvas to draw on
     */
    @Override
    public void drawBackground(float l, float t, float rt, float b,
                               float x, float y, float r, float sr, Canvas canvas) {
        p.setStyle(Paint.Style.FILL);
        p.setColor(mParent.getPrimaryColor());
        canvas.drawRect(l, t, rt, b, p);
    }

    /**
     * Sets this child's master theme for reference
     *
     * @param masterTheme this theme's parent
     */
    @Override
    public void setParent(MasterTheme masterTheme) {
        mParent = masterTheme;
    }
}
