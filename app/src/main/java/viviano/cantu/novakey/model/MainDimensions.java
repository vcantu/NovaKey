package viviano.cantu.novakey.model;

/**
 * Created by viviano on 11/26/2017.
 *
 * Main dimensions of the keyboard
 */

public class MainDimensions extends Dimensions{

    public static String
            X = "x",
            Y = "y",
            RADIUS = "radius",
            SMALL_RADIUS = "smallRadius",
            PADDING = "padding",
            WIDTH = "width",
            HEIGHT = "height";

    public MainDimensions(float x, float y, float radius, float smallRadius,
                          float padding, int width, int height) {
        set(X, x);
        set(Y, y);
        set(RADIUS, radius);
        set(SMALL_RADIUS, smallRadius);
        set(PADDING, padding);
        set(WIDTH, width);
        set(HEIGHT, height);
    }

    public float getX() {
        return getF(X);
    }

    public void setX(float x) {
        set(X, x);
    }

    public float getY() {
        return getF(Y);
    }

    public void setY(float y) {
        set(Y, y);
    }

    public float getRadius() {
        return getF(RADIUS);
    }

    public void setRadius(float radius) {
       set(RADIUS, radius);
    }

    public float getSmallRadius() {
        return getF(SMALL_RADIUS);
    }

    public void setSmallRadius(float smallRadius) {
        set(SMALL_RADIUS, smallRadius);
    }

    public float getPadding() {
        return getF(PADDING);
    }

    public void setPadding(float padding) {
        set(PADDING, padding);
    }

    public int getWidth() {
        return getI(WIDTH);
    }

    public void setWidth(int width) {
        set(WIDTH, width);
    }

    public int getHeight() {
        return getI(HEIGHT);
    }

    public void setHeight(int height) {
        set(HEIGHT, height);
    }
}
