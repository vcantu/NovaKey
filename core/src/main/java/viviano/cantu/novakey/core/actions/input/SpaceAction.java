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

import viviano.cantu.novakey.core.controller.Controller;
import viviano.cantu.novakey.core.actions.Action;
import viviano.cantu.novakey.core.actions.SetKeyboardAction;
import viviano.cantu.novakey.core.NovaKeyService;
import viviano.cantu.novakey.core.elements.keyboards.Keyboards;
import viviano.cantu.novakey.core.model.InputState;
import viviano.cantu.novakey.core.model.Model;
import viviano.cantu.novakey.core.model.Settings;

/**
 * Created by vcantu on 9/18/16.
 */
public class SpaceAction implements Action {

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

        // If at the end of composing and should AutoCorrect
        if (!state.cursorAtComposingEnd() &&
                Settings.autoCorrect && state.shouldAutoCorrect()) {
            ime.commitCorrection();
        } else {
            ime.commitComposingText();
        }
        ime.inputText(" ", 1);

        if (state.returnAfterSpace())
            control.fire(new SetKeyboardAction(Keyboards.DEFAULT));
        state.setReturnAfterSpace(false);
        control.fire(new UpdateShiftAction());
    }
}
