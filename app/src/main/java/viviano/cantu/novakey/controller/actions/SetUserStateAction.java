package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.typing.DeleteAction;
import viviano.cantu.novakey.controller.touch.DeleteHandler;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.states.UserState;

/**
 * Created by Viviano on 6/15/2016.
 */
public class SetUserStateAction implements Action<Void> {

    private final UserState mUserState;

    public SetUserStateAction(UserState userState) {
        mUserState = userState;
    }
    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, Model model) {
        //TODO: userstate transitions
        model.setUserState(mUserState);
        switch (mUserState) {
            case TYPING:
                break;
            case DELETING:
                String del = control.fire(new DeleteAction());
                control.getManager().setHandler(
                        new DeleteHandler(control.getMainBoard(), del));
                break;
            case SELECTING:
                model.setCursorMode(0);
                break;
            case ON_MENU:
                break;
        }
        return null;
    }
}
