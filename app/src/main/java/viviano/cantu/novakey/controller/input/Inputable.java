package viviano.cantu.novakey.controller.input;

import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.model.states.InputState;
import viviano.cantu.novakey.model.states.ShiftState;

/**
 * Created by Viviano on 9/11/2016.
 */
public interface Inputable {

    /**
     * Inputs this object
     *
     * @param state state for context
     * @param shiftState shift state for context
     * @return a side effect to this input action
     */
    Action input(InputState state, ShiftState shiftState);
}
