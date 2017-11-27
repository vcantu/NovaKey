package viviano.cantu.novakey.controller.touch;

import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.elements.MainElement;

/**
 * Created by Viviano on 6/12/2016.
 */
public abstract class AreaCrossedHandler implements TouchHandler {

    private float currX, currY;
    private int currArea, prevArea;

    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event current touch event
     * @param control view being acted on
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller control) {
        currX = event.getX(0);
        currY = event.getY(0);
        currArea = MainElement.getArea(currX, currY, control.getModel());

        boolean result = true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                result = result & onDown(currX, currY, currArea, control);
                prevArea = currArea;
                break;
            case MotionEvent.ACTION_MOVE:
                result = result & onMove(currX, currY, control);
                if (currArea != prevArea) {
                    result = result & onCross(new CrossEvent(currArea, prevArea), control);
                    prevArea = currArea;
                }
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
     * Override this to specify onMove behaviour
     * @param x current x position
     * @param y current y position
     * @param controller controller used for context
     */
    protected boolean onMove(float x, float y, Controller controller) {
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param event describes the event
     * @param controller controller used for context
     */
    protected abstract boolean onCross(CrossEvent event, Controller controller);


    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller controller used for context
     *
     */
    protected abstract boolean onUp(Controller controller);

}
