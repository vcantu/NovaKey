package viviano.cantu.novakey.controller.touch;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.actions.ToggleCursorModeAction;
import viviano.cantu.novakey.controller.actions.UpdateSelectionAction;

/**
 * Created by Viviano on 6/15/2016.
 */
public class SelectingHandler extends RotatingHandler {

    private final Action mRight, mLeft;

    public SelectingHandler() {
        mRight = new UpdateSelectionAction(true);
        mLeft = new UpdateSelectionAction(false);
    }

    /**
     * Called when the user enters or exits the inner circle.
     * Call unrelated to onMove()
     * @param entered true if event was triggered by entering the
     *                inner circle, false if was triggered by exit
     * @param controller
     */
    @Override
    protected boolean onCenterCross(boolean entered,
                                    Controller controller) {
        if (entered)
            controller.fire(new ToggleCursorModeAction());
        return true;
    }

    /**
     * Called for every move event so that the handler can update
     * display properly. Called before onRotate()
     * @param x current finger x position
     * @param y current finger y position
     * @param controller
     */
    @Override
    protected boolean onMove(float x, float y, Controller controller) {
        //Do nothing
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param clockwise true if rotation is clockwise, false otherwise
     * @param inCenter  if finger position is currently in the center
     * @param controller
     */
    @Override
    protected boolean onRotate(boolean clockwise, boolean inCenter,
                               Controller controller) {
        if (!inCenter) {
            if (clockwise)
                controller.fire(mRight);
            else
                controller.fire(mLeft);
        }
        return true;
    }

    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller
     *
     */
    @Override
    protected boolean onUp(Controller controller) {
        controller.fire(new SetOverlayAction(controller.getModel().getKeyboard()));
        return false;
    }
}
