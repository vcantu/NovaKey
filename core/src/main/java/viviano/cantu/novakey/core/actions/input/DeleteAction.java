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

package viviano.cantu.novakey.core.actions.input;

import android.view.KeyEvent;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.core.controller.Controller;
import viviano.cantu.novakey.core.actions.Action;
import viviano.cantu.novakey.core.NovaKeyService;
import viviano.cantu.novakey.core.model.InputState;
import viviano.cantu.novakey.core.model.Model;
import viviano.cantu.novakey.core.utils.Predicate;

/**
 * Created by Viviano on 6/15/2016.
 * <p>
 * Action performs a delete action
 * Returns the text that is deleted as a String
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
     *  @param ime
     * @param control
     * @param model
     */
    @Override
    public String trigger(NovaKeyService ime, Controller control, Model model) {
        InputState is = model.getInputState();
        // If not a single cursor then perform a delete key action
        if (is.getSelectionStart() != is.getSelectionEnd()) {
            String selected = ime.getSelectedText();
            ime.commitComposingText();

            if (mForward)
                ime.sendDownUpKeyEvents(KeyEvent.KEYCODE_FORWARD_DEL);
            else
                ime.sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);
            return selected;
        }
        // Otherwise handle a delete fast or slow
        else {
            Predicate<Character> slow = character -> true,
                    fast = character -> character.charValue() == ' ';
            return handleDelete(ime, control, model, !mForward, mFast ? fast : slow, true);
        }
    }


    /**
     * Call this to delete until a predicate is reached
     *
     *
     * @param ime
     * @param backspace true if deleting to left, false if deleting to right
     * @param until     will delete until this predicate is reached
     * @param included  true if it should delete the character which made it stop
     * @return the deleted string
     */
    public String handleDelete(NovaKeyService ime, Controller control, Model model,
                               boolean backspace, Predicate<Character> until, boolean included) {
        // add deleted character to temporary memory so it can be added

        InputConnection ic = ime.getCurrentInputConnection();
        if (ic == null)
            return "";

        StringBuilder sb = new StringBuilder();
        ExtractedText et = ime.getExtractedText();
        String text = (String) et.text;
        String left = text.substring(0, et.selectionStart);
        String right = text.substring(et.selectionEnd);

        char curr = 0;
        if (backspace) {
            if (left.length() > 0)
                curr = left.charAt(left.length() - 1);
        } else {
            if (right.length() > 0)
                curr = right.charAt(0);
        }

        int soFar = 1;

        while (!until.test(curr) && curr != 0) {
            if (backspace)
                sb.insert(0, curr);
            else
                sb.append(curr);

            curr = 0;
            if (backspace) {
                if (left.length() - soFar > 0)
                    curr = left.charAt(left.length() - 1 - soFar);
            } else {
                if (right.length() - soFar > 0)
                    curr = right.charAt(soFar);
            }
            soFar++;
        }
        if (included && curr != 0) {
            if (backspace)
                sb.insert(0, curr);
            else
                sb.append(curr);
        }

        ic.finishComposingText();
        model.getInputState().clearComposingText();
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
