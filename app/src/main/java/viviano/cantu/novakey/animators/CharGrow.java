package viviano.cantu.novakey.animators;

import android.animation.ValueAnimator;
import android.view.animation.OvershootInterpolator;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.NovaKeyView;
import viviano.cantu.novakey.Print;

/**
 * Created by Viviano on 10/27/2015.
 */
public class CharGrow extends CharAnimator {

    public CharGrow(int style, long duration) {
        super(style, duration);
    }

    public CharGrow(int style) {
        super(style);
    }

    public CharGrow(int style, long duration, boolean reverse) {
        super(style, duration, reverse);
    }

    @Override
    protected void setBeginingState(Key k, boolean removing) {
        if (!removing)
            k.size = 0;
    }

    @Override
    public ValueAnimator animKey(final Key key, long delay) {
        ValueAnimator anim = ValueAnimator.ofFloat(reverse ? 1 : 0, reverse ? 0 : 1);
        anim.setDuration(duration / 2);
        anim.setStartDelay(delay);
        anim.setInterpolator(new OvershootInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                key.size = (Float)anim.getAnimatedValue();
                view.invalidate();
            }
        });
        return anim;
    }
}
