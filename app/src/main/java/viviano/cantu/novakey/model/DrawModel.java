package viviano.cantu.novakey.model;

import java.util.List;

import viviano.cantu.novakey.model.properties.ButtonProperties;
import viviano.cantu.novakey.model.properties.KeyProperties;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

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
     * @param properties sets the KeyProperties of this model to this
     */
    void setKeyProperties(List<KeyProperties> properties);

    /**
     * @return the button properties of this model
     */
    List<ButtonProperties> getButtonProperties();

    /**
     * @param properties sets the ButtonProperties of this model
     */
    void setButtonProperties(List<ButtonProperties> properties);

    /**
     * @return this model's theme
     */
    MasterTheme getTheme();

    /**
     * @param theme theme to set
     */
    void setTheme(MasterTheme theme);
}
