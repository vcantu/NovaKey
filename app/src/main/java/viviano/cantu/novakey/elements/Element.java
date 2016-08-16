package viviano.cantu.novakey.elements;

import android.graphics.Canvas;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.view.INovaKeyView;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

/**
 * An element is anything that can be drawn on the view.
 * Note: for the element to perform any real changes
 * it must fire an action.
 *
 * Created by Viviano on 6/17/2016.
 */
public interface Element {

    /**
     * Draws the element.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     * @param view view given for context. Use this for access to the model
     * @param theme theme for drawing properties
     * @param canvas canvas to draw on
     */
    void draw(INovaKeyView view, MasterTheme theme, Canvas canvas);

    /**
     * @param controller provides context to the handler
     * @return a touch handler which returns true if being used
     * or false otherwise. For example if a button element is activated by being
     * clicked, if the handler detects this in the onDown event it will
     * return true. Otherwise false, in order to allow other handlers
     * to be activated
     */
    TouchHandler getTouchHandler(Controller controller);
}
