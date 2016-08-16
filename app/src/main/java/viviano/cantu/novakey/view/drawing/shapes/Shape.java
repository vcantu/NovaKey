package viviano.cantu.novakey.view.drawing.shapes;

import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 6/17/2016.
 */
public interface Shape extends Drawable {

    /**
     * Determines if the given coordinates are inside the shape
     *
     * @param fingX x coordinate to test
     * @param fingY y coordinate to test
     * @param x x coordinate of shape
     * @param y y coordinate of shape
     * @param size size of shape
     * @return true if inside the shape
     */
    boolean isInside(float fingX, float fingY, float x, float y, float size);
}
