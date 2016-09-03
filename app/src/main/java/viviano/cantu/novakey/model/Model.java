package viviano.cantu.novakey.model;

import viviano.cantu.novakey.model.elements.ElementManager;

/**
 * Created by Viviano on 6/6/2016.
 */
public interface Model
        extends DrawModel, StateModel, ElementManager {

    /**
     * Update's it's update listener. Typically a view.
     * Call this, rather than invalidating a view directly to limit access &
     * to guarantee that the view attached to this model is updated accordingly.
     */
    void update();

    /**
     * Set this model's update listener
     *
     * @param listener called when th
     */
    void setUpdateListener(UpdateListener listener);

    public interface UpdateListener {
        /**
         * Called every time the model updates and it's view should be redrawn
         */
        void onUpdate();
    }
}
