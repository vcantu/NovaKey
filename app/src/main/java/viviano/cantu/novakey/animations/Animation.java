package viviano.cantu.novakey.animations;

import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 6/20/2016.
 */
public interface Animation {

    /**
     * Should start the animation
     * <p>
     * Initialize the necessary data here
     */
    void start(Model model);

    /**
     * Set the start delay of this animation
     *
     * @param delay start delay in milliseconds
     * @return this animation
     */
    Animation setDelay(long delay);

    /**
     * @param listener set this animation's on end listener
     */
    void setOnEndListener(OnEndListener listener);


    public interface OnEndListener {
        /**
         * Called when this animation ends
         */
        void onEnd();
    }
}
