package viviano.cantu.novakey.elements;

import viviano.cantu.novakey.model.properties.Properties;

/**
 * Created by Viviano on 6/26/2016.
 */
public interface HasProperties<T extends Properties> {

    /**
     * @return this class's properties
     */
    T getProperties();

    /**
     * Sets this class's properties
     * @param properties properties to set
     */
    void setProperties(T properties);
}
