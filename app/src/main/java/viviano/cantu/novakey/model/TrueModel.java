package viviano.cantu.novakey.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.model.keyboards.Key;
import viviano.cantu.novakey.model.keyboards.KeyLayout;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.menus.OnUpMenu;
import viviano.cantu.novakey.model.keyboards.KeyProperties;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.themes.Theme;

/**
 * Model that pulls data from settings and defaults
 *
 * and holds the true state of the keyboard
 *
 * Created by Viviano on 6/7/2016.
 */
public class TrueModel implements NovaKeyModel {

    private final SharedPreferences mSharedPref;
    private final Context mContext;

    private ShiftState mShiftState;
    private UserState mUserState;
    private int mCursor = 0;

    private int mKeyboardCode = 0;
    private List<KeyLayout> mKeyLayouts;

    private InfiniteMenu mInfiniteMenu;
    private OnUpMenu mOnUpMenu;

    public TrueModel(Context context) {
        this.mContext = context;
        this.mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        this.mShiftState = ShiftState.UPPERCASE;
        this.mUserState = UserState.TYPING;

        mKeyLayouts = new ArrayList<>();
        //TODO: read this from sharedPref
        mKeyLayouts.add(KeyLayout.get("English"));
    }

    /**
     * @param model copies all the parameters of this model
     */
    @Override
    public void sync(DrawModel model) {

    }

    /**
     * @return the width of the board
     */
    @Override
    public int getWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * @param width width to set
     */
    @Override
    public void setWidth(int width) {
        //TODO: for now it's based on the witdh of the screen
    }

    /**
     * @return the height of the board
     */
    @Override
    public int getHeight() {
        float r = getRadius();
        return (int)(mSharedPref.getFloat("y" + (isLandscape() ? "_land" : ""), r)
                + r + getPadding());
    }

    /**
     * @param height height to set
     */
    @Override
    public void setHeight(int height) {
        //TODO: for now it's based on the radius
    }

    /**
     * @return the center X position of the board
     */
    @Override
    public float getX() {
        return mSharedPref.getFloat("x" + (isLandscape() ? "_land" : ""),
                getWidth() / 2);
    }

    /**
     * @param x x coord to set
     */
    @Override
    public void setX(float x) {
        mSharedPref.edit()
                .putFloat("x" + (isLandscape() ? "_land" : ""), x)
                .commit();
    }

    /**
     * @return the center Y position of the board
     */
    @Override
    public float getY() {
        return getRadius() + getPadding();
    }

    /**
     * @param y y coord to set
     */
    @Override
    public void setY(float y) {
        mSharedPref.edit()
                .putFloat("y" + (isLandscape() ? "_land" : ""), y)
                .commit();
    }

    /**
     * @return the radius of the board
     */
    @Override
    public float getRadius() {
        return this.mSharedPref.getFloat("size" + (isLandscape() ? "_land" : ""),
                mContext.getResources().getDimension(R.dimen.default_radius));
    }

    /**
     * @param radius radius to set
     */
    @Override
    public void setRadius(float radius) {
        mSharedPref.edit()
                .putFloat("size" + (isLandscape() ? "_land" : ""), radius)
                .commit();
    }

    /**
     * @return the small radius of the board
     */
    @Override
    public float getSmallRadius() {
        return getRadius() / mSharedPref.getFloat("smallRadius", 3);
    }

    /**
     * @param smallRadius radius to set
     */
    @Override
    public void setSmallRadius(float smallRadius) {
        mSharedPref.edit()
                .putFloat("smallRadius", smallRadius)
                .commit();
    }

    /**
     * @return the top vertical padding
     */
    @Override
    public float getPadding() {
        return mSharedPref.getFloat("padd" + (isLandscape() ? "_land" : ""),
                mContext.getResources().getDimension(R.dimen.default_padding));
    }

    /**
     * @param padding padding to set
     */
    @Override
    public void setPadding(float padding) {
        mSharedPref.edit()
                .putFloat("padd" + (isLandscape() ? "_land" : ""), padding)
                .commit();
    }

