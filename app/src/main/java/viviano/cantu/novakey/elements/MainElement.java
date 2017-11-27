package viviano.cantu.novakey.elements;

import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.elements.keyboards.overlays.OverlayElement;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 6/20/2016.
 *
 * Main keyboard element, must have an overlay
 */
public class MainElement implements Element {

    private OverlayElement mOverlay;

    public MainElement(OverlayElement overlay) {
        mOverlay = overlay;
    }

    public void setOverlay(OverlayElement overlay) {
        mOverlay = overlay;
    }

    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        MainDimensions d = model.getMainDimensions();
        
        theme.getBackgroundTheme().drawBackground(0, 0, d.getWidth(), d.getHeight(),
                d.getX(), d.getY(), d.getRadius(), d.getSmallRadius(), canvas);

        theme.getBoardTheme().drawBoard(d.getX(), d.getY(),
                d.getRadius(), d.getSmallRadius(), canvas);

        if (mOverlay != null)
            mOverlay.draw(model, theme, canvas);
    }

    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event   current touch event
     * @param control controller used for context
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller control) {
        return mOverlay.handle(event, control);
    }


    //------------------------------------Static Helper Methods--------------------------------------

    /**
     * @param x x position
     * @param y y position
     * @return returns the current area based on the given coordinates
     */
    public static int getArea(float x, float y, Model model) {
        MainDimensions d = model.getMainDimensions();
        if (Util.distance(d.getX(), d.getY(), x, y) <= d.getSmallRadius()) //inner circle
            return 0;
        else if (Util.distance(d.getX(), d.getY(), x, y) <= d.getRadius())
            return getSector(x, y, model);
        return -1;//outside area
    }

    /**
     * @param x x position
     * @param y y position
     * @return returns the current rotational sector based on the given coordinates
     */
    public static int getSector(float x, float y, Model model) {
        MainDimensions d = model.getMainDimensions();
        return getSectorFromCenter(x, y, d.getX(), d.getY());
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
