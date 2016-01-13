package viviano.cantu.novakey.animators;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.Key;
import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.NovaKeyView;

/**
 * Created by Viviano on 9/28/2015.
 */
public abstract class Animator {

    protected NovaKeyView view;
    protected long delay = 0;
    protected AnimatorSet set;

    public void start(NovaKeyView view) {
        this.view = view;
        set = createSet();
        set.setStartDelay(delay);
        set.start();
    }

    public abstract AnimatorSet createSet();

    public void cancel() {
        if (set != null)
            set.cancel();
    }

    public Animator addDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public void setView(NovaKeyView view) {
        this.view = view;
    }

}
