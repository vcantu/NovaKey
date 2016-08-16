package viviano.cantu.novakey.view.themes;

import viviano.cantu.novakey.view.themes.background.BackgroundTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.view.themes.button.ButtonTheme;

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
        //TODO: maybe builder
    }

    /**
     * sets whether theme should use shadows to appear 3D
     *
     * @param is3D true if theme should appear 3D
     */
    @Override
    public void set3D(boolean is3D) {
        mIs3d = is3D;
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
    public void setColors(int primary, int accent, int contrast) {
        mPrimiary = primary;
        mAccent = accent;
        mContrast = contrast;
    }

    /**
     * Sets the colors of this theme given an app package.
     * This method gets the colors of the app using the package and sets them.
     *
     * @param appPackage app to get colors from
     */
    @Override
    public void setColors(String appPackage) {
        AppTheme app = AppTheme.fromPk(appPackage);
        if (app == null) {
            //App not supported
            //maybe ask user to support this app
            //TODO: AppTheme.promptUser()
            return;
        }
        setColors(app.color1, app.color2, app.color3);
    }

    /**
     * @return primary color of this theme
     */
    @Override
    public int getPrimaryColor() {
        return mPrimiary;
    }

    /**
     * @return accent color of this theme
     */
    @Override
    public int getAccentColor() {
        return mAccent;
    }

    /**
     * @return contrast color of this theme
     */
    @Override
    public int getContrastColor() {
        return mContrast;
    }

    /**
     * Set the board theme
     *
     * @param boardTheme board theme to set
     */
    @Override
    public void setBoardTheme(BoardTheme boardTheme) {
        mBoard = boardTheme;
        mBoard.setParent(this);
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
    public void setButtonTheme(ButtonTheme buttonTheme) {
        mButton = buttonTheme;
        mButton.setParent(this);
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
    public void setBackgroundTheme(BackgroundTheme backgroundTheme) {
        mBackground = backgroundTheme;
        mBackground.setParent(this);
    }

    /**
     * @return this master theme's background theme
     */
    @Override
    public BackgroundTheme getBackgroundTheme() {
        return mBackground;
    }
}
