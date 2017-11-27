package viviano.cantu.novakey.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viviano on 11/26/2017.
 *
 * Dimensions used by an element to draw
 */

public abstract class Dimensions {

    private Map<String, Object> mValues;

    public void set(String key, Object o) {
        if (key == null || o == null)
            return;
        if (mValues == null)
            mValues = new HashMap<>();
        mValues.put(key, o);
    }

    public Object get(String key) {
        if (mValues == null || key == null)
            return null;
        return mValues.get(key);
    }

    public int getI(String key) {
        if (mValues == null || key == null)
            return 0;
        return (int)mValues.get(key);
    }

    public float getF(String key) {
        if (mValues == null || key == null)
            return 0;
        return (float)mValues.get(key);
    }
}
