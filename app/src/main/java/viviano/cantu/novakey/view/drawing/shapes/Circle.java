package viviano.cantu.novakey.view.drawing.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/17/2016.
 */
public class Circle implements Shape {

    /**
     * Determines if the given coordinates are inside the shape
     *
     * @param fingX x coordinate to test
     * @param fingY y coordinate to test
     * @param x x coordinate to test
     * @param y y coordinate to test
     * @param size
     * @return true if inside the shape
     */
    @Override
    public boolean isInside(float fingX, float fingY, float x, float y, float size) {
        return Util.distance(fingX, fingY, x, y) <= size;
    }

    /**
     * DO NOT CALL THIS call Icons.draw() instead as it does null checks before
     * drawing
     *
     * @param x      x position
     * @param y      y position
     * @param size   size of icon
     * @param p      paint to use
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        canvas.drawCircle(x, y, size, p);
    }
}
