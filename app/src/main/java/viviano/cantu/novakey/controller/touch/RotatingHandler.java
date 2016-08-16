package viviano.cantu.novakey.controller.touch;

import android.view.MotionEvent;

import viviano.cantu.novakey.elements.boards.Board;
import viviano.cantu.novakey.controller.Controller;

/**
 * Created by Viviano on 6/15/2016.
 */
public abstract class RotatingHandler implements TouchHandler {

    private float currX, currY;
    private int currArea, prevArea;
    private int currSector, prevSector;

    protected final Board mBoard;
    public RotatingHandler(Board board) {
        mBoard = board;
    }

    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event current touch event
     * @param controller  view being acted on
     * @param manager
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller controller, HandlerManager manager) {
        currX = event.getX(0);
        currY = event.getY(0);
        currArea = mBoard.getArea(currX, currY);
        currSector = mBoard.getSector(currX, currY);

        boolean result = true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                result = result & onDown(currX, currY, currArea, controller, manager);
                prevArea = currArea;
                prevSector = currSector;
                break;
            case MotionEvent.ACTION_MOVE:
                result = result & onMove(currX, currY, controller, manager);
                if (currArea != prevArea) {
                    if (currArea == 0)
                        result = result & onCenterCross(true, controller, manager);
                    else if (prevArea == 0)
                        result = result & onCenterCross(false, controller, manager);
                    prevArea = currArea;
                }
                if (currSector != prevSector) {
                    boolean clockwise = !((prevSector < currSector
                            && !(prevSector == 1 && currSector == 5))
                            || (prevSector == 5 && currSector == 1));
                    result = result & onRotate(clockwise, currArea == 0, controller, manager);
                    prevSector = currSector;
                }
                break;
            case MotionEvent.ACTION_UP:
                result = result & onUp(controller, manager);
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
     * @param manager use this to switch handlers
     */
    protected boolean onDown(float x, float y, int area,
                             Controller controller, HandlerManager manager) {
        return true;
    }

    /**
     * Called when the user enters or exits the inner circle.
     * Call unrelated to onMove()
     * @param entered true if event was triggered by entering the
     *                inner circle, false if was triggered by exit
     * @param controller provides context
     * @param manager allows for switching the handler
     */
    protected abstract boolean onCenterCross(boolean entered,
                                             Controller controller, HandlerManager manager);

    /**
     * Called for every move event so that the handler can update
     * display properly. Called before onRotate()
     * @param x current finger x position
     * @param y current finger y position
     * @param controller provides context
     * @param manager allows for switching the handler
     */
    protected boolean onMove(float x, float y, Controller controller, HandlerManager manager) {
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param clockwise true if rotation is clockwise, false otherwise
     * @param inCenter if finger position is currently in the center
     * @param controller provides context
     * @param manager allows for switching the handler
     */
    protected abstract boolean onRotate(boolean clockwise, boolean inCenter,
                                        Controller controller, HandlerManager manager);


    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller provides context
     * @param manager allows for switching the handler
     */
    protected abstract boolean onUp(Controller controller, HandlerManager manager);
}
