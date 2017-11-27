package viviano.cantu.novakey.elements.keyboards.overlays;

import android.graphics.Canvas;
import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.touch.DeleteHandler;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.drawing.Icons;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 9/2/2016.
 */
public class DeleteOverlay implements OverlayElement {

    private final TouchHandler mHandler;
    private final Drawable mIcon;

    public DeleteOverlay() {
        mHandler = new DeleteHandler();
        mIcon = Icons.get("backspace");
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
        MainDimensions d = model.getMainDimensions();
        theme.getBoardTheme().drawItem(mIcon, d.getX(), d.getY(),
                d.getSmallRadius() * .8f, canvas);
    }

    @Override
    public boolean handle(MotionEvent event, Controller control) {
        return mHandler.handle(event, control);
    }
}
