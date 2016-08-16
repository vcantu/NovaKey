package viviano.cantu.novakey.model;

import java.util.List;

import viviano.cantu.novakey.model.properties.ButtonProperties;
import viviano.cantu.novakey.model.properties.KeyProperties;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Stores all dimensions needed and draws according to
 * Created by Viviano on 6/10/2016.
 */
public class MainDrawModel implements DrawModel {

    private int mWidth, mHeight;
    private float mX, mY, mRadius, mSmallRadius, mPadding;

    private List<KeyProperties> mKeyProps;
    private List<ButtonProperties> mButtonProps;
    private MasterTheme mTheme;

    public MainDrawModel(DrawModel model) {
        sync(model);
    }

    /**
     * @param model copies all the parameters of this model
     */
    @Override
    public void sync(DrawModel model) {
        setWidth(model.getWidth());
        setHeight(model.getHeight());
        setX(model.getX());
        setY(model.getY());
        setRadius(model.getRadius());
        setSmallRadius(model.getSmallRadius());
        setPadding(model.getPadding());
        setKeyProperties(model.getKeyProperties());
        setButtonProperties(model.getButtonProperties());
        setTheme(model.getTheme());
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
     * @return the key properties of this model
     */
    @Override
    public List<KeyProperties> getKeyProperties() {
        return mKeyProps;
    }

    /**
     * @param properties sets the KeyProperties of this model to this
     */
    @Override
    public void setKeyProperties(List<KeyProperties> properties) {
        mKeyProps = properties;
    }

    /**
     * @return the button properties of this model
     */
    @Override
    public List<ButtonProperties> getButtonProperties() {
        return mButtonProps;
    }

    /**
     * @param properties sets the ButtonProperties of this model
     */
    @Override
    public void setButtonProperties(List<ButtonProperties> properties) {
        mButtonProps = properties;
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
}
