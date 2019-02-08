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

package viviano.cantu.novakey.elements.keyboards;

import viviano.cantu.novakey.animations.utils.Animator;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 9/2/2016.
 * <p>
 * Will change the size of the key from the given factors
 */
public class KeySizeAnimator implements Animator<Key> {

    private final float mStart, mEnd;


    public KeySizeAnimator(float start, float end) {
        mStart = start;
        mEnd = end;
    }


    /**
     * Takes in a T and a fraction and updates the T according
     * to the fraction
     *
     * @param key      object to update
     * @param fraction percentage of animation where 0 is the start & 1 is the end
     */
    @Override
    public void update(Key key, float fraction) {
        key.setSize(Util.fromFrac(mStart, mEnd, fraction));
    }
}
