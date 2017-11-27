package viviano.cantu.novakey.controller.touch;

import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.elements.MainElement;

/**
 * Created by Viviano on 6/15/2016.
 */
public abstract class RotatingHandler implements TouchHandler {

    private float currX, currY;
    private int currArea, prevArea;
    private int currSector = -1, prevSector = -1;


    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event current touch event
     * @param control
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller control) {
        currX = event.getX(0);
        currY = event.getY(0);
        currArea = MainElement.getArea(currX, currY, control.getModel());
        currSector = MainElement.getSector(currX, currY, control.getModel());

        boolean result = true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                result = result & onDown(currX, currY, currArea, control);
                prevArea = currArea;
                prevSector = currSector;
                break;
            case MotionEvent.ACTION_MOVE:
                result = result & onMove(currX, currY, control);
                if (currArea != prevArea) {
                    if (currArea == 0)
                        result = result & onCenterCross(true, control);
                    else if (prevArea == 0)
                        result = result & onCenterCross(false, control);
                }
                prevArea = currArea;
                if (currSector != prevSector) {//if not initial handling
                    //to not perform onRotate on start make add prevSector != -1 to if statement
                    boolean clockwise = (prevSector - 1) == (currSector % 5);
                    result = result & onRotate(clockwise, currArea == 0, control);
                }
                prevSector = currSector;
                break;
            case MotionEvent.ACTION_UP:
                result = result & onUp(control);
                break;
        }
        return result;
    }

    /**
     * Override this to specify onDown behaviour
     * @param x current x position
     * @param y current y position
     * @param area current area
     * @param controller controller used for context
     */
    protected boolean onDown(float x, float y, int area, Controller controller) {
        return true;
    }

    /**
     * Called when the user enters or exits the inner circle.
     * Call unrelated to onMove()
     * @param entered true if event was triggered by entering the
     *                inner circle, false if was triggered by exit
     * @param controller provides context
     */
    protected abstract boolean onCenterCross(boolean entered, Controller controller);

    /**
     * Called for every move event so that the handler can update
     * display properly. Called before onRotate()
     * @param x current finger x position
     * @param y current finger y position
     * @param controller provides context
     */
    protected boolean onMove(float x, float y, Controller controller) {
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param clockwise true if rotation is clockwise, false otherwise
     * @param inCenter if finger position is currently in the center
     * @param controller provides context
     */
    protected abstract boolean onRotate(boolean clockwise, boolean inCenter, Controller controller);


    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller provides context
     *
     */
    protected abstract boolean onUp(Controller controller);
}
