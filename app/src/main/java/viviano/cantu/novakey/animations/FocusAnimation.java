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

package viviano.cantu.novakey.animations;

import android.animation.TimeInterpolator;
import android.view.animation.AnticipateInterpolator;

import viviano.cantu.novakey.animations.utils.Animator;
import viviano.cantu.novakey.elements.keyboards.Key;
import viviano.cantu.novakey.elements.keyboards.KeySizeAnimator;

/**
 * Created by Viviano on 10/25/2015.
 *
 * This animator will hide all keys not in mLocs
 */
public class FocusAnimation extends CharAnimation {

    private final TimeInterpolator mInterpolator = new AnticipateInterpolator();
    private final Animator<Key> mAnimator = new KeySizeAnimator(1, 0);
    private Character[] mChars;

    public FocusAnimation(Character[] chars) {
        super(1);
        mChars = chars;
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
        for (Character c : mChars) {
            if (c.equals(k.getChar()))
                return null;
        }
        return mAnimator;
    }
}
