package viviano.cantu.novakey.view.animations.animators;

import android.animation.TimeInterpolator;
import android.view.animation.AnticipateInterpolator;

import viviano.cantu.novakey.model.keyboards.Key;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/25/2015.
 *
 * This animator will hide all keys not in mLocs
 */
public class FocusBadAnimator extends CharBadAnimator {

    private Location[] mLocs;

    public FocusBadAnimator(Location[] locs) {
        super(0);
        this.mLocs = locs;
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
        return new AnticipateInterpolator();
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
        //nothing
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
        for (Location l : mLocs) {
            if (l.x == k.group && l.y == k.loc)
                return;
        }
        k.size = Util.fromFrac(1, 0, value);
    }
}
