package viviano.cantu.novakey.controller.touch;


import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.typing.InputAction;

/**
 * Will input the assigned character every time one of
 * the correct Regions is entered
 *
 * Created by Viviano on 6/14/2016.
 */
public class RepeatHandler extends AreaCrossedHandler {

    private final int mFirst, mSecond;
    private final Action mAction;

    public RepeatHandler(int keyCode, int firstArea, int secondArea) {
        mFirst = firstArea;
        mSecond = secondArea;
        mAction = new InputAction(keyCode);
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     *
     * @param event describes the event
     * @param controller view being called on
     * @return
     */
    @Override
    protected boolean onCross(CrossEvent event, Controller controller) {
        if (event.newArea == mFirst ||
                event.newArea == mSecond) {
            controller.fire(mAction);//types character
        }
        else {
            return true;//false stops it
        }
        return true;
    }

    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller view being called on
     *
     */
    @Override
    protected boolean onUp(Controller controller) {
        return false;
    }
}
