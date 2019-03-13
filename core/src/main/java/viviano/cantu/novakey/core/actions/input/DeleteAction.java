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
public class DeleteAction implements Action {

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
    public void trigger(NovaKeyService ime, Controller control, Model model) {
        InputState state = model.getInputState();
        // If not a single cursor then perform a delete key action
        if (!state.oneCursor()) {
            String selected = ime.getSelectedText();
            ime.commitComposingText();

            if (mForward)
                ime.sendDownUpKeyEvents(KeyEvent.KEYCODE_FORWARD_DEL);
            else
                ime.sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);
        }
        // Otherwise handle a delete fast or slow
        else {
            Predicate<Character> until = mFast
                    ? (character -> character == ' ') : (character -> true);
            String deletedText = ime.deleteText(until, !mForward);
            control.fire(new UpdateShiftAction());
        }
    }
}
