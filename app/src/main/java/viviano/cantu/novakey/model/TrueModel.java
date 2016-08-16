package viviano.cantu.novakey.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.elements.menus.Menu;
import viviano.cantu.novakey.model.keyboards.KeyLayout;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.model.loaders.Loader;
import viviano.cantu.novakey.model.loaders.ThemeSharedPrefLoader;
import viviano.cantu.novakey.model.properties.ButtonProperties;
import viviano.cantu.novakey.model.properties.KeyProperties;
import viviano.cantu.novakey.model.states.InputState;
import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.model.states.UserState;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.view.themes.ThemeFactory;

/**
 * Model that pulls data from settings and defaults
 *
 * and holds the true state of the keyboard
 *
 * Created by Viviano on 6/7/2016.
 */
public class TrueModel implements NovaKeyModel {

    //Shared preferences
    private final SharedPreferences mSharedPref;
    private final Context mContext;

    //Loaders
    private final Loader<List<Element>> mElementLoader;
    private final Loader<MasterTheme> mThemeLoader;

    //States
    private ShiftState mShiftState;
    private UserState mUserState;
    private int mCursorMode = 0;
    private InputState mInputState;

    //Keyboard
    private int mKeyboardCode = 0;
    private List<KeyLayout> mKeyLayouts;

    //Menu TODO: set menu
    private Menu mMenu;

    public TrueModel(Context context) {
        this.mContext = context;
        this.mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        this.mShiftState = ShiftState.UPPERCASE;
        this.mUserState = UserState.TYPING;

        mKeyLayouts = new ArrayList<>();
        //TODO: read this from sharedPref
        mKeyLayouts.add(KeyLayout.get("English"));

        //loaders
        mThemeLoader = new ThemeSharedPrefLoader(mSharedPref);
        mElementLoader = null;//TODO: dis shite
    }

    /**
     * @param model copies all the parameters of this model
     */
    @Override
    public void sync(DrawModel model) {
        throw new IllegalAccessError("True model cannot be synced");
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
        //TODO: for now it's based on the radius and padding
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
     * @return the current input state
     */
    @Override
    public InputState getInputState() {
        return mInputState;
    }

    /**
     * Uses the given editor info to update the input state
     *
     * @param editorInfo info used to generate input state
     */
    @Override
    public void updateInputState(EditorInfo editorInfo) {
        mInputState = new InputState(editorInfo);
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
     * @param properties sets the KeyProperties of this model to this
     */
    @Override
    public void setKeyProperties(List<KeyProperties> properties) {
        //TODO: save to shared pref
    }

    /**
     * @return the button properties of this model
     */
    @Override
    public List<ButtonProperties> getButtonProperties() {
        return null;
    }

    /**
     * @param properties sets the ButtonProperties of this model
     */
    @Override
    public void setButtonProperties(List<ButtonProperties> properties) {

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
        return mCursorMode;
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
        mCursorMode = cursorMode;
    }

    /**
     * @return this model's theme
     */
    @Override
    public MasterTheme getTheme() {
        return mThemeLoader.load();
    }

    /**
     * @param theme theme to set
     */
    @Override
    public void setTheme(MasterTheme theme) {
        mThemeLoader.save(theme);
    }

    /**
     * @return the current menu or null if there is none
     */
    @Override
    public Menu getMenu() {
        return mMenu;
    }


    /**
     * @param menu menu to set
     */
    @Override
    public void setMenu(Menu menu) {
        mMenu = menu;
    }


}
