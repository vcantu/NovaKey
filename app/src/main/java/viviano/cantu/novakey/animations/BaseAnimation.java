package viviano.cantu.novakey.animations;

import android.animation.ValueAnimator;

import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 9/28/2015.
 */
public abstract class BaseAnimation implements Animation{

    private long mDelay = 0;
    private ValueAnimator mAnim;
    private OnEndListener mOnEnd;

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
            if (mOnEnd != null)
                mOnEnd.onEnd();
            return;
        }
        mAnim.setStartDelay(mAnim.getStartDelay() + mDelay);

        mAnim.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) { }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (mOnEnd != null)
                    mOnEnd.onEnd();
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) { }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) { }
        });
        mAnim.start();
    }

    /**
     * @param model given to reference. Also should call
     *              model.update() to invalidate the view
     * @return ValueAnimator which will be called
     */
    protected abstract ValueAnimator animator(Model model);

    /**
     * Set the start delay of this animation
     *
     * @param delay start delay in milliseconds
     * @return this animation
     */
    @Override
    public Animation setDelay(long delay) {
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

    /**
     * @param listener set this animation's on end listener
     */
    @Override
    public void setOnEndListener(OnEndListener listener) {
        mOnEnd = listener;
    }
}
