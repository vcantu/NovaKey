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
import viviano.cantu.novakey.core.view.themes.background.FlatBackgroundTheme;
import viviano.cantu.novakey.core.view.themes.board.BaseTheme;
import viviano.cantu.novakey.core.view.themes.board.BoardTheme;
import viviano.cantu.novakey.core.view.themes.button.BaseButtonTheme;
import viviano.cantu.novakey.core.view.themes.button.ButtonTheme;

/**
 * Created by Viviano on 8/14/2016.
 */
public class BaseMasterTheme implements MasterTheme {

    private boolean mIs3d;
    private int mPrimiary, mAccent, mContrast;

    private BoardTheme mBoard;
    private BackgroundTheme mBackground;
    private ButtonTheme mButton;


    public BaseMasterTheme() {
        setBoardTheme(new BaseTheme());
        setBackgroundTheme(new FlatBackgroundTheme());
        setButtonTheme(new BaseButtonTheme());

        mIs3d = false;
        mPrimiary = 0xFF616161;
        mAccent = 0xFFF5F5F5;
        mContrast = 0xFFF5F5F5;
    }


    /**
     * sets whether theme should use shadows to appear 3D
     *
     * @param is3D true if theme should appear 3D
     */
    @Override
    public MasterTheme set3D(boolean is3D) {
        mIs3d = is3D;
        return this;
    }


    /**
     * @returns whether this theme has 3d mode set
     */
    @Override
    public boolean is3D() {
        return mIs3d;
    }


    /**
     * Sets the colors of this theme given an array of colors
     *
     * @param primary  primary color of the theme
     * @param accent   accent color of the theme
     * @param contrast contrast color of the theme
     */
    @Override
    public MasterTheme setColors(int primary, int accent, int contrast) {
        mPrimiary = primary;
        mAccent = accent;
        mContrast = contrast;
        return this;
    }


    /**
     * Sets the colors of this theme given an app package.
     * This method gets the colors of the app using the package and sets them.
     *
     * @param appPackage app to get colors from
     */
    @Override
    public MasterTheme setPackage(String appPackage) {
        AppTheme app = AppTheme.fromPk(appPackage);
        if (app == null) {
            //App not supported
            //maybe ask user to support this app
            //TODO: AppTheme.promptUser()
            return this;
        }
        setColors(app.color1, app.color2, app.color3);
        return this;
    }


    /**
     * @return primary color of this theme
     */
    @Override
    public int getPrimaryColor() {
        return mPrimiary;
    }


    @Override
    public void setPrimaryColor(int color) {
        mPrimiary = color;
    }


    /**
     * @return accent color of this theme
     */
    @Override
    public int getAccentColor() {
        return mAccent;
    }


    @Override
    public void setAccentColor(int color) {
        mAccent = color;
    }


    /**
     * @return contrast color of this theme
     */
    @Override
    public int getContrastColor() {
        return mContrast;
    }


    @Override
    public void setContrastColor(int color) {
        mContrast = color;
    }


    /**
     * Set the board theme
     *
     * @param boardTheme board theme to set
     */
    @Override
    public MasterTheme setBoardTheme(BoardTheme boardTheme) {
        mBoard = boardTheme;
        mBoard.setParent(this);
        return this;
    }


    /**
     * @return this master theme's board theme
     */
    @Override
    public BoardTheme getBoardTheme() {
        return mBoard;
    }


    /**
     * Sets the button theme
     *
     * @param buttonTheme button theme to set
     */
    @Override
    public MasterTheme setButtonTheme(ButtonTheme buttonTheme) {
        mButton = buttonTheme;
        mButton.setParent(this);
        return this;
    }


    /**
     * @return this master theme's button theme
     */
    @Override
    public ButtonTheme getButtonTheme() {
        return mButton;
    }


    /**
     * Sets the background theme
     *
     * @param backgroundTheme background theme to set
     */
    @Override
    public MasterTheme setBackgroundTheme(BackgroundTheme backgroundTheme) {
        mBackground = backgroundTheme;
        mBackground.setParent(this);
        return this;
    }


    /**
     * @return this master theme's background theme
     */
    @Override
    public BackgroundTheme getBackgroundTheme() {
        return mBackground;
    }
}
