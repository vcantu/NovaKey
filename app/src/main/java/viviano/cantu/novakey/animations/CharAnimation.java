package viviano.cantu.novakey.animations;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

import java.util.HashMap;
import java.util.Map;

import viviano.cantu.novakey.animations.utils.Animator;
import viviano.cantu.novakey.animations.utils.DelayableInterpolator;
import viviano.cantu.novakey.animations.utils.MultiValueAnimator;
import viviano.cantu.novakey.elements.keyboards.Key;
import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/27/2015.
 */
public abstract class CharAnimation extends BaseAnimation {

    public final static int NONE = -1, CENTER = 0, RANDOM = 1,
            FLIP_X = 2, FLIP_Y = 4, RIGHT = 8, UP = 16;

    private final int mStyle;
    private final long mDuration;
    private final Map<Key, Animator<Key>> mAnimators;

    public CharAnimation(int style) {
        this(style, 500);
    }

    public CharAnimation(int style, long duration) {
        mStyle = style;
        mDuration = duration;
        mAnimators = new HashMap<>();
    }

    /**
     * @param model given to reference. Also should call
     *              model.update() to invalidate the view
     * @return ValueAnimator which will be called
     */
    @Override
    protected ValueAnimator animator(Model model) {
        MultiValueAnimator<Key> anim = MultiValueAnimator.create();

        for (Key k : model.getKeyboard()) {
            long dur = mDuration / 2;
            long delay = getDelay(model, k, dur);
            anim.addInterpolator(k,
                    new DelayableInterpolator(
                            delay, dur, mDuration, getInterpolatorFor(k)), delay, dur);
            mAnimators.put(k, getAnimatorFor(k));
        }
        anim.setMultiUpdateListener(new MultiValueAnimator.MultiUpdateListener<Key>() {
            @Override
            public void onValueUpdate(ValueAnimator animator, float value, Key key) {
                Animator<Key> anim = mAnimators.get(key);
                if (anim != null)
                    anim.update(key, value);
            }

            @Override
            public void onAllUpdate(ValueAnimator animator, float value) {
                //TODO: need way to update
            }
        });

        return anim;
    }

    /**
     * Will be called when building the animation to set this particular key's
     * interpolator
     *
     * @param k key whose interpolator you wish to set
     * @return an interpolator to set
     */
    protected abstract TimeInterpolator getInterpolatorFor(Key k);

    /**
     * Called when building the animation to determine which animator to assign
     * to which key
     *
     * @param k key whose animator you wish to set
     * @return the animator to set
     */
    protected abstract Animator<Key> getAnimatorFor(Key k);

    /**
     * Will determine the delay according to this animator's  parameters
     *
     * @param k   key to set delay of
     * @param max maximum delay
     * @return the delay in milliseconds
     */
    private long getDelay(Model model, Key k, long max) {
        MainDimensions d = model.getMainDimensions();
        if (mStyle == -1)
            return 0;

        if ((mStyle & RANDOM) == RANDOM)
            return (long) (Math.random() * max);

        float x = d.getX(), y = d.getY();

        if ((mStyle & RIGHT) == RIGHT)
            x -= d.getRadius() * (((mStyle & FLIP_X) == FLIP_X) ? -1 : 1);
        if ((mStyle & UP) == UP)
            y += d.getRadius() * (((mStyle & FLIP_Y) == FLIP_Y) ? -1 : 1);;

        float dist = Util.distance(x, y, k.getPosn().getX(d), k.getPosn().getX(d));
        float ratio = dist / (d.getRadius() * 2);
        return (long)(max * ratio);
    }
}
