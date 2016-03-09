package viviano.cantu.novakey.animations.animators;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;

import viviano.cantu.novakey.drawing.Icons;

/**
 * Created by Viviano on 11/16/2015.
 */

//TODO: this class is supposed to be the animator that flashes an icon, (For example SPACE_BAR);
    // when the user does the space motion
public class IconHintAnimator extends Animator {

    private Icons.Drawable icon;
    private long duration;

    //TODO: not done
    public IconHintAnimator(Icons.Drawable icon) {
        this(icon, 100);
    }

    public IconHintAnimator(Icons.Drawable icon, long duration) {
        this.icon = icon;
        this.duration = duration;
    }


    private ValueAnimator animIcon(Icons ic, int first, int last) {
        final float begX = view.getAreaX(first);
        final float begY = view.getAreaY(first);
        final float endX = view.getAreaX(last);
        final float endY = view.getAreaY(last);
        final float endSize = (view.dimens.r - view.dimens.sr) * .5f;

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(this.duration);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

            }
        });
        return anim;
    }

    @Override
    public AnimatorSet createSet() {
        return null;
    }
}
