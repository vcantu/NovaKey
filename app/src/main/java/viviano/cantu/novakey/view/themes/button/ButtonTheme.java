package viviano.cantu.novakey.view.themes.button;

import android.graphics.Canvas;

import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.drawing.shapes.Shape;
import viviano.cantu.novakey.view.themes.ChildTheme;

/**
 * Created by Viviano on 8/14/2016.
 */
public interface ButtonTheme extends ChildTheme {

    /**
     * Draws an object, ensuring contrast, on top of the board.
     * Use this to draw thngs like letters, text, icons bmps and such
     *
     * @param shape drawable object to draw
     * @param x x position to draw
     * @param y y position to draw
     * @param size size of object to draw
     * @param canvas canvas to draw on
     */
    void drawBack(Shape shape, float x, float y, float size, Canvas canvas);

    /**
     * Draws an object, ensuring contrast, on top of the board.
     * Use this to draw thngs like letters, text, icons bmps and such
     *
     * @param drawable drawable object to draw
     * @param x x position to draw
     * @param y y position to draw
     * @param size size of object to draw
     * @param canvas canvas to draw on
     */
    void drawIcon(Drawable drawable, float x, float y, float size, Canvas canvas);
}