    /**
     * @return the current orientation state of the phone
     */
    @Override
    public boolean isLandscape() {
        return mContext.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * @return the key layout that should be drawn
     */
    @Override
    public KeyLayout getKeyboard() {
        switch (getKeyboardCode()) {
            case KeyLayout.SYMBOLS:
                return KeyLayout.get("Symbols");
            case KeyLayout.PUNCTUATION:
                return KeyLayout.get("Punctuation");
            default:
                return mKeyLayouts.get(getKeyboardCode());
        }
    }

    /**
     * @return the code/index of the current keyboard
     */
    @Override
    public int getKeyboardCode() {
        return mKeyboardCode;
    }

    /**
     * @param code key layout code
     */
    @Override
    public void setKeyboard(int code) {
        this.mKeyboardCode = code;
    }

    /**
     * @return the key properties of this model
     */
    @Override
    public List<KeyProperties> getKeyProperties() {
        //TODO: pull from shared pref
        return getKeyboard().properties;
    }

    /**
     * WARNING: calling this method will alter the default KeyLayout
     * properties, Only change for user preferences like
     * for keys to be twice as big and such
     *
     * This method will use the given changer to alter
     * the keyProperties within it
     *
     * @param changer interface which changes the keyProperties
     *                with it's change() method
     */
    @Override
    public void changeKeyProperties(KeyChanger changer) {
        for (KeyProperties kp : getKeyProperties()) {
            changer.change(kp);
        }
    }

    /**
     * @param properties sets the KeyProperties of this model to this
     */
    @Override
    public void setKeyProperties(List<KeyProperties> properties) {
        //NOT SUPPORTED BY THE TRUE MODEL
    }

    /**
     * @return the current shift state of the keyboard
     */
    @Override
    public ShiftState getShiftState() {
        return mShiftState;
    }

    /**
     * @param shiftState the shiftState to set the keyboard to
     */
    @Override
    public void setShiftState(ShiftState shiftState) {
        this.mShiftState = shiftState;
    }

    /**
     * @return the current general action the user is doing
     */
    @Override
    public UserState getUserState() {
        return mUserState;
    }

    /**
     * @param userState the user state to set
     */
    @Override
    public void setUserState(UserState userState) {
        mUserState = userState;
    }

    /**
     * if cursor mode is 0 both the left and the right are moving,
     * if cursor mode is -1 the left only is moving,
     * if cursor mdoe is 1 the right only is moving
     *
     * @return current cursor mode
     */
    @Override
    public int getCursorMode() {
        return mCursor;
    }

    /**
     * if cursor mode is 0 both the left and the right are moving,
     * if cursor mode is -1 the left only is moving,
     * if cursor mdoe is 1 the right only is moving
     *
     * @param cursorMode cursor mode to set
     * @throws IllegalArgumentException if the param is outside the range [-1, 1]
     */
    @Override
    public void setCursorMode(int cursorMode) {
        if (cursorMode < -1 || cursorMode > 1)
            throw new IllegalArgumentException(cursorMode + " is outside the range [-1, 1]");
        mCursor = cursorMode;
    }

    /**
     * Updates the theme's color based on the package
     *
     * @param pkg current package the input method is on which
     *            determines the color of the theme
     */
    @Override
    public void updateTheme(String pkg) {
        Theme t = Settings.theme;
        if (Settings.autoColor)
            t.setPackage(pkg);
    }

    /**
     * @return this model's theme
     */
    @Override
    public Theme getTheme() {
        return Settings.theme;
    }

    /**
     * @param theme theme to set
     */
    @Override
    public void setTheme(Theme theme) {
        //TODO: save theme to shared pref
    }

    /**
     * @return the current infinite menu or null if there is none
     */
    @Override
    public InfiniteMenu getInfiniteMenu() {
        return mInfiniteMenu;
    }

    /**
     * @param infiniteMenu menu to set
     */
    @Override
    public void setInfiniteMenu(InfiniteMenu infiniteMenu) {
        mInfiniteMenu = infiniteMenu;
    }

    /**
     * @return the current on up menu or null if there is none
     */
    @Override
    public OnUpMenu getOnUpMenu() {
        return mOnUpMenu;
    }

    /**
     * @param onUpMenu menu to set
     */
    @Override
    public void setOnUpMenu(OnUpMenu onUpMenu) {
        mOnUpMenu = onUpMenu;
    }


}
