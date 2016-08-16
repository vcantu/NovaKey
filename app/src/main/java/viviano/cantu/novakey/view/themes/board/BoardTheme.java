package viviano.cantu.novakey.view.themes.board;

import android.graphics.Canvas;

import viviano.cantu.novakey.settings.widgets.pickers.PickerItem;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.themes.ChildTheme;

/**
 * Created by Viviano on 2/2/2016.
 */
public interface BoardTheme extends PickerItem, ChildTheme {

    /**
     * Draw the center of the board
     *
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawBoard(float x, float y, float r, float sr, Canvas canvas);

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
    void drawItem(Drawable drawable, float x, float y, float size, Canvas canvas);
}
