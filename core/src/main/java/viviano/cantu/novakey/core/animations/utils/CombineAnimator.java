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

package viviano.cantu.novakey.core.animations.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viviano on 9/2/2016.
 */
public class CombineAnimator<T> implements Animator<T> {

    private final List<Animator<T>> mList;


    public CombineAnimator(List<Animator<T>> list) {
        mList = list;
    }


    public CombineAnimator() {
        mList = new ArrayList<>();
    }


    CombineAnimator<T> add(Animator<T> animator) {
        mList.add(animator);
        return this;
    }


    /**
     * Takes in a T and a fraction and updates the T according
     * to the fraction
     *
     * @param t        object to update
     * @param fraction percentage of animation where 0 is the start & 1 is the end
     */
    @Override
    public void update(T t, float fraction) {
        for (Animator<T> a : mList) {
            a.update(t, fraction);
        }
    }
}
