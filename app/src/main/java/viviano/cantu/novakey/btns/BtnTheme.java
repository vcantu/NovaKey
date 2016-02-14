package viviano.cantu.novakey.btns;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import viviano.cantu.novakey.Font;
import viviano.cantu.novakey.NovaKeyDimen;
import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 7/9/2015.
 * Base button theme
 */
public class BtnTheme {

    protected Paint p;
    private int mBackColor, mFrontColor;
    protected boolean mIs3D = false;

    /**
     * Constructor initializes paint
     */
    public BtnTheme() {
        p = new Paint();
        p.setAntiAlias(true);
        p.setTypeface(Font.SANS_SERIF_LIGHT);
    }

    /**
     * Use this to set both colors
     * @param back back color
     * @param front front color
     */
    public void setColors(int back, int front) {
        mBackColor = back;
        mFrontColor = front;
    }

    /**
     * set back color
     * @param color back color
     */
    public void setBackColor(int color) {
        mBackColor = color;
    }

    /**
     * Set front color
     * @param color front color
     */
    public void setFrontColor(int color) {
        mFrontColor = color;
    }

    /**
     * @return returns this theme's back color
     */
    public int backColor() {
        return mBackColor;
    }

    /**
     * @return returns this theme's front color
     */
    public int frontColor() {
        return mFrontColor;
    }

    /**
     * Set whether this theme is 3D
     * @param is3D whether to set tom3D
     */
    public void setIs3D(boolean is3D) {
        mIs3D = is3D;
    }

    /**
     * @return whether this theme is 3D
     */
    public boolean is3D() {
        return mIs3D;
    }

    /**
     * Will call the drawBack & drawIcon methods
     *
     * @param x button X position
     * @param y button Y position
     * @param shape shape of button
     * @param dimens dimensions of NovaKey
     * @param canvas canvas to draw on
     */
    public final void draw(Object icon, float x, float y, int shape, NovaKeyDimen dimens,
                           Canvas canvas) {
        preDrawBack(dimens);
        drawBack(x, y, shape, dimens, canvas);
        preDrawIcon(dimens);
        drawIcon(icon, x, y, getIconSize(shape, dimens), canvas);
    }

    /**
     * Use this to override paint settings before drawing the back of the button
     */
    protected void preDrawBack(NovaKeyDimen dimens) {
        p.setColor(backColor());
    }

    /**
     * Will draw the board back if subclasses choose to override it
     *
     * @param x button center X position
     * @param y button center Y position
     * @param shape shape of button
     * @param dimens dimensions of NovaKey
     * @param canvas canvas to draw on
     */
    protected void drawBack(float x, float y, int shape, NovaKeyDimen dimens, Canvas canvas) {
        //draw nothing by default
//        switch (shape & SHAPE) {
//            case CIRCLE:
//                float bRad = getRadius(shape & SIZE, r);
//                if (!t.hasAttrbute(BtnTheme.NO_BACK))
//                    canvas.drawCircle(bx, by, bRad, t.p);
//                size = bRad * .8f * 2;
//                break;
//            case ARC:
//                float w = arcWidth(r);
//                Path path = new Path();
//                double theta = getArcTheta(shape & SIZE);
//                path.arcTo(Util.square(cx, cy, dist * r - w / 2),
//                        (float) Math.toDegrees(angle - theta / 2),
//                        (float) Math.toDegrees(theta));
//                path.arcTo(Util.square(cx, cy, dist * r + w / 2),
//                        (float) Math.toDegrees(angle + theta / 2),
//                        (float) Math.toDegrees(-theta));
//                path.close();
//                if (!t.hasAttrbute(BtnTheme.NO_BACK))
//                    canvas.drawPath(path, t.p);
//
//                size = w * .8f;
//                break;
//        }
    }

    /**
     * @param shape shape of button
     * @param dimens dimensions of NovaKey
     * @return the desired icon size according to the shape & dimensions of keyboard
     */
    private final float getIconSize(int shape, NovaKeyDimen dimens) {
        switch (shape & Btn.SHAPE) {
            default:
            case Btn.CIRCLE:
                float bRad = Btn.getRadius(shape & Btn.SIZE, dimens.r);
                return bRad * .8f * 2;
            case Btn.ARC:
                float w = Btn.arcWidth(dimens.r);
                return w * .8f;
        }
    }

    /**
     * Use this to override paint settings before drawing the icon
     */
    protected void preDrawIcon(NovaKeyDimen dimens) {
        p.setColor(frontColor());
        p.setStyle(Paint.Style.FILL);
        if (is3D())
            p.setShadowLayer(dimens.r / 72f / 2, 0, dimens.r / 72f / 2, 0x80000000);
    }

    /**
     * Draws the icon of the button according to the given given size
     *
     * @param x desired center X position
     * @param y desired center Y position
     * @param size size of icon
     * @param canvas canvas to draw on
     */
    private final void drawIcon(Object o, float x, float y, float size, Canvas canvas) {
        if (o == null)
            return;
        if (o instanceof Icon.Drawable) {
            Icon.draw((Icon.Drawable)o, x, y, size, p, canvas);
        }
        else if (o instanceof String) {
            String text = (String)o;
            if (text.equals(".")) {
                Draw.textFlat(text, x, y - (p.ascent() + p.descent()) / 2, size * 1.5f, p, canvas);
            }
            else {
                Draw.text(text, x, y, size, p, canvas);
            }
        }
    }
}
