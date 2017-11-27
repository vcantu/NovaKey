package viviano.cantu.novakey.controller.actions.input;


import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.InputState;
import viviano.cantu.novakey.model.ShiftState;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 9/15/2016.
 */
public class KeyAction implements Action<Void> {

    //return after space
    private final Character[] returnAfterSpace = new Character[]
            { '.', ',', ';', '&', '!', '?' };
    public boolean shouldReturnAfterSpace(Character c) {
        for (Character C : returnAfterSpace) {
            if (C == c)
                return true;
        }
        return false;
    }

    private static char[] openers = new char[] { '¿', '¡', '⌊', '⌈' },
                         closers = new char[] { '?', '!', '⌋', '⌉' };

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

    private static char[] quickOpeners = new char[] { '(', '[', '{', '<', '>', '|', '\"' },// the | is used for absolute
                         quickClosers = new char[] { ')', ']', '}', '>', '<', '|', '\"' };//  value so its in
    private static char getQuickCloser(int c) {
        for (int i = 0; i < quickOpeners.length; i++) {
            if (quickOpeners[i] == c)
                return quickClosers[i];
        }
        return (char)0;
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
        InputState state = model.getInputState();
        boolean regText = state.onPassword() || state.onEmailAddress();
        System.out.println("key action: " + mChar);

        //TODO: insert keys normally
        if (!regText && Settings.autoCorrect
                && (Character.isLetter(mChar) || Util.isNumber(mChar))) {
            System.out.println("\t normal insert");
            char c;
            if (model.getShiftState() == ShiftState.LOWERCASE) {
                c = Character.toLowerCase(mChar);
            } else {
                c = Character.toUpperCase(mChar);
            }
            state.inputText(c + "", 1);
        }
        else if (!regText && Settings.quickInsert && isOpener(mChar)) {//auto insert
            System.out.println("\t quick insert");
            state.inputText(mChar.toString(), 1);
            state.inputText(String.valueOf(getCloser(mChar)), -1);
        }
        else if (!regText && Settings.quickClose && isQuickOpener(mChar)) {
            System.out.println("\t quick close");
            switch (state.getRepeatCount()) {
                default:
                case 0:
                    state.inputText(mChar.toString(), 1);
                    break;
                case 1:
                    state.inputText(getQuickCloser(mChar) + "", 0);
                    state.moveSelection(-1, -1);
                    break;
                case 2:
                    state.deleteSurroundingText(0, 1);
                    state.inputText(mChar.toString() + mChar.toString(), 1);
                    break;
            }
        }
        else {
            System.out.println("\t else insert");
            //TODO if regular keys but not auto correcting(MAKE PRETTIER)
            char c;
            if (model.getShiftState() == ShiftState.LOWERCASE) {
                c = Character.toLowerCase(mChar);
            } else {
                c = Character.toUpperCase(mChar);
            }
            state.inputText(c + "", 1);
        }

        //side effects
        state.setReturnAfterSpace(shouldReturnAfterSpace(mChar));
        if (state.getRepeatCount() < 0 &&
                (mChar == '\'' || mChar == '"' || mChar == '¿' || mChar == '¡'))
            control.fire(new SetKeyboardAction(Keyboards.DEFAULT));

        control.fire(new UpdateShiftAction());
        return null;
    }
}
