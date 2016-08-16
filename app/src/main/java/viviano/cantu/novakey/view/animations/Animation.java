package viviano.cantu.novakey.view.animations;

import android.view.animation.Interpolator;

/**
 * Created by Viviano on 6/20/2016.
 */
public interface Animation {

    /**
     * Called before the animation starts
     *
     * Initialize the necessary data here
     */
    void initialize();

    /**
     * Called every tick in the animation
     *
     * @param fraction 0 at the start of the animation 1 at the end
     *                 May be a number outside those bounds
     */
    void update(float fraction);
}
