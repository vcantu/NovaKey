package viviano.cantu.novakey.animations.animators;

import android.animation.ValueAnimator;

import viviano.cantu.novakey.NovaKeyView;

/**
 * Created by Viviano on 9/28/2015.
 */
public abstract class Animator {

    protected NovaKeyView view;
    protected long mDelay = 0;
    protected ValueAnimator mAnim;

    /**
     * Will start the animation and invalidate the given view
     * @param view view to invalidate
     */
    public void start(NovaKeyView view) {
        this.view = view;
        mAnim = animator();
        mAnim.setStartDelay(mDelay);
        mAnim.start();

        mAnim.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                onPreAnimate();
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                onPostAnimaate();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) { }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) { }
        });
    }

    /**
     * Override this method to set any pre animation behavior
     */
    protected void onPreAnimate() {
    }

    /**
     * Override this method to set any post animation behavior
     */
    protected void onPostAnimaate() {

    }

    protected abstract ValueAnimator animator();

    /**
     * Will add a delay to this animator and return it
     * @param delay ammount in milliseconds
     * @return this animator
     */
    public Animator addDelay(long delay) {
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
