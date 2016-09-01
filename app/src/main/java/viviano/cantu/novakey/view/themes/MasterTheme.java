package viviano.cantu.novakey.view.themes;

import viviano.cantu.novakey.view.themes.background.BackgroundTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.view.themes.button.ButtonTheme;

/**
 * Created by Viviano on 8/14/2016.
 *
 * Master theme which holds information which all of it's child themes will need to draw
 */
public interface MasterTheme {


    /**
     * sets whether theme should use shadows to appear 3D
     * @param is3D true if theme should appear 3D
     */
    MasterTheme set3D(boolean is3D);

    /**
     * @returns whether this theme has 3d mode set
     */
    boolean is3D();

    /**
     * Sets the colors of this theme given an array of colors
     *  @param primary primary color of the theme
     * @param accent accent color of the theme
     * @param contrast contrast color of the theme
     */
    MasterTheme setColors(int primary, int accent, int contrast);

    /**
     * Sets the colors of this theme given an app package.
     * This method gets the colors of the app using the package and sets them.
     *
     * @param appPackage app to get colors from
     */
    MasterTheme setPackage(String appPackage);

    /**
     * @return primary color of this theme
     */
    int getPrimaryColor();

    /**
     * @param color sets this to the primary color
     */
    void setPrimaryColor(int color);

    /**
     * @return accent color of this theme
     */
    int getAccentColor();

    /**
     * @param color sets this to the accent color
     */
    void setAccentColor(int color);

    /**
     * @return contrast color of this theme
     */
    int getContrastColor();

    /**
     * @param color sets this to the contrast color
     */
    void setContrastColor(int color);

    /**
     * Set the board theme
     *
     * @param boardTheme board theme to set
     */
    MasterTheme setBoardTheme(BoardTheme boardTheme);

    /**
     * @return this master theme's board theme
     */
    BoardTheme getBoardTheme();

    /**
     * Sets the button theme
     *
     * @param buttonTheme button theme to set
     */
    MasterTheme setButtonTheme(ButtonTheme buttonTheme);

    /**
     * @return this master theme's button theme
     */
    ButtonTheme getButtonTheme();

    /**
     * Sets the background theme
     *
     * @param backgroundTheme background theme to set
     */
    MasterTheme setBackgroundTheme(BackgroundTheme backgroundTheme);

    /**
     * @return this master theme's background theme
     */
    BackgroundTheme getBackgroundTheme();
}
