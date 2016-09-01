package viviano.cantu.novakey.model.states;

import android.view.inputmethod.EditorInfo;

import java.util.List;

import viviano.cantu.novakey.elements.DefaultElementManager;
import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.elements.ElementManager;
import viviano.cantu.novakey.elements.keyboards.Keyboard;
import viviano.cantu.novakey.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.TrueModel;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 8/27/2016.
 *
 * Base model just stores all of the storeable fields
 */
public abstract class BaseModel implements Model {

    private UpdateListener mListener;

    private final TrueModel mTrueModel;

    //Dimensions
    private int mWidth, mHeight;
    private float mX, mY, mRadius, mSmallRadius, mPadding;

    //Theme
    private MasterTheme mTheme;

    //States
    private ShiftState mShiftState;
    private UserState mUserState;
    private int mCursorMode = 0;
    private InputState mInputState;

    //Keyboard
    private int mKeyboardCode = 0;
    private final Keyboards mKeyboards;

    //Elements
    private final ElementManager mElements;


    public BaseModel(TrueModel model) {
        mTrueModel = model;

        mWidth = model.getWidth();
        mHeight = model.getHeight();
        mX = model.getX();
        mY = model.getY();
        mRadius = model.getRadius();
        mSmallRadius = model.getRadius();
        mPadding = model.getPadding();

        mTheme = model.getTheme();

        mShiftState = ShiftState.UPPERCASE;
        mUserState = UserState.TYPING;
        //Input State determined during start

        mKeyboards = model.getKeyboards();

        mElements = new DefaultElementManager(mKeyboards.get(Keyboards.DEFAULT));
    }

    /**
     * @return the width of the board
     */
    @Override
    public int getWidth() {
        return mWidth;
    }

    /**
     * @param width width to set
     */
    @Override
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * @return the height of the board
     */
    @Override
    public int getHeight() {
        return mHeight;
    }

    /**
     * @param height height to set
     */
    @Override
    public void setHeight(int height) {
        mHeight = height;
    }

    /**
     * @return the center X position of the board
     */
    @Override
    public float getX() {
        return mX;
    }

    /**
     * @param x x coord to set
     */
    @Override
    public void setX(float x) {
        mX = x;
    }

    /**
     * @return the center Y position of the board
     */
    @Override
    public float getY() {
        return mY;
    }

    /**
     * @param y y coord to set
     */
    @Override
    public void setY(float y) {
        mY = y;
    }

    /**
     * @return the radius of the board
     */
    @Override
    public float getRadius() {
        return mRadius;
    }

    /**
     * @param radius radius to set
     */
    @Override
    public void setRadius(float radius) {
        mRadius = radius;
    }

    /**
     * @return the small radius of the board
     */
    @Override
    public float getSmallRadius() {
        return mSmallRadius;
    }

    /**
     * @param smallRadius radius to set
     */
    @Override
    public void setSmallRadius(float smallRadius) {
        mSmallRadius = smallRadius;
    }

    /**
     * @return the top vertical padding
     */
    @Override
    public float getPadding() {
        return mPadding;
    }

    /**
     * @param padding padding to set
     */
    @Override
    public void setPadding(float padding) {
        mPadding = padding;
    }

    /**
     * @return this model's theme
     */
    @Override
    public MasterTheme getTheme() {
        return mTheme;
    }

    /**
     * @param theme theme to set
     */
    @Override
    public void setTheme(MasterTheme theme) {
        mTheme = theme;
    }

    /**
     * @return the current list of elements
     */
    @Override
    public List<Element> getElements() {
        return mElements.getElements();
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
    public void onStart(EditorInfo editorInfo) {
        mInputState = new InputState(editorInfo);

        //reads theme from preferences & colors according to the app
        setTheme(mTrueModel.getTheme().setPackage(editorInfo.packageName));

        setUserState(UserState.TYPING);
        switch (mInputState.getType()) {
            default:
            case TEXT:
                setKeyboard(Keyboards.DEFAULT);
                break;
            case NUMBER:
                setKeyboard(Keyboards.PUNCTUATION);
                break;
            case PHONE:
                setKeyboard(Keyboards.PUNCTUATION);
                break;
            case DATETIME:
                setKeyboard(Keyboards.PUNCTUATION);
                break;
        }

        //TODO: update shiftstate
    }

    /**
     * @return the key layout that should be drawn
     */
    @Override
    public Keyboard getKeyboard() {
        return mKeyboards.get(getKeyboardCode());
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
     * Set this model's update listener
     *
     * @param listener called when the model updates
     */
    @Override
    public void setUpdateListener(UpdateListener listener) {
        this.mListener = listener;
    }

    protected void update() {
        if (mListener != null)
            mListener.onUpdate();
    }
}
