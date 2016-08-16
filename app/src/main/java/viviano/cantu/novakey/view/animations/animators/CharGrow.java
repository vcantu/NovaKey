package viviano.cantu.novakey.view.animations.animators;

import android.animation.TimeInterpolator;
import android.view.animation.OvershootInterpolator;

import viviano.cantu.novakey.model.keyboards.Key;

/**
 * Created by Viviano on 10/27/2015.
 */
public class CharGrow extends CharBadAnimator {

    public CharGrow(int style, long duration) {
        super(style, duration);
    }

    public CharGrow(int style) {
        super(style);
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
        return new OvershootInterpolator();
    }

    /**
     * Use this to set the keys of the value maps you want
     */
    @Override
    protected String[] initializeValues() {
        return new String[0];
    }

    /**
     * Override this method if you want to set the initial state of a key
     *
     * @param k
     */
    @Override
    protected void setInitialState(Key k) {
        k.size = 0;
    }

    /**
     * Override this method and update the key accordingly
     *
     * @param k     key to update
     * @param value percent of animation used tu update key
     * @return the updated key
     */
    @Override
    protected void updateKey(Key k, float value) {
        k.size = value;
    }
}
