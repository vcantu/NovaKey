/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viviano on 11/26/2017.
 * <p>
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
        return (int) mValues.get(key);
    }


    public float getF(String key) {
        if (mValues == null || key == null)
            return 0;
        return (float) mValues.get(key);
    }
}
