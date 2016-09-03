package viviano.cantu.novakey.model;

import android.view.inputmethod.EditorInfo;

import viviano.cantu.novakey.model.elements.keyboards.Keyboard;
import viviano.cantu.novakey.model.states.InputState;
import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.model.states.UserState;

/**
 * Created by Viviano on 6/10/2016.
 *
 * Model interface which contains variables which can change throughout a typing session
 */
public interface StateModel {

    /**
     * @return the current input state
     */
    InputState getInputState();

    /**
     * Uses the given editor info to update the input state
     *
     * @param editorInfo info used to generate input state
     */
    void onStart(EditorInfo editorInfo);

    /**
     * @return the key layout that should be drawn
     */
    Keyboard getKeyboard();

    /**
     * @return the code/index of the current keyboard
     */
    int getKeyboardCode();

    /**
     * @param code key layout code
     */
    void setKeyboard(int code);

    /**
     * @return the shift state of the keyboard
     */
    ShiftState getShiftState();

    /**
     * @param shiftState the shiftState to set the keyboard to
     */
    void setShiftState(ShiftState shiftState);

    /**
     * @return the general action the user is doing
     */
    UserState getUserState();

    /**
     * @param userState the user state to set
     */
    void setUserState(UserState userState);

    /**
     * if cursor mode is 0 both the left and the right are moving,
     * if cursor mode is -1 the left only is moving,
     * if cursor mode is 1 the right only is moving
     *
     * @return cursor mode
     */
    int getCursorMode();

    /**
     * if cursor mode is 0 both the left and the right are moving,
     * if cursor mode is -1 the left only is moving,
     * if cursor mdoe is 1 the right only is moving
     *
     * @param cursorMode cursor mode to set
     * @throws IllegalArgumentException if the param is outside the range [-1, 1]
     */
    void setCursorMode(int cursorMode);

}
