/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.core.view.themes;

import viviano.cantu.novakey.core.view.themes.background.BackgroundTheme;
import viviano.cantu.novakey.core.view.themes.board.BoardTheme;
import viviano.cantu.novakey.core.view.themes.button.ButtonTheme;

/**
 * Created by Viviano on 8/14/2016.
 * <p>
 * Master theme which holds information which all of it's child themes will need to draw
 */
public interface MasterTheme {


    /**
     * sets whether theme should use shadows to appear 3D
     *
     * @param is3D true if theme should appear 3D
     */
    MasterTheme set3D(boolean is3D);


    /**
     * @returns whether this theme has 3d mode set
     */
    boolean is3D();


    /**
     * Sets the colors of this theme given an array of colors
     *
     * @param primary  primary color of the theme
     * @param accent   accent color of the theme
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
