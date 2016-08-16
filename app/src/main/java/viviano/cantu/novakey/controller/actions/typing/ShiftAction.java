package viviano.cantu.novakey.controller.actions.typing;

import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SelectionActions;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.controller.actions.SetShiftStateAction;
import viviano.cantu.novakey.model.NovaKeyModel;
import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.model.keyboards.KeyLayout;
import viviano.cantu.novakey.utils.Util;

/**
 * Performs the desired action of the user performing
 * the shift gesture. Which is, switch ShiftState & UserState
 * accordingly, and update the selected text
 *
 * Created by Viviano on 6/15/2016.
 */
public class ShiftAction implements Action<Void> {

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        String selectedText = ime.getSelectedText();
        boolean shiftText = selectedText.length() > 0;
        int s = 0, e = 0;
        if (shiftText) {
            ExtractedText et = ime.getCurrentInputConnection()
                    .getExtractedText(new ExtractedTextRequest(), 0);
            s = et.selectionStart;
            e = et.selectionEnd;
        }

        switch (model.getKeyboardCode()) {
            case KeyLayout.PUNCTUATION:
                control.fire(new SetKeyboardAction(KeyLayout.SYMBOLS));
                break;
            case KeyLayout.SYMBOLS:
                control.fire(new SetKeyboardAction(KeyLayout.PUNCTUATION));
                break;
            default:
                switch (model.getShiftState()) {
                    case LOWERCASE:
                        control.fire(new SetShiftStateAction(ShiftState.UPPERCASE));
                        if (shiftText) {//uppercase each word
                            control.fire(new InputAction(Util.uppercaseFirst(selectedText)));
                            control.fire(new SelectionActions.Set(s, e));
                        }
                        break;
                    case UPPERCASE:
                        control.fire(new SetShiftStateAction(ShiftState.CAPS_LOCKED));
                        if (shiftText) {//caps everything
                            control.fire(new InputAction(selectedText.toUpperCase()));
                            control.fire(new SelectionActions.Set(s, e));
                        }
                        break;
                    case CAPS_LOCKED:
                        control.fire(new SetShiftStateAction(ShiftState.LOWERCASE));
                        if (shiftText) {//lowercase everything
                            control.fire(new InputAction(selectedText.toLowerCase()));
                            control.fire(new SelectionActions.Set(s, e));
                        }
                        break;
                }
        }
        return null;
    }
}
