package viviano.cantu.novakey.controller;

import viviano.cantu.novakey.controller.actions.Action;

/**
 * Interface that can fire actions
 *
 * Created by Viviano on 6/18/2016.
 */
public interface Gun {

    /**
     * Triggers action
     *
     * @param action action to fire
     * @param <T> action return type
     * @return returns the result of the action
     */
    <T> T fire(Action<T> action);
}
