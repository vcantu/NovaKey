package viviano.cantu.novakey.controller.touch;

import android.view.MotionEvent;

import viviano.cantu.novakey.elements.boards.Board;
import viviano.cantu.novakey.controller.Controller;

/**
 * Created by Viviano on 6/12/2016.
 */
public abstract class AreaCrossedHandler implements TouchHandler {

    private float currX, currY;
    private int currArea, prevArea;

    protected final Board mBoard;
    public AreaCrossedHandler(Board board) {
        mBoard = board;
    }

    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event current touch event
     * @param controller view being acted on
     * @param manager use this to switch handlers
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller controller, HandlerManager manager) {
        currX = event.getX(0);
        currY = event.getY(0);
        currArea = mBoard.getArea(currX, currY);

        boolean result = true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                result = result & onDown(currX, currY, currArea, controller, manager);
                prevArea = currArea;
                break;
            case MotionEvent.ACTION_MOVE:
                result = result & onMove(currX, currY, controller, manager);
                if (currArea != prevArea) {
                    result = result & onCross(new CrossEvent(currArea, prevArea), controller, manager);
                    prevArea = currArea;
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
    protected boolean onDown(float x, float y, int area, Controller controller, HandlerManager manager) {
        return true;
    }

    /**
     * Override this to specify onMove behaviour
     * @param x current x position
     * @param y current y position
     * @param controller controller used for context
     * @param manager use this to switch handlers
     */
    protected boolean onMove(float x, float y, Controller controller, HandlerManager manager) {
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param event describes the event
     * @param controller controller used for context
     * @param manager use this to switch handlers
     */
    protected abstract boolean onCross(CrossEvent event, Controller controller, HandlerManager manager);


    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller controller used for context
     * @param manager use this to switch handlers
     */
    protected abstract boolean onUp(Controller controller, HandlerManager manager);

}
