/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.core.actions;

import android.content.ClipData;
import android.view.inputmethod.ExtractedText;
import android.widget.Toast;

import viviano.cantu.novakey.core.Clipboard;
import viviano.cantu.novakey.core.controller.Controller;
import viviano.cantu.novakey.core.actions.input.InputAction;
import viviano.cantu.novakey.core.NovaKeyService;
import viviano.cantu.novakey.core.model.InputState;
import viviano.cantu.novakey.core.model.Model;

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
     *  @param ime
     * @param control
     * @param model
     */
    @Override
    public String trigger(NovaKeyService ime, Controller control, Model model) {
        ExtractedText eText = ime.getExtractedText();
        InputState state = model.getInputState();

        // copy/cut
        if (mAction == Clipboard.COPY || mAction == Clipboard.CUT) {
            String text = ime.getSelectedText();
            if (copy(text, ime)) {
                // cut
                if (mAction == Clipboard.CUT) {
                    ime.inputText("", 0);
                }
                control.fire(new ShowToastAction("Text Copied", Toast.LENGTH_SHORT));
            }
        }
        // paste
        else if (mAction == Clipboard.PASTE) {
            String text = ime.getClipboard().getPrimaryClip()
                    .getItemAt(ime.getClipboard().getPrimaryClip().getItemCount() - 1)
                    .getText().toString();
            if (text != null)
                control.fire(new InputAction(text));
        }
        // select all
        else if (mAction == Clipboard.SELECT_ALL) {
            int end = eText.text.length();
            ime.setSelection(0, end);
        }
        // deselect all
        else if (mAction == Clipboard.DESELECT_ALL) {
            int i = control.getModel().getCursorMode() <= 0
                    ? eText.selectionEnd : eText.selectionStart;
            ime.setSelection(i, i);
        }
        return null;
    }


    //Returns true if copy was successful
    public boolean copy(String text, NovaKeyService ime) {
        if (text.length() > 0) {
            ClipData cd = ClipData.newPlainText("text", text);
            ime.getClipboard().setPrimaryClip(cd);
            return true;
        }
        return false;
    }
}
