package viviano.cantu.novakey.view.animations;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

import viviano.cantu.novakey.view.INovaKeyView;

/**
 * Created by Viviano on 6/11/2016.
 */
public class Animator {

    private final MultiValueAnimator<Animation> mAnimator;
    private final INovaKeyView mView;

    Animator(MultiValueAnimator<Animation> animator, INovaKeyView view) {
        mAnimator = animator;
        mView = view;
    }

    /**
     * Starts the animation
     */
    public void start() {
        for (Animation a : mAnimator.getKeys()) {
            a.initialize();
        }
        mView.update();
        mAnimator.start();
    }


    public static class Builder {

        private MultiValueAnimator<Animation> anim = MultiValueAnimator.create();
        private INovaKeyView view;

        public Builder(INovaKeyView view) {
            this.view = view;
        }


        public Builder add(Animation animation, Interpolator interpolator,
                           long delay, long duration) {
            anim.addInterpolator(animation, interpolator, delay, duration);
            return this;
        }

        public Animator build() {
            anim.setMultiUpdateListener(new MultiValueAnimator
                    .MultiUpdateListener<Animation>() {
                @Override
                public void onValueUpdate(ValueAnimator animator, float value, Animation key) {
                    key.update(value);
                }

                @Override
                public void onAllUpdate(ValueAnimator animator, float value) {
                    view.update();
                }
            });
            return new Animator(anim, view);
        }
    }
}
