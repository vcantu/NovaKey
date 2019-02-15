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

package viviano.cantu.novakey.core.model.factories;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Viviano on 8/27/2016.
 * <p>
 * Holds a list of "Constructors" which can be used to instantiate new objects
 * using the construct(Class) method
 */
public abstract class InstanceList<T> implements Iterator<T>, Iterable<T> {

    private final Map<Integer, Class> mMap;
    private Iterator<Map.Entry<Integer, Class>> mIter;


    public InstanceList() {
        mMap = new LinkedHashMap<>();
        build(mMap);
    }


    /**
     * Called when constructing, use this to build the
     *
     * @param map
     */
    protected abstract void build(Map<Integer, Class> map);


    public int size() {
        return mMap.size();
    }


    private T construct(Class clazz) {
        try {
            return (T) clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    public T getValue(int key) {
        try {
            return construct(mMap.get(key));
        } catch (Exception e) {
            return null;
        }
    }


    public Integer getKey(Class clazz) {
        for (Map.Entry<Integer, Class> e : mMap.entrySet()) {
            if (e.getValue() == clazz) {
                return e.getKey();
            }
        }
        return null;
    }


    /**
     * Returns an {@link Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<T> iterator() {
        mIter = mMap.entrySet().iterator();
        return this;
    }


    /**
     * Returns true if there is at least one more element, false otherwise.
     *
     * @see #next
     */
    @Override
    public boolean hasNext() {
        return mIter.hasNext();
    }


    /**
     * Returns the next object and advances the iterator.
     *
     * @return the next object.
     * @throws NoSuchElementException if there are no more elements.
     * @see #hasNext
     */
    @Override
    public T next() {
        return construct(mIter.next().getValue());
    }


    /**
     * Removes the last object returned by {@code next} from the collection.
     * This method can only be called once between each call to {@code next}.
     *
     * @throws UnsupportedOperationException if removing is not supported by the collection being
     *                                       iterated.
     * @throws IllegalStateException         if {@code next} has not been called, or {@code remove} has
     *                                       already been called after the last call to {@code next}.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("cannot remove items");
    }
}
