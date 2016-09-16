package viviano.cantu.novakey.controller.input;


import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.model.states.InputState;
import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 9/15/2016.
 */
public class KeyInput implements Inputable {

    private static char[] openers = new char[] { '¿', '¡', '⌊', '⌈' },
                         closers = new char[] { '?', '!', '⌋', '⌉' };

    private static char getCloser(int c) {
        for (int i=0; i<openers.length; i++) {
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

    private static int[] quickOpeners = new int[] { '(', '[', '{', '<', '>', '|', '\"' },// the | is used for absolute
                         quickClosers = new int[] { ')', ']', '}', '>', '<', '|', '\"' };//  value so its in
    private static int getQuickCloser(int c) {
        for (int i=0; i<openers.length; i++) {
            if (openers[i] == c)
                return closers[i];
        }
        return -1;
    }
    private static boolean isQuickOpener(int c) {
        for (int i : openers) {
            if (i == c)
                return true;
        }
        return false;
    }

    private final Character mChar;

    public KeyInput(Character character) {
        mChar = character;
    }


    /**
     * Inputs this object
     *
     * @param state      state for context
     * @param shiftState shift state for context
     * @return a side effect to this input action
     */
    @Override
    public Action input(InputState state, ShiftState shiftState) {
        boolean regText = state.onPassword() || state.onEmailAddress();

        if (!regText && Settings.autoCorrect
                && (Character.isLetter(mChar) || Util.isNumber(mChar))) {
            //add/insert text in current composing
        }
        else if (!regText && Settings.quickInsert && isOpener(mChar)) {
            //commit composing
            //input mChar normally
            //input closer with cursor behind
        }
        else if (!regText && Settings.quickClose && isQuickOpener(mChar)) {
            if (state.repeatCount )
            //commit composing
        }
        else {
            //commit composing
            //input mChar normally
        }
        return null;
    }
}
