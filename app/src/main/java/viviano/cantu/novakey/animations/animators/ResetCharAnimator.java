package viviano.cantu.novakey.animations.animators;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 11/15/2015.
 */
public class ResetCharAnimator extends CharAnimator {

    public ResetCharAnimator() {
        this(80);
    }

    public ResetCharAnimator(long duration) {
        super(-1, duration);
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
        return new AccelerateInterpolator();
    }

    /**
     * Initialize the Maps in this.values before adding to them
     */
    @Override
    protected String[] initializeValues() {
        return new String[] { "size", "x", "y", "endx", "endy" };
    }

    /**
     * Override this method if you want to set the initial state of a key
     *
     * @param k
     */
    @Override
    protected void setInitialState(Key k) {
        this.values.get("size").put(k, k.size);
        this.values.get("x").put(k, k.x);
        this.values.get("y").put(k, k.y);
        this.values.get("endx").put(k,
                k.getDesiredX(view.dimens.x, view.dimens.r, view.dimens.sr));
        this.values.get("endy").put(k,
                k.getDesiredY(view.dimens.y, view.dimens.r, view.dimens.sr));
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
        k.size = Util.fromFrac((Float)values.get("size").get(k), 1, value);
        k.x = Util.fromFrac(
                (Float)values.get("x").get(k),
                (Float)values.get("endx").get(k), value);
        k.y = Util.fromFrac(
                (Float)values.get("y").get(k),
                (Float)values.get("endy").get(k), value);
    }
}
