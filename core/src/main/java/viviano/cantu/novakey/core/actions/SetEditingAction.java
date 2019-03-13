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

import viviano.cantu.novakey.core.controller.Controller;
import viviano.cantu.novakey.core.NovaKeyService;
import viviano.cantu.novakey.core.model.Model;
import viviano.cantu.novakey.core.view.EditView;

/**
 * Created by Viviano on 6/15/2016.
 */
public class SetEditingAction implements Action {

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
    public void trigger(NovaKeyService ime, Controller control, Model model) {
        if (mEditing) {
            //TODO: haptic feedback
            EditView editView = new EditView(ime, control);
            editView.setTheme(model.getTheme());
            ime.setInputView(editView);
            //main.setInputView(new ControlView(main));
            //main.addWindow(editView, true);
            //TODO: floating view support with settings
        } else {
            //updates the main model
            model.syncWithPrefs();
            //ime.clearWindows(); NOTE: this code was for a floating windows test
            ime.setInputView(control.getView());
        }
    }
}
