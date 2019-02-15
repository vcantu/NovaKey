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

import android.animation.ValueAnimator;

import viviano.cantu.novakey.core.model.Model;

/**
 * Created by Viviano on 9/28/2015.
 */
public abstract class BaseAnimation implements Animation {

    private long mDelay = 0;
    private ValueAnimator mAnim;
    private OnEndListener mOnEnd;
    private OnUpdateListener mOnUpdate;


    /**
     * Should start the animation
     * <p>
     * Initialize the necessary data here
     *
     * @param model
     */
    @Override
    public void start(Model model) {
        mAnim = animator(model);
        if (mAnim == null) {
            if (mOnEnd != null)
                mOnEnd.onEnd();
            return;
        }
        mAnim.setStartDelay(mAnim.getStartDelay() + mDelay);

        mAnim.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
            }


            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (mOnEnd != null)
                    mOnEnd.onEnd();
            }


            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
            }


            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {
            }
        });
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mOnUpdate != null)
                    mOnUpdate.onUpdate();
            }
        });
        mAnim.start();
    }


    /**
     * Will cancel the animation if it's running
     */
    @Override
    public void cancel() {
        if (mAnim != null)
            mAnim.cancel();
    }


    /**
     * @param model given to reference. Also should call
     *              model.update() to invalidate the view
     * @return ValueAnimator which will be called
     */
    protected abstract ValueAnimator animator(Model model);


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
