package viviano.cantu.novakey.themes;

import android.graphics.Canvas;

import viviano.cantu.novakey.model.keyboards.KeyLayout;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.menus.OnUpMenu;
import viviano.cantu.novakey.settings.widgets.pickers.PickerItem;

/**
 * Created by Viviano on 2/2/2016.
 */
public interface Theme extends PickerItem {

    //TODO: make everything take in a NovaKeyDimens object maybe

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
     * used to set the three colors of the theme
     *
     * @param primary primary color
     * @param accent accent color
     * @param contrast contrast color
     */
    void setColors(int primary, int accent, int contrast);

    /**
     * @return the primary color of the theme, usually the background color
     */
    int primaryColor();

    /**
     * @return the accent color of the theme, usually for lines
     */
    int accentColor();

    /**
     * @return the contrast color of the theme, usually for keys & icons
     */
    int contrastColor();


    int buttonColor();//TODO: may remove

    /**
     * sets whether theme should use shadows to appear 3D
     * @param is3D true if theme should appear 3D
     */
    void set3D(boolean is3D);

    /**
     * @returns whether this theme has 3d mode set
     */
    boolean is3D();

    /**
     * Draw background of keyboard
     *
     * @param l left position
     * @param t top position
     * @param rt right position
     * @param b bottom position
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawBackground(float l, float t, float rt, float b, float x, float y,
                        float r, float sr, Canvas canvas);

    /**
     * Draw the center of the board
     *
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawBoard(float x, float y, float r, float sr, Canvas canvas);

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
     * @param cursorCode state of the cursor
     * @param x center X position
     * @param y center Y position
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawCursorIcon(int cursorCode, float x, float y, float sr, Canvas canvas);

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

    /**
     * Set this theme's current package to have it auto color itself,
     * if pack is a valid packaged
     *
     * @param pack package to obtain color from
     */
    void setPackage(String pack);
}
