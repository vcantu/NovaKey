package viviano.cantu.novakey.animations;

import android.animation.TimeInterpolator;
import android.view.animation.AnticipateInterpolator;

import viviano.cantu.novakey.animations.utils.Animator;
import viviano.cantu.novakey.model.elements.keyboards.Key;
import viviano.cantu.novakey.model.elements.keyboards.KeySizeAnimator;

/**
 * Created by Viviano on 10/25/2015.
 *
 * This animator will hide all keys not in mLocs
 */
public class FocusAnimation extends CharAnimation {

    private final TimeInterpolator mInterpolator = new AnticipateInterpolator();
    private final Animator<Key> mAnimator = new KeySizeAnimator(1, 0);
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
        return mInterpolator;
    }

    /**
     * Called when building the animation to determine which animator to assign
     * to which key
     *
     * @param k key whose animator you wish to set
     * @return the animator to set
     */
    @Override
    protected Animator<Key> getAnimatorFor(Key k) {
        for (Character c : mChars) {
            if (c.equals(k.getChar()))
                return null;
        }
        return mAnimator;
    }
}
