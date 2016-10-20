package viviano.cantu.novakey.view.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by vcantu on 10/2/16.
 */
public class Square implements Shape {

    /**
     * Determines if the given coordinates are inside the shape
     *
     * @param fingX x coordinate to test
     * @param fingY y coordinate to test
     * @param x     x coordinate of shape
     * @param y     y coordinate of shape
     * @param size  size of shape
     * @return true if inside the shape
     */
    @Override
    public boolean isInside(float fingX, float fingY, float x, float y, float size) {
        return fingX >= x - size / 2 && fingX <= x + size / 2 &&
                fingY >= y - size / 2 && fingY <= y + size / 2;
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
        canvas.drawRect(x - size / 2, y - size / 2, x + size / 2, y + size / 2, p);
    }
}
