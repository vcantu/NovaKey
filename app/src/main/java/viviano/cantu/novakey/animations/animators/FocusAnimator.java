package viviano.cantu.novakey.animations.animators;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.Location;
import viviano.cantu.novakey.NovaKeyView;

/**
 * Created by Viviano on 10/25/2015.
 *
 * This animator will hide all keys not in mLocs
 */
public class FocusAnimator extends CharAnimator {

    private Location[] mLocs;

    public FocusAnimator(Location[] locs) {
        super(0);
        this.mLocs = locs;
    }

    @Override
    protected void setBeginingState(Key key, boolean removing) {
        //do nothing
    }

    @Override
    protected ValueAnimator animKey(final Key key, long delay) {
        for (Location l : mLocs) {
            if (l.x == key.group && l.y == key.loc)
                return null;
        }

        ValueAnimator anim = ValueAnimator.ofFloat(1, 0)
                .setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                key.size = (float)animation.getAnimatedValue();
                view.invalidate();
            }
        });
        return anim;
    }
}
