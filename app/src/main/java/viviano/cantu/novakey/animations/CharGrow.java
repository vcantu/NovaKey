package viviano.cantu.novakey.animations;

import android.animation.TimeInterpolator;
import android.view.animation.OvershootInterpolator;

import viviano.cantu.novakey.animations.utils.Animator;
import viviano.cantu.novakey.model.elements.keyboards.Key;
import viviano.cantu.novakey.model.elements.keyboards.KeySizeAnimator;

/**
 * Created by Viviano on 10/27/2015.
 */
public class CharGrow extends CharAnimation {

    private final TimeInterpolator mInterpolator = new OvershootInterpolator();
    private final Animator<Key> mAnimator = new KeySizeAnimator(0, 1);

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
        return mAnimator;
    }
}
