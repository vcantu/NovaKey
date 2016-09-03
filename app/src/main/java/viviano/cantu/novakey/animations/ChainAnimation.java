package viviano.cantu.novakey.animations;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 9/2/2016.
 */
public class ChainAnimation implements Animation {

    private long mDelay = 0;
    private OnEndListener mOnEnd;

    private final List<Animation> mAnimations;

    public ChainAnimation() {
        mAnimations = new ArrayList<>();
    }

    public ChainAnimation(List<Animation> animations) {
        mAnimations = animations;
    }

    public ChainAnimation add(Animation animation) {
        mAnimations.add(animation);
        return this;
    }

    /**
     * Should start the animation
     * <p>
     * Initialize the necessary data here
     *
     * @param model
     */
    @Override
    public void start(Model model) {
        mAnimations.get(0).setDelay(mDelay);
        for (int i = 0; i < mAnimations.size() - 1; i++) {
            final int finalI = i;
            mAnimations.get(i).setOnEndListener(
                    () -> mAnimations.get(finalI + 1).start(model));
        }
        mAnimations.get(mAnimations.size() - 1).setOnEndListener(mOnEnd);
        mAnimations.get(0).start(model);
    }

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
     * @param listener set this animation's on end listener
     */
    @Override
    public void setOnEndListener(OnEndListener listener) {
        mOnEnd = listener;
    }
}
