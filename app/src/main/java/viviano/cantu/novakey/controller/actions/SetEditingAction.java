package viviano.cantu.novakey.controller.actions;

import android.view.View;

import viviano.cantu.novakey.EditView;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Created by Viviano on 6/15/2016.
 */
public class SetEditingAction implements Action<Void> {

    private final boolean mEditing;

    /**
     * Constructs the action
     *
     * @param editing true if setting to editing, false otherwise
     */
    public SetEditingAction(boolean editing) {
        mEditing = editing;
    }

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        if (mEditing) {
            //TODO: haptic feedback
            EditView editView = new EditView(ime);
            editView.setTheme(model.getTheme());
            ime.setInputView(editView);
            //main.setInputView(new ControlView(main));
            //main.addWindow(editView, true);
            //TODO: floating view support with settings
        }
        else {
            ime.clearWindows();
            ime.setInputView(control.getView());
        }
        return null;
    }
}
