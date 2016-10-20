package viviano.cantu.novakey.controller.actions;

import android.content.ClipData;
import android.view.inputmethod.ExtractedText;
import android.widget.Toast;

import viviano.cantu.novakey.Clipboard;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.input.InputAction;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.InputState;

/**
 * Created by Viviano on 6/16/2016.
 */
public class ClipboardAction implements Action<String> {

    private final int mAction;

    public ClipboardAction(int action) {
        mAction = action;
    }

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public String trigger(NovaKey ime, Controller control, Model model) {
        ExtractedText eText = model.getInputState().getExtractedText();
        InputState state = model.getInputState();
        
        // copy/cut
        if (mAction == Clipboard.COPY || mAction == Clipboard.CUT) {
            String text = state.getSelectedText();
            if (copy(text, ime)) {
                // cut
                if (mAction == Clipboard.CUT) {
                    state.inputText("", 0);
                }
                control.fire(new ShowToastAction("Text Copied", Toast.LENGTH_SHORT));
            }
        }
        // paste
        else if (mAction == Clipboard.PASTE) {
            String text = ime.getClipboard().getPrimaryClip()
                    .getItemAt(ime.getClipboard().getPrimaryClip().getItemCount()-1)
                    .getText().toString();
            if (text != null)
                control.fire(new InputAction(text));
        }
        // select all
        else if (mAction == Clipboard.SELECT_ALL) {
            int end = eText.text.length();
            state.setSelection(0, end);
        }
        // deselect all
        else if (mAction == Clipboard.DESELECT_ALL) {
            int i = control.getModel().getCursorMode() <= 0
                    ? eText.selectionEnd : eText.selectionStart;
            state.setSelection(i, i);
        }
        return null;
    }

    //Returns true if copy was successful
    public boolean copy(String text, NovaKey ime) {
        if (text.length() > 0) {
            ClipData cd = ClipData.newPlainText("text", text);
            ime.getClipboard().setPrimaryClip(cd);
            return true;
        }
        return false;
    }
}
