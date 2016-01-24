package viviano.cantu.novakey.animations;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Interpolator;

import java.util.ArrayList;

/**
 * Created by Viviano on 1/22/2016.
 */
public class MultiValueAnimator extends ValueAnimator {

    /**
     * Static method which MUST be used to initialize a MultiValueAnimator
     *
     * @return A the equivalent of ValueAnimator.ofFloat(0, 1)
     */
    public static MultiValueAnimator create() {
        MultiValueAnimator anim = new MultiValueAnimator();
        anim.setFloatValues(0, 1);
        return anim;
    }

    private ArrayList<InterpolatorData> mInterpolatorData;
    private ArrayList<DelayableInterpolator> mInterpolators;
    private MultiUpdateListener mUpdateListener;

    /**
     * Constructor initializes data and adds an update listener which calls the
     * MultiUpdateListener methods onValueUpdate() & onAllUpdate()
     *
     */
    public MultiValueAnimator() {
        super();
        mInterpolatorData = new ArrayList<>();

        this.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) getAnimatedValue();
                for (int i = 0; i < mInterpolators.size(); i++) {
                    float actual = mInterpolators.get(i).getInterpolation(v);
                    if (mUpdateListener != null)
                        mUpdateListener.onValueUpdate(animation, actual, i);
                }

                if (mUpdateListener != null)
                    mUpdateListener.onAllUpdate(animation, v);
            }
        });
    }

    /**
     * Determines total duration of animation & sets DelayInterpolators
     * Then starts the animation
     */
    @Override
    public void start() {
        long totalDuration = 0;
        for (InterpolatorData id : mInterpolatorData) {
            if ((id.delay + id.duration) > totalDuration)
                totalDuration = id.delay + id.duration;
        }
        setDuration(totalDuration);

        mInterpolators = new ArrayList<>();
        for (InterpolatorData id : mInterpolatorData) {
            mInterpolators.add(new DelayableInterpolator(id.delay, id.duration, totalDuration,
                    id.interpolator));
        }
        super.start();
    }

    /**
     * Adds a new interpolator which will be used to create its corresponding DelayInterpolator
     *
     * @param timeInterpolator interpolator for DelayInterpolator
     * @param delay delay of specific value animation
     * @param duration duration of specific value animation
     */
    public void addInterpolator(TimeInterpolator timeInterpolator, long delay, long duration) {
        mInterpolatorData.add(new InterpolatorData(timeInterpolator, delay, duration));
    }

    /**
     * Adds a new interpolator which will be used to create its corresponding DelayInterpolator
     *
     * @param index index of specific value animation
     * @param timeInterpolator interpolator for DelayInterpolator
     * @param delay delay of specific value animation
     * @param duration duration of specific value animation
     */
    public void addInterpolator(int index, TimeInterpolator timeInterpolator, long delay, long duration) {
        mInterpolatorData.add(index, new InterpolatorData(timeInterpolator, delay, duration));
    }

    // holds interpolator data until start
    private class InterpolatorData {
        TimeInterpolator interpolator;
        long delay, duration;

        public InterpolatorData(TimeInterpolator interpolator, long delay, long duration) {
            this.interpolator = interpolator;
            this.delay = delay;
            this.duration = duration;
        }
    }


    /**
     * Sets update multi update listener
     *
     * @param updateListener listener to set
     */
    public void setMultiUpdateListener(MultiUpdateListener updateListener) {
        mUpdateListener = updateListener;
    }

    /**
     * Update Listener that contains callback methods which users of MultiValueAnimator
     * will use to specify values
     */
    public interface MultiUpdateListener {
        /**
         * Callback method that will be called for each different value which is being animated.
         *
         * @param animator this animator
         * @param value If a specific value is given a delay, the value will be 0 until the delay
         *              is reached. If a specific value is done animating, the value will be 1
         *              until the whole animation is finished
         * @param index index of the specific value animatios
         */
        void onValueUpdate(ValueAnimator animator, float value, int index);

        /**
         * Callback method that will be called at the end of each update.
         * Will be called after all the onValueUpdate() methods are called
         *
         * @param animator this animator
         * @param value current animated value from 0 - 1 depending on given interpolator
         */
        void onAllUpdate(ValueAnimator animator, float value);
    }
}
