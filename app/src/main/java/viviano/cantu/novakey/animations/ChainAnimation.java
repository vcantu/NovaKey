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

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 9/2/2016.
 */
public class ChainAnimation implements Animation {

    private long mDelay = 0;
    private OnEndListener mOnEnd;
    private OnUpdateListener mOnUpdate;

    private final List<Animation> mAnimations;

    public ChainAnimation() {
        mAnimations = new ArrayList<>();
    }

    public ChainAnimation(List<Animation> animations) {
        mAnimations = animations;
    }

    public ChainAnimation add(Animation animation) {
        mAnimations.add(animation);
        return this;
    }

    /**
     * Should start the animation
     * <p>
     * Initialize the necessary data here
     *
     * @param model
     */
    @Override
    public void start(Model model) {
        mAnimations.get(0).setDelay(mDelay);
        for (int i = 0; i < mAnimations.size() - 1; i++) {
            final int finalI = i;
            mAnimations.get(i).setOnEndListener(() -> mAnimations.get(finalI + 1).start(model));
            mAnimations.get(i).setOnUpdateListener(mOnUpdate);
        }
        mAnimations.get(mAnimations.size() - 1).setOnEndListener(mOnEnd);
        mAnimations.get(0).start(model);
    }

    @Override
    public void cancel() {
        for (Animation a : mAnimations) {
            a.cancel();
        }
    }

    /**
     * Set the start delay of this animation
     *
     * @param delay start delay in milliseconds
     * @return this animation
     */
    @Override
    public Animation setDelay(long delay) {
        mDelay = delay;
        return this;
    }

    /**
     * @param listener set this animation's on end listener
     */
    @Override
    public Animation setOnEndListener(OnEndListener listener) {
        mOnEnd = listener;
        return this;
    }
    /**
     * @param listener set this animation's on end listener
     */
    @Override
    public Animation setOnUpdateListener(OnUpdateListener listener) {
        mOnUpdate = listener;
        return this;
    }
}
