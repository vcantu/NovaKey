package viviano.cantu.novakey.model;

import java.util.List;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.model.keyboards.KeyProperties;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 6/10/2016.
 */
public interface DrawModel {

    /**
     * @param model copies all the parameters of this model
     */
    void sync(DrawModel model);

    /**
     * @return the width of the board
     */
    int getWidth();

    /**
     * @param width width to set
     */
    void setWidth(int width);

    /**
     * @return the height of the board
     */
    int getHeight();

    /**
     * @param height height to set
     */
    void setHeight(int height);

    /**
     * @return the center X position of the board
     */
    float getX();

    /**
     * @param x x coord to set
     */
    void setX(float x);

    /**
     * @return the center Y position of the board
     */
    float getY();

    /**
     * @param y y coord to set
     */
    void setY(float y);

    /**
     * @return the radius of the board
     */
    float getRadius();

    /**
     * @param radius radius to set
     */
    void setRadius(float radius);

    /**
     * @return the small radius of the board
     */
    float getSmallRadius();

    /**
     * @param smallRadius radius to set
     */
    void setSmallRadius(float smallRadius);

    /**
     * @return the top vertical padding
     */
    float getPadding();

    /**
     * @param padding padding to set
     */
    void setPadding(float padding);

    /**
     * @return the key properties of this model
     */
    List<KeyProperties> getKeyProperties();

    /**
     * This method will use the given changer to alter
     * the keyProperties within it
     *
     * @param changer interface which changes the keyProperties
     *                with it's change() method
     */
    void changeKeyProperties(KeyChanger changer);

    /**
     * @param properties sets the KeyProperties of this model to this
     */
    void setKeyProperties(List<KeyProperties> properties);

    /**
     * Updates the theme's color based on the package
     *
     * @param pkg package which determines the color of the theme
     */
    void updateTheme(String pkg);

    /**
     * @return this model's theme
     */
    Theme getTheme();

    /**
     * @param theme theme to set
     */
    void setTheme(Theme theme);

    /**
     * Changes the keys depending on the change method
     */
    public interface KeyChanger {
        /**
         * Will be iterated through all keys
         *
         * @param keyProps current properties in the iteration
         */
        void change(KeyProperties keyProps);
    }
}
