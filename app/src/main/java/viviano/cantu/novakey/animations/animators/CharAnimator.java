package viviano.cantu.novakey.animations.animators;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import viviano.cantu.novakey.model.keyboards.Key;
import viviano.cantu.novakey.animations.DelayableInterpolator;
import viviano.cantu.novakey.animations.MultiValueAnimator;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/27/2015.
 */
public abstract class CharAnimator extends Animator {

    public final static int NONE = -1, CENTER = 0, RANDOM = 1,
            FLIP_X = 2, FLIP_Y = 4, RIGHT = 8, UP = 16;

    protected int style = 0;
    protected long duration = 0;
    protected final Map<String, Map<Key, Object>> values;//use to store initial values


    public CharAnimator(int style) {
        this(style, 500);
    }

    public CharAnimator(int style, long duration) {
        this.style = style;
        this.duration = duration;
        this.values = new HashMap<>();
        for (String s : this.initializeValues()) {
            values.put(s, new HashMap<Key, Object>());
        }
    }

    /**
     * @return a MultiValueAnimator
     */
    @Override
    protected ValueAnimator animator() {
        MultiValueAnimator<Key> anim = MultiValueAnimator.create();
        ArrayList<Key> keys = new ArrayList<>(26);
        for (Key k : view.dimens.kl) {
            long dur = duration / 2;
            long delay = getDelay(k, dur);
            anim.addInterpolator(k,
                    new DelayableInterpolator(
                            delay, dur, duration, getInterpolatorFor(k)),
                    delay, dur);
            keys.add(k);
            this.setInitialState(k);
        }
        anim.setMultiUpdateListener(new MultiValueAnimator.MultiUpdateListener<Key>() {
            @Override
            public void onValueUpdate(ValueAnimator animator, float value, Key key) {
                updateKey(key, value);
            }

            @Override
            public void onAllUpdate(ValueAnimator animator, float value) {
                view.invalidate();
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
     * Use this to set the keys of the value maps you want
     */
    protected abstract String[] initializeValues();

    /**
     * Override this method if you want to set the initial state of a key
     *
     * @param k
     */
    protected abstract void setInitialState(Key k);

    /**
     * Override this method and update the key accordingly
     *
     * @param k     key to update
     * @param value percent of animation used tu update key
     */
    protected abstract void updateKey(Key k, float value);


    /**
     * Will determine the delay according to this animator's  parameters
     *
     * @param k   key to set delay of
     * @param max maximum delay
     * @return the delay in milliseconds
     */
    protected long getDelay(Key k, long max) {
        if (style == -1)
            return 0;

        if ((style & RANDOM) == RANDOM)
            return (long) (Math.random() * max);

        float x = view.dimens.x, y = view.dimens.y;

        if ((style & RIGHT) == RIGHT)
            x -= view.dimens.r * (((style & FLIP_X) == FLIP_X) ? -1 : 1);
        if ((style & UP) == UP)
            y += view.dimens.r * (((style & FLIP_Y) == FLIP_Y) ? -1 : 1);;

        float dist = Util.distance(x, y, k.x, k.y);
        float ratio = dist / (view.dimens.r * 2);
        return (long)(max * ratio);
    }
}
