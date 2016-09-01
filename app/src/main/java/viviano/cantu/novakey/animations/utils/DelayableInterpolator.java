package viviano.cantu.novakey.animations.utils;

import android.animation.TimeInterpolator;

/**
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