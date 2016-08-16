package viviano.cantu.novakey.view.drawing.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Viviano on 6/21/2016.
 *
 * Simple interface for drawable items
 */
public interface Drawable {
    /**
     * Interface for any kind of drawable
     *
     * @param x      x position
     * @param y      y position
     * @param size   size of icon
     * @param p      paint to use
     * @param canvas canvas to draw on
     */
    void draw(float x, float y, float size, Paint p, Canvas canvas);
}
