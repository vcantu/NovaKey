package viviano.cantu.novakey.view.themes.button;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import viviano.cantu.novakey.view.drawing.Font;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.drawing.shapes.Shape;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 9/10/2016.
 */
public class BaseButtonTheme implements ButtonTheme {

    private MasterTheme mParent;
    private final Paint p;

    public BaseButtonTheme() {
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);//smooth edges and Never changes
        p.setTypeface(Font.SANS_SERIF_LIGHT);
    }

    /**
     * Draws an object, ensuring contrast, on top of the board.
     * Use this to draw thngs like letters, text, icons bmps and such
     *
     * @param shape  drawable object to draw
     * @param x      x position to draw
     * @param y      y position to draw
     * @param size   size of object to draw
     * @param canvas canvas to draw on
     */
    @Override
    public void drawBack(Shape shape, float x, float y, float size, Canvas canvas) {
        //no back for base theme
    }

    /**
     * Draws an object, ensuring contrast, on top of the board.
     * Use this to draw thngs like letters, text, icons bmps and such
     *
     * @param drawable drawable object to draw
     * @param x        x position to draw
     * @param y        y position to draw
     * @param size     size of object to draw
     * @param canvas   canvas to draw on
     */
    @Override
    public void drawIcon(Drawable drawable, float x, float y, float size, Canvas canvas) {
        if (mParent.is3D() && false)
            p.setShadowLayer(50, 0, 50, 0x80000000);//TODO: globalize shadow height
        p.setStyle(Paint.Style.FILL);
        p.setColor(mParent.getContrastColor());
        drawable.draw(x, y, size, p, canvas);
        p.clearShadowLayer();
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
