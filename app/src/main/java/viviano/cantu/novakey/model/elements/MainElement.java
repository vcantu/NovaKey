package viviano.cantu.novakey.model.elements;

import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;

import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.states.UserState;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.drawing.Icons;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 6/20/2016.
 */
public class MainElement implements Element {

    private OverlayElement mOverlay;

    public MainElement(OverlayElement overlay) {
        mOverlay = overlay;
    }

    public void setOverlay(OverlayElement overlay) {
        mOverlay = overlay;
    }

    /**
     * Draws the element.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     * @param model
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        BoardTheme board = theme.getBoardTheme();

        theme.getBackgroundTheme().drawBackground(0, 0, model.getWidth(), model.getHeight(),
                model.getX(), model.getY(), model.getRadius(), model.getSmallRadius(), canvas);

        board.drawBoard(model.getX(), model.getY(), model.getRadius(), model.getSmallRadius(), canvas);

        if (mOverlay != null)
            mOverlay.draw(model, theme, canvas);
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
        if (mOverlay == null)
            return null;
        return mOverlay.getTouchHandler();
    }



    //------------------------------------Static Helper Methods--------------------------------------

    /**
     * @param x x position
     * @param y y position
     * @return returns the current area based on the given coordinates
     */
    public static int getArea(float x, float y, Model model) {
        if (Util.distance(model.getX(), model.getY(), x, y) <= model.getSmallRadius()) //inner circle
            return 0;
        else if (Util.distance(model.getX(), model.getY(), x, y) <= model.getRadius())
            return getSector(x, y, model);
        return -1;//outside area
    }

    /**
     * @param x x position
     * @param y y position
     * @return returns the current rotational sector based on the given coordinates
     */
    public static int getSector(float x, float y, Model model) {
        return getSectorFromCenter(x, y, model.getX(), model.getY());
    }

    /*
    * Will return a number [1, 5]
    * representing which sector, the x and y is in
    * returns Keyboard.KEYCODE_CANCEL if invalid
    */
    private static int getSectorFromCenter(float x, float y, float centX, float centY) {
        x -= centX;
        y = centY - y;
        double angle = Util.getAngle(x, y);
        angle = (angle < Math.PI / 2 ? Math.PI * 2 + angle : angle);//sets angle to [90, 450]
        for (int i=0; i < 5; i++) {
            double angle1 = (i * 2 * Math.PI) / 5  + Math.PI / 2;
            double angle2 = ((i+1) * 2 * Math.PI) / 5  + Math.PI / 2;
            if (angle >= angle1 && angle < angle2)
                return i+1;
        }
        return Keyboard.KEYCODE_CANCEL;
    }
}
