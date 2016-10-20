package viviano.cantu.novakey.controller.actions;


import android.os.Handler;
import android.widget.Toast;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;

/**
 * Created by vcantu on 9/18/16.
 */
public class ShowToastAction implements Action<Void> {

    private final String mMessage;
    private final int mLength;

    public ShowToastAction(String message, int length) {
        mMessage = message;
        mLength = length;
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
        Handler h = new Handler(ime.getMainLooper());
        h.post(() -> Toast.makeText(ime, mMessage, mLength).show());
        return null;
    }
}
