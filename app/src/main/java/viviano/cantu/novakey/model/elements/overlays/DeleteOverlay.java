package viviano.cantu.novakey.model.elements.overlays;

import android.graphics.Canvas;

import viviano.cantu.novakey.controller.touch.DeleteHandler;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.elements.OverlayElement;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 9/2/2016.
 */
public class DeleteOverlay implements OverlayElement {

    private final TouchHandler mHandler;

    public DeleteOverlay(String firstDeleted) {
        mHandler = new DeleteHandler(firstDeleted);
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
        //draws nothing...currently
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
