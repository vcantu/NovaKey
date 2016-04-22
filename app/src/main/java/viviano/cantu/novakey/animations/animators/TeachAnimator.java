package viviano.cantu.novakey.animations.animators;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.NovaKeyView;
import viviano.cantu.novakey.utils.Print;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 10/25/2015.
 */
public class TeachAnimator extends Animator {

    private String command;

    public TeachAnimator(String command) {
        this.command = command;
    }

    public AnimatorSet createSet() {
        AnimatorSet set = new AnimatorSet();
        if (command.length() > 0) {
            ValueAnimator prev = animateChar(command.charAt(0), 200);
            set.play(prev);
            if (command.length() > 1) {
                for (int i = 1; i < command.length(); i++) {
                    ValueAnimator a = animateChar(command.charAt(i), 200);
                    set.play(a).after(prev);
                    prev = a;
                }
            }
        }
        set.setStartDelay(mDelay);
        return set;
    }

    public ValueAnimator animateChar(final char c, long betweenDelay) {
        KeyLayout kl = Controller.currKeyboard;
        int group = kl.getGroup(c), loc = kl.getLoc(c);
        int firstArea = group, lastArea = lastArea(group, loc);

        if (c == ' ') {
            firstArea = 2;
            lastArea = 4;
        }
        else if (c == '⌫') {
            firstArea = 4;
            lastArea = 2;
        }
        else if (c == '▲') {
            firstArea = 3;
            lastArea = -1;
        }
        else if (c == '\n') {
            lastArea = 3;
            firstArea = -1;
        }
        if (c >='⑴' && c <= '⑽') {
            int i = c - '⑴';
            return rotateAnimator((i % 5)+1, c > '⑸');
        }
        float x1 = view.getAreaX(firstArea), y1 = view.getAreaY(firstArea),
                x2 = view.getAreaX(lastArea), y2 = view.getAreaY(lastArea);

        if (firstArea == lastArea) {
            ValueAnimator a = tapAnimator(x1, y1);
            a.setStartDelay(betweenDelay);
            return a;
        }
        ValueAnimator a = swipeAnimator(x1, y1, x2, y2);
        a.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animator) {
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animator) {
                if (c == '▲')
                    Controller.toggleShift();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animator) {
            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animator) {
            }
        });
        a.setStartDelay(betweenDelay);
        return a;
    }

    public ValueAnimator tapAnimator(final float x, final float y) {
        final Fing fing = new Fing(x, y);

        final float r1 = fing.r, r2 = fing.r * 1.2f;
        final ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(600);
        anim.setInterpolator(new OvershootInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac = (Float) anim.getAnimatedValue();
                fing.r = r1 + (r2 - r1) * frac;
                view.invalidate();
            }
        });
        anim.addListener(fingListener(fing));
        return anim;
    }

    public ValueAnimator swipeAnimator(final float x1, final float y1, final float x2, final float y2) {
        final Fing fing = new Fing(x1, y1);

        final ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(700);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac = (Float) anim.getAnimatedValue();
                fing.x = x1 + (x2 - x1) * frac;
                fing.y = y1 + (y2 - y1) * frac;
                view.invalidate();
            }
        });
        anim.addListener(fingListener(fing));
        return anim;
    }

    public ValueAnimator rotateAnimator(int area, boolean clockwise) {
        final Fing fing = new Fing(view.getAreaX(area), view.getAreaY(area));

        double a = Util.angle(view.dimens.x, view.dimens.y, fing.x, fing.y);
        double dist = Math.PI * 2 / 5;
        Print.angle(a);
        final ValueAnimator anim = ValueAnimator.ofFloat(
                (float)a, (float)(clockwise ? a + dist : a - dist));
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        final float rad = (view.dimens.r - view.dimens.sr) / 2 + view.dimens.sr;
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) anim.getAnimatedValue();
                fing.x = Util.xFromAngle(view.dimens.x, rad, value);
                fing.y = Util.yFromAngle(-view.dimens.y, rad, value);//TODO: yFromAngle has changed
                view.invalidate();
            }
        });
        anim.addListener(fingListener(fing));
        return anim;
    }

    private android.animation.Animator.AnimatorListener fingListener(final Fing fing) {
        return new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                view.addDrawer(fing);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                view.removeDrawer(fing);
                view.invalidate();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {
            }
        };
    }

    @Override
    protected ValueAnimator animator() {
        return null;
    }

    private class Fing implements NovaKeyView.Drawer {
        public float x, y;
        public float r;
        public Paint p;

        public Fing(float x, float y) {
            this.x = x;
            this.y = y;
            p = new Paint();
            p.setColor(0xFFFFFFFF);
            p.setFlags(Paint.ANTI_ALIAS_FLAG);
            r = view.dimens.sr * .3f;
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawCircle(x, y, r, p);
        }
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
