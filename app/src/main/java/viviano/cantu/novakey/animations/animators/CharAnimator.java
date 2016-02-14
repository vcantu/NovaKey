package viviano.cantu.novakey.animations.animators;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;

import java.util.ArrayList;

import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/27/2015.
 */
public abstract class CharAnimator extends Animator {

    final public static int NONE = -1, CENTER = 0, RANDOM = 1, INVERSE = 2, RIGHT = 4, UP = 8;

    protected int style = 0;
    protected long duration = 0;
    protected boolean reverse = false;

    protected AnimatorSet set;

    protected ArrayList<ValueAnimator> extraAnimators;

    public CharAnimator(int style, long duration) {
        this.style = style;
        this.duration = duration;
    }

    public CharAnimator(int style) {
        this(style, 500);
    }

    public CharAnimator(int style, long duration, boolean reverse) {
        this(style, duration);
        this.reverse = reverse;
    }

    @Override
    public AnimatorSet createSet() {
        AnimatorSet set = new AnimatorSet();
        for (Key k : view.dimens.kl) {
            setBeginingState(k, reverse);

            ValueAnimator a = animKey(k, getDelay(k, duration / 2));
            if (a != null)
                set.play(a);
        }
        if (extraAnimators != null) {
            for (ValueAnimator a : extraAnimators) {
                set.play(a);
            }
        }
        return set;
    }

    protected long getDelay(Key k, long max) {
        if (style == -1)
            return 0;

        if ((style & RANDOM) == RANDOM)
            return (long) (Math.random() * max);
        float x = view.dimens.x, y = view.dimens.y;

        if ((style & RIGHT) == RIGHT)
            x -= view.dimens.r;
        if ((style & UP) == UP)
            y += view.dimens.r;

        float dist = Util.distance(x, y, k.x, k.y);
        float ratio = dist / view.dimens.r;
        return (long) (max * (((style & INVERSE) == INVERSE) ? (1 - ratio) : ratio));
    }

    protected abstract void setBeginingState(Key key, boolean removing);
    protected abstract ValueAnimator animKey(final Key key, long delay);
}
