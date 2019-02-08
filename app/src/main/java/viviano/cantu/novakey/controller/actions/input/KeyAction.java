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

package viviano.cantu.novakey.controller.actions.input;


import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.InputState;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.model.ShiftState;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 9/15/2016.
 */
public class KeyAction implements Action<Void> {

    //return after space
    private final Character[] returnAfterSpace = new Character[]
            {'.', ',', ';', '&', '!', '?'};


    public boolean shouldReturnAfterSpace(Character c) {
        for (Character C : returnAfterSpace) {
            if (C == c)
                return true;
        }
        return false;
    }


    private static char[] openers = new char[]{'¿', '¡', '⌊', '⌈'},
            closers = new char[]{'?', '!', '⌋', '⌉'};


    private static char getCloser(int c) {
        for (int i = 0; i < openers.length; i++) {
            if (openers[i] == c)
                return closers[i];
        }
        return 0;
    }


    private static boolean isOpener(int c) {
        for (int i : openers) {
            if (i == c)
                return true;
        }
        return false;
    }


    private static char[] quickOpeners = new char[]{'(', '[', '{', '<', '>', '|', '\"'},// the | is used for absolute
            quickClosers = new char[]{')', ']', '}', '>', '<', '|', '\"'};//  value so its in


    private static char getQuickCloser(int c) {
        for (int i = 0; i < quickOpeners.length; i++) {
            if (quickOpeners[i] == c)
                return quickClosers[i];
        }
        return (char) 0;
    }


    private static boolean isQuickOpener(int c) {
        for (int i : quickOpeners) {
            if (i == c)
                return true;
        }
        return false;
    }


    private final Character mChar;


    public KeyAction(Character character) {
        mChar = character;
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
        InputConnection ic = ime.getCurrentInputConnection();
        InputState state = model.getInputState();
        boolean regText = state.shouldAutoCorrect();

        //TODO: insert keys normally
        // Regular text with autocorrecting
        if (!regText && Settings.autoCorrect && (Character.isLetter(mChar) || Util.isNumber(mChar))) {
            char c;
            if (model.getShiftState() == ShiftState.LOWERCASE) {
                c = Character.toLowerCase(mChar);
            } else {
                c = Character.toUpperCase(mChar);
            }
            ime.inputText(Character.toString(c), 1);
        }
        // Quick Insert
        else if (!regText && Settings.quickInsert && isOpener(mChar)) {
            ime.inputText(mChar.toString(), 1);
            ime.inputText(String.valueOf(getCloser(mChar)), -1);
        }
        // Quick Close
        else if (!regText && Settings.quickClose && isQuickOpener(mChar)) {
            switch (state.getRepeatCount()) {
                default:
                case 0:
                    ime.inputText(mChar.toString(), 1);
                    break;
                case 1:
                    ime.inputText(getQuickCloser(mChar) + "", 0);
                    ime.moveSelection(-1, -1);
                    break;
                case 2:
                    ic.deleteSurroundingText(0, 1);
                    ime.inputText(mChar.toString() + mChar.toString(), 1);
                    break;
            }
        } else {
            //TODO if regular keys but not auto correcting(MAKE PRETTIER)
            char c;
            if (model.getShiftState() == ShiftState.LOWERCASE) {
                c = Character.toLowerCase(mChar);
            } else {
                c = Character.toUpperCase(mChar);
            }
            ime.inputText(Character.toString(c), 1);
        }

        //side effects
        state.setReturnAfterSpace(shouldReturnAfterSpace(mChar));
        if (state.getRepeatCount() <= 0 &&
                (mChar == '\'' || mChar == '"' || mChar == '¿' || mChar == '¡'))
            control.fire(new SetKeyboardAction(Keyboards.DEFAULT));

        control.fire(new UpdateShiftAction());
        return null;
    }
}
