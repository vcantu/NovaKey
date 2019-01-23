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

package viviano.cantu.novakey.animations.utils;

import android.animation.TimeInterpolator;

/**
 * Copies the given interpolator but adds a delay
 *
 * Created by Viviano on 1/21/2016.
 */
public class DelayableInterpolator implements TimeInterpolator {

    private float mBegPercent, mEndPercent;
    private TimeInterpolator mBaseInterpolator;

    /**
     * Constructor for Delayable Interpolator
     * @param delay delay in milliseconds the interpolator will be waiting for
     * @param duration total duration of the process, to be cross referenced with delay
     * @param baseInterpolator interpolator to use when calculating value
     * @throws IllegalArgumentException if delay or duration are negative.
     *   Or if delay is longer than duration. Or if duration is longer than totalDuration
     */
    public DelayableInterpolator(long delay, long duration, long totalDuration,
                                 TimeInterpolator baseInterpolator) {
        if (delay < 0 || duration <= 0 || totalDuration <= 0
                || delay > totalDuration || (delay + duration) > totalDuration) {
            throw new IllegalArgumentException("Invalid delay or duration. delay: " +
                    delay + "   duration: " + duration + "   totalDuration: " + totalDuration);
        }

        this.mBaseInterpolator = baseInterpolator;

        this.mBegPercent = (float)delay / (float)duration;
        this.mEndPercent = (float)(totalDuration - duration - delay) / (float)totalDuration;
    }

    /**
     * Maps a value representing the elapsed fraction of an animation to a value that represents
     * the interpolated fraction. This interpolated value is then multiplied by the change in
     * value of an animation to derive the animated value at the current elapsed animation time.
     *
     * @param input A value between 0 and 1.0 indicating our current point
     *              in the animation where 0 represents the start and 1.0 represents
     *              the end
     * @return The interpolation value. This value can be more than 1.0 for
     * interpolators which overshoot their targets, or less than 0 for
     * interpolators that undershoot their targets.
     *
     * If the specified is > 0 delay, the value will be 0 until the delay is reached.
     * If a specified delay + duration < totalDuration the value will be 1 until the totalDuration
     * is reached
     */
    @Override
    public float getInterpolation(float input) {
        if (input <= mBegPercent)
            return 0;
        if (input >= (1 - mEndPercent))
            return 1;
        return mBaseInterpolator.getInterpolation(
                (input - mBegPercent) / (1 - mBegPercent - mEndPercent));
    }
}