package viviano.cantu.novakey.themes;

import android.graphics.Canvas;

import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.menus.OnUpMenu;

/**
 * Created by Viviano on 2/2/2016.
 */
public interface Theme {

    /**
     * @return Its name identifier, if it inherits from another theme
     * it must include its parents name in the format ParentName.ThisName
     */
    String themeName();

    /**
     * @return an integer ID unique to this theme
     */
    int themeID();


    /**
     * Draw background of keyboard
     *
     * @param l left position
     * @param t top position
     * @param rt right position
     * @param b bottom position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawBackground(float l, float t, float rt, float b, float r, float sr, Canvas canvas);

    /**
     * Draw board if necessary, of isUndocked
     *
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param isUndocked whether the keyboard is attached to bottom of device
     * @param canvas canvas to draw on
     */
    void drawBoard(float x, float y, float r, float sr, boolean isUndocked, Canvas canvas);

    /**
     * Make the buttons draw themselves with the given parameters
     *
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawButtons(float x, float y, float r, Canvas canvas);

    /**
     * Make the given keyLayout draw itself with the given parameters
     *
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param kl KeyLayout to draw
     * @param isCapsedLocked whether keyboard is in capsedLocked state
     * @param canvas canvas to draw on
     */
    void drawKeys(float x, float y, float r, float sr, KeyLayout kl, boolean isCapsedLocked,
                  Canvas canvas);

    /**
     * Draw cursor icons depending on the state of the cursor
     *
     * @param state state of the cursor
     * @param x center X position
     * @param y center Y position
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawCursorIcon(int state, float x, float y, float sr, Canvas canvas);

    /**
     * Makes the infinite menu draw itself
     *
     * @param menu infinite menu to draw
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawInfiniteMenu(InfiniteMenu menu, float x, float y, float r, float sr, Canvas canvas);

    /**
     * Makes the on up menu draw itself
     *
     * @param menu OnUpMenu menu to draw
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawOnUpMenu(OnUpMenu menu, float x, float y, float r, float sr, Canvas canvas);

}
