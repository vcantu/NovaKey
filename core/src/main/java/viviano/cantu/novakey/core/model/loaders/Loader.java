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

package viviano.cantu.novakey.core.model.loaders;

/**
 * Created by Viviano on 8/13/2016.
 * <p>
 * Saves and loads data into shared preferences
 */
public interface Loader<T> {

    /**
     * Loads the element from where ever this interface saved it from
     *
     * @return a new loaded object
     */
    T load();


    /**
     * Saves the element to be loaded later
     *
     * @param t object to be saved
     */
    void save(T t);
}
