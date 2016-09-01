package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 6/10/2016.
 */
public interface Action<T> {

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
     T trigger(NovaKey ime, Controller control, Model model);
}