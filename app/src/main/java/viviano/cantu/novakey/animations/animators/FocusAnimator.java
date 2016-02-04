package viviano.cantu.novakey.animations.animators;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.Location;
import viviano.cantu.novakey.NovaKeyView;

/**
 * Created by Viviano on 10/25/2015.
 */
public class FocusAnimator extends Animator {

    //TODO: add delay

    private Location[] mLocs;
    private ArrayList<Focus> focusPoints;
    private boolean removing = false;

    public FocusAnimator(Location[] locs) {
        this.mLocs = locs;
        focusPoints = new ArrayList<>();
    }

    public FocusAnimator(Location[] locs, boolean remove) {
        this(locs);
        removing = remove;
    }

    @Override
    public AnimatorSet createSet() {
        AnimatorSet set = new AnimatorSet();
        for (int i = 0; i < mLocs.length; i++) {
            Key k = view.dimens.kl.getKey(mLocs[i].x, mLocs[i].y);
            ValueAnimator a = focusAnim(k.x, k.y);
            a.setStartDelay((int) (Math.random() * 400) + delay);
            set.play(a);
        }
        return set;
    }

    public ValueAnimator focusAnim(float x, float y) {
        final Focus focus = new Focus(x, y);
        focusPoints.add(focus);

        if (removing) {
            view.addDrawer(focus);
            view.invalidate();
        }

        final ValueAnimator anim = ValueAnimator.ofFloat(
                removing ? focus.r : 0, removing ? 0 : focus.r);
        anim.setDuration(400);
        anim.setInterpolator(new OvershootInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                focus.r = (Float) anim.getAnimatedValue();
                view.invalidate();
            }
        });
        anim.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animator) {
                if (!removing)
                    view.addDrawer(focus);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animator) {
                view.invalidate();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animator) {
            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animator) {
            }
        });
        return anim;
    }


    private class Focus implements NovaKeyView.Drawer {
        public float x, y;
        public float r;
        public Paint p;
        public Path path;

        public Focus(float x, float y) {
            this.x = x;
            this.y = y;
            p = new Paint();
            p.setColor(0x50000000);
            p.setFlags(Paint.ANTI_ALIAS_FLAG);
            r = view.dimens.sr * .4f;
            p.setStrokeWidth(10);
            p.setStyle(Paint.Style.STROKE);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawCircle(x, y, r, p);
        }
    }

}
