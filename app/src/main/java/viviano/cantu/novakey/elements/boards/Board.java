package viviano.cantu.novakey.elements.boards;


import java.util.Map;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.model.keyboards.Key;
import viviano.cantu.novakey.model.properties.Properties;

/**
 * Created by Viviano on 6/18/2016.
 */
public interface Board extends Element {

    /**
     * @param x x position
     * @param y y position
     * @return returns the current area based on the given coordinates
     */
    int getArea(float x, float y);

    /**
     * @param x x position
     * @param y y position
     * @return returns the current rotational sector based on the given coordinates
     */
    int getSector(float x, float y);
}
