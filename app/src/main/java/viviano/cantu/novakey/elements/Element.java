package viviano.cantu.novakey.elements;

import android.graphics.Canvas;

import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * An element is anything that can be drawn on the view.
 * Note: for the element to perform any real changes
 * it must fire an action.
 *
 * Created by Viviano on 6/17/2016.
 */
public interface Element extends TouchHandler {

    /**
     * Draws the element.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *
     * @param model
     * @param theme theme for drawing properties
     * @param canvas canvas to draw on
     */
    void draw(Model model, MasterTheme theme, Canvas canvas);

}
