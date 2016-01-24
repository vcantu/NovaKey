package viviano.cantu.novakey.animations.animators;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;

import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 11/15/2015.
 */
public class HintAnimator extends CharAnimator {

    private int area;

    public HintAnimator(int area, long duration) {
        super(-1, duration);
        this.area = area;
    }

    public HintAnimator(int area) {
        this(area, 100);
    }

    @Override
    public void setBeginingState(Key key, boolean removing) {
    }

    @Override
    public ValueAnimator animKey(final Key key, long delay) {
        final float destX = key.group == area ? view.getAreaX(lastArea(key.group, key.loc)) : key.x;
        final float destY = key.group == area ? view.getAreaY(lastArea(key.group, key.loc)) : key.y;
        final float size = key.group == area ? 1.2f : 0;
        final float begX = key.x, begY = key.y, begSize = key.size;

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(this.duration);
        anim.setStartDelay(delay);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                float frac = (Float)anim.getAnimatedValue();
                key.size = Util.fromFrac(begSize, size, frac);
                key.x = Util.fromFrac(begX, destX, frac);
                key.y = Util.fromFrac(begY, destY, frac);
                view.invalidate();
            }
        });
        return anim;
    }

    private static int lastArea(int group, int loc) {
        if (loc == 0)
            return group;
        if (group == 0)
            return loc;
        if (loc == 2)
            return 0;
        if (loc == 1)
            return (group + 1) == 6 ? 1 : (group + 1);
        return (group - 1) == 0 ? 5 : (group - 1);
    }
}
