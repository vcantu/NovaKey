package viviano.cantu.novakey.view.animations.animators;

import android.animation.TimeInterpolator;
import android.view.animation.AccelerateInterpolator;

import viviano.cantu.novakey.model.keyboards.Key;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 11/15/2015.
 */
public class HintBadAnimator extends CharBadAnimator {

    private int area;

    public HintBadAnimator(int area, long duration) {
        super(-1, duration);
        this.area = area;
    }

    public HintBadAnimator(int area) {
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
        return new AccelerateInterpolator();
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
        this.values.get("endSize").put(k, k.group == area ? 1.2f : 0);
        this.values.get("begSize").put(k, k.size);
        this.values.get("begx").put(k, k.x);
        this.values.get("begy").put(k, k.y);
        this.values.get("destx").put(k,
                k.group == area ? view.getAreaX(lastArea(k.group, k.loc)) : k.x);
        this.values.get("desty").put(k,
                k.group == area ? view.getAreaY(lastArea(k.group, k.loc)) : k.y);
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
        k.size = Util.fromFrac(
                (Float)values.get("begSize").get(k),
                (Float)values.get("endSize").get(k), value);
        k.x = Util.fromFrac(
                (Float)values.get("begx").get(k),
                (Float)values.get("destx").get(k), value);
        k.y = Util.fromFrac(
                (Float)values.get("begy").get(k),
                (Float)values.get("desty").get(k), value);
    }

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
