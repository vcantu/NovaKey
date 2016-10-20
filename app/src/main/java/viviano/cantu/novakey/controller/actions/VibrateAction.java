package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;

/**
 * Created by vcantu on 9/22/16.
 */
public class VibrateAction implements Action<Void> {

    private final long mTime;

    public VibrateAction(int vibrateLevel) {
        mTime = vibrateLevel;
    }


    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     *
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, Model model) {
        ime.vibrate(mTime);
        return null;
    }
}
