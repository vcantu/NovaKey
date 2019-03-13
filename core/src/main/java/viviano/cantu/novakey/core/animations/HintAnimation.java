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

package viviano.cantu.novakey.core.animations;

import android.animation.TimeInterpolator;
import android.view.animation.AccelerateInterpolator;

import viviano.cantu.novakey.core.animations.utils.Animator;
import viviano.cantu.novakey.core.elements.keyboards.Key;

/**
 * Created by Viviano on 11/15/2015.
 */
public class HintAnimation extends CharAnimation {

    private final TimeInterpolator mInterpolator = new AccelerateInterpolator();
    private final int mArea;


    public HintAnimation(int area, long duration) {
        super(-1, duration);
        mArea = area;
    }


    public HintAnimation(int area) {
        this(area, 100);
    }


    /**
     * Will be called when building the animation to set this particular key's
     * interpolator
     *
     * @param k key whose interpolator you wish to set
     * @return an interpolator to set
     */
    @Override
    protected TimeInterpolator getInterpolatorFor(Key k) {
        return mInterpolator;
    }


    /**
     * Called when building the animation to determine which animator to assign
     * to which key
     *
     * @param k key whose animator you wish to set
     * @return the animator to set
     */
    @Override
    protected Animator<Key> getAnimatorFor(Key k) {
        if (k.group == mArea) {
            new KeySizeAnimator(k.getSize(), 1.2f);
        }
        return new KeySizeAnimator(k.getSize(), 0);
    }

//    /**
//     * Override this method if you want to set the initial state of a key
//     *
//     * @param k
//     */
//    @Override
//    protected void setInitialState(Key k) {
//        this.values.get("endSize").put(k, k.group == mArea ? 1.2f : 0);
//        this.values.get("begSize").put(k, k.size);
//        this.values.get("begx").put(k, k.x);
//        this.values.get("begy").put(k, k.y);
//        this.values.get("destx").put(k,
//                k.group == mArea ? view.getAreaX(lastArea(k.group, k.loc)) : k.x);
//        this.values.get("desty").put(k,
//                k.group == mArea ? view.getAreaY(lastArea(k.group, k.loc)) : k.y);
//    }


    private static int lastArea(int group, int loc) {
        if (loc == 0)
            return group;
        if (group == 0)
            return loc;
        if (loc == 2)
            return 0;
        if (loc == 1)
            return (group + 1) == 6 ? 1 : (group + 1);
        return (group - 1) == 0 ? 5 : (group - 1);
    }
}
