package viviano.cantu.novakey.animations.animators;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;

import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 11/15/2015.
 */
public class ResetCharAnimator extends CharAnimator {

    public ResetCharAnimator(long duration) {
        super(-1, duration);
    }

    public ResetCharAnimator() {
        this(80);
    }

    @Override
    public void setBeginingState(Key key, boolean removing) {

    }

    @Override
    public ValueAnimator animKey(final Key key, long delay) {
        final float size = key.size;
        final float x = key.x;
        final float y = key.y;
        final float endX = key.getDesiredX(view.dimens.x, view.dimens.r, view.dimens.sr);
        final float endY = key.getDesiredY(view.dimens.y, view.dimens.r, view.dimens.sr);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(this.duration);
        anim.setStartDelay(delay);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                float frac = (Float)anim.getAnimatedValue();
                key.size = Util.fromFrac(size, 1, frac);
                key.x = Util.fromFrac(x, endX, frac);
                key.y = Util.fromFrac(y, endY, frac);
                view.invalidate();
            }
        });
        return anim;
    }
}
