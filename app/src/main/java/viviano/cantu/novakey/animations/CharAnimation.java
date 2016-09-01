package viviano.cantu.novakey.animations;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viviano.cantu.novakey.animations.utils.DelayableInterpolator;
import viviano.cantu.novakey.animations.utils.MultiValueAnimator;
import viviano.cantu.novakey.elements.keyboards.Key;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/27/2015.
 */
public abstract class CharAnimation extends BaseAnimation {

    public final static int NONE = -1, CENTER = 0, RANDOM = 1,
            FLIP_X = 2, FLIP_Y = 4, RIGHT = 8, UP = 16;

    protected int style = 0;
    protected long duration = 0;
    protected final Map<String, Map<Key, Object>> values;//use to store initial values


    public CharAnimation(int style) {
        this(style, 500);
    }

    public CharAnimation(int style, long duration) {
        this.style = style;
        this.duration = duration;
        this.values = new HashMap<>();
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
            long dur = duration / 2;
            long delay = getDelay(model, k, dur);
            anim.addInterpolator(k,
                    new DelayableInterpolator(
                            delay, dur, duration, getInterpolatorFor(k)),
                    delay, dur);
            this.setInitialState(k);
        }
        anim.setMultiUpdateListener(new MultiValueAnimator.MultiUpdateListener<Key>() {
            @Override
            public void onValueUpdate(ValueAnimator animator, float value, Key key) {
                updateKey(key, value);
            }

            @Override
            public void onAllUpdate(ValueAnimator animator, float value) {
                model.update();
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
    private long getDelay(Model model, Key k, long max) {
        if (style == -1)
            return 0;

        if ((style & RANDOM) == RANDOM)
            return (long) (Math.random() * max);

        float x = model.getX(), y = model.getY();

        if ((style & RIGHT) == RIGHT)
            x -= model.getRadius() * (((style & FLIP_X) == FLIP_X) ? -1 : 1);
        if ((style & UP) == UP)
            y += model.getRadius() * (((style & FLIP_Y) == FLIP_Y) ? -1 : 1);;

        float dist = Util.distance(x, y, k.getPosn().getX(model), k.getPosn().getX(model));
        float ratio = dist / (model.getRadius() * 2);
        return (long)(max * ratio);
    }
}
