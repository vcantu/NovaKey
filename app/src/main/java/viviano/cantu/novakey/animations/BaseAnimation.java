package viviano.cantu.novakey.animations;

import android.animation.ValueAnimator;

import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 9/28/2015.
 */
public abstract class BaseAnimation implements Animation{

    private long mDelay = 0;
    private ValueAnimator mAnim;

    /**
     * Should start the animation
     * <p>
     * Initialize the necessary data here
     *
     * @param model
     */
    @Override
    public void start(Model model) {
        mAnim = animator(model);
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
    protected void onEnd() {}

    /**
     * @param model given to reference. Also should call
     *              model.update() to invalidate the view
     * @return ValueAnimator which will be called
     */
    protected abstract ValueAnimator animator(Model model);

    /**
     * Will add a delay to this animator and return it
     * @param delay ammount in milliseconds
     * @return this animator
     */
    public BaseAnimation addDelay(long delay) {
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
