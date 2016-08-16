package viviano.cantu.novakey.controller.actions.typing;

import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.model.NovaKeyModel;
import viviano.cantu.novakey.utils.Pred;

/**
 * Created by Viviano on 6/15/2016.
 */
public class DeleteAction implements Action<String> {

    private final boolean mForward, mFast;

    public DeleteAction() {
        this(false);
    }

    public DeleteAction(boolean forwards) {
        this(forwards, false);
    }

    public DeleteAction(boolean forwards, boolean fast) {
        mForward = forwards;
        mFast = fast;
    }

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public String trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        Pred<Character> slow = character -> true,
                fast = character -> character.charValue() == ' ';
        return handleDelete(ime, control, model,
                !mForward, mFast ? fast : slow, true);
    }

    /**
     * Call this to delete until a predicate is reached
     *
     * @param backspace true if deleting to left, false if deleting to right
     * @param until will delete until this predicate is reached
     * @param included true if it should delete the character which made it stop
     * @return the deleted string
     */
    public String handleDelete(NovaKey ime, Controller control, NovaKeyModel model,
                               boolean backspace, Pred<Character> until, boolean included) {
        // add deleted character to temporary memory so it can be added
        InputConnection ic = ime.getCurrentInputConnection();
        if (ic == null)
            return "";

        StringBuilder sb = new StringBuilder();

        ExtractedText et = ic.getExtractedText(new ExtractedTextRequest(), 0);
        String text = (String) et.text;
        String back = text.substring(0, et.selectionStart);
        String front = text.substring(et.selectionEnd);

        char curr = 0;
        if (backspace) {
            if (back.length() > 0)
                curr = back.charAt(back.length() - 1);
        } else {
            if (front.length() > 0)
                curr = front.charAt(0);
        }

        int soFar = 1;

        while (!until.apply(curr) && curr != 0) {
            if (backspace)
                sb.insert(0, curr);
            else
                sb.append(curr);

            curr = 0;
            if (backspace) {
                if (back.length() - soFar > 0)
                    curr = back.charAt(back.length() - 1 - soFar);
            } else {
                if (front.length() - soFar > 0)
                    curr = front.charAt(soFar);
            }
            soFar++;
        }
        if (included && curr != 0) {
            if (backspace)
                sb.insert(0, curr);
            else
                sb.append(curr);
        }

        ime.commitComposing();
        if (sb.length() >= 1) {
            if (backspace)
                ic.deleteSurroundingText(sb.length(), 0);
            else
                ic.deleteSurroundingText(0, sb.length());
        }
        control.fire(new UpdateShiftAction());
        return sb.toString();
    }
}
