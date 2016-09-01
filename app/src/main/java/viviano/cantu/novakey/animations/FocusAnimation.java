package viviano.cantu.novakey.animations;

import android.animation.TimeInterpolator;
import android.view.animation.AnticipateInterpolator;

import viviano.cantu.novakey.elements.keyboards.Key;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/25/2015.
 *
 * This animator will hide all keys not in mLocs
 */
public class FocusAnimation extends CharAnimation {

    private Character[] mChars;

    public FocusAnimation(Character[] chars) {
        super(1);
        mChars = chars;
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
     *
     * @param k     key to update
     * @param value percent of animation used tu update key
     * @return the updated key
     */
    @Override
    protected void updateKey(Key k, float value) {
        for (Character c : mChars) {
            if (c.equals(k.VALUE))
                return;
        }
        k.setSize(Util.fromFrac(1, 0, value));
    }
}
