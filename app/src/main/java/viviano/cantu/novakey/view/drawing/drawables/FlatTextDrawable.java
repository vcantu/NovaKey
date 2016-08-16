package viviano.cantu.novakey.view.drawing.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.drawing.Draw;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 6/21/2016.
 */
public class FlatTextDrawable implements Drawable {

    private final String mText;

    public FlatTextDrawable(String text) {
        mText = text;
    }

    /**
     * Interface for any kind of drawable
     *
     * @param x      x position
     * @param y      y position
     * @param size   size of icon
     * @param p      paint to use
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Draw.textFlat(mText, x, y, size, p, canvas);
    }
}
