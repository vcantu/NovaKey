package viviano.cantu.novakey.view.animations.animators;

import android.animation.ValueAnimator;

import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.view.INovaKeyView;

/**
 * Created by Viviano on 9/28/2015.
 */
public abstract class BadAnimator {

    protected INovaKeyView view;
    protected DrawModel model;

    private long mDelay = 0;
    private ValueAnimator mAnim;

    /**
     * Will start the animation and invalidate the given view
     * @param view view to invalidate
     */
    public void start(INovaKeyView view, DrawModel model) {
        this.view = view;
        mAnim = animator();
        if (mAnim == null) {
            onEnd();
            return;
        }
        mAnim.setStartDelay(mAnim.getStartDelay() + mDelay);

        mAnim.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) { }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                onEnd();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) { }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) { }
        });
        mAnim.start();
    }

    /**
     * Override this method to set any post animation behavior
     */
    public void onEnd() {}

    /**
     * @return ValueAnimator which will be called
     */
    protected abstract ValueAnimator animator();

    /**
     * Will add a delay to this animator and return it
     * @param delay ammount in milliseconds
     * @return this animator
     */
    public BadAnimator addDelay(long delay) {
        mDelay = delay;
        return this;
    }

    /**
     * Will cancel the animation if it's running
     */
    public void cancel() {
        if (mAnim != null)
            mAnim.cancel();
    }

}
