package viviano.cantu.novakey.model.elements.keyboards.overlays;

import android.graphics.Canvas;

import viviano.cantu.novakey.controller.touch.handlers.SelectingHandler;
import viviano.cantu.novakey.controller.touch.handlers.TouchHandler;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.drawing.Icons;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 9/2/2016.
 */
public class CursorOverlay implements OverlayElement {

    private final TouchHandler mHandler;

    public CursorOverlay(boolean clockwise) {
        mHandler = new SelectingHandler(clockwise);
    }

    /**
     * Draws the element.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *
     * @param model
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        BoardTheme board = theme.getBoardTheme();

        int cursorCode = model.getCursorMode();
        board.drawItem(Icons.cursors, model.getX(), model.getY(),
                model.getSmallRadius(), canvas);
        if (cursorCode >= 0)
            board.drawItem(Icons.cursorLeft, model.getX(), model.getY(),
                    model.getSmallRadius(), canvas);
        if (cursorCode <= 0)
            board.drawItem(Icons.cursorRight, model.getX(), model.getY(),
                    model.getSmallRadius(), canvas);
    }

    /**
     * @return a touch handler which returns true if being used
     * or false otherwise. For example if a button element is activated by being
     * clicked, if the handler detects this in the onDown event it will
     * return true. Otherwise false, in order to allow other handlers
     * to be activated
     */
    @Override
    public TouchHandler getTouchHandler() {
        return mHandler;
    }
}
