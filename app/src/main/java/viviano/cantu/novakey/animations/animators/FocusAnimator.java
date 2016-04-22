package viviano.cantu.novakey.animations.animators;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.Location;
import viviano.cantu.novakey.NovaKeyView;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/25/2015.
 *
 * This animator will hide all keys not in mLocs
 */
public class FocusAnimator extends CharAnimator {

    private Location[] mLocs;

    public FocusAnimator(Location[] locs) {
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
