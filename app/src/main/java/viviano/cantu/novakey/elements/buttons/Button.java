package viviano.cantu.novakey.elements.buttons;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.touch.HandlerManager;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.model.properties.ButtonProperties;
import viviano.cantu.novakey.view.INovaKeyView;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.drawing.shapes.Shape;
import viviano.cantu.novakey.view.posns.RelativePosn;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 6/22/2015.
 */
public abstract class Button implements IButton, TouchHandler {

    private Drawable mIcon;
    private ButtonProperties mProperties;

    private CountDownTimer mLongPress;
    private boolean mShouldClick = true;

    /**
     * Must be called by children of this class in order to
     * set the icon of this button
     *
     * @param icon icon to set
     */
    protected final void setIcon(Drawable icon) {
        mIcon = icon;
    }

    /**
     * @return action to fire, or null if no action is needed
     */
    protected abstract Action onClickAction();

    /**
     * @return action to fire, or null if no action is needed
     */
    protected abstract Action onLongPressAction();


    /**
     * @return this class's properties
     */
    @Override
    public ButtonProperties getProperties() {
        return mProperties;
    }

    /**
     * Sets this class's properties
     *
     * @param properties properties to set
     */
    @Override
    public void setProperties(ButtonProperties properties) {
        mProperties = properties;
    }

    /**
     * Draws the button. Button must handle it's own paint.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *  @param view   view given for context
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(INovaKeyView view, MasterTheme theme, Canvas canvas) {
        //TODO: 3d button
        Shape shape = mProperties.getShape();
        RelativePosn posn = mProperties.getPosn();
        float size = mProperties.getSize();

        //TODO: get background paint
        shape.draw(posn.getX(view.getDrawModel()), posn.getY(view.getDrawModel()),
                size, p, canvas);

        //TODO get contrast paint
        if (mIcon != null)
            mIcon.draw(posn.getX(view.getDrawModel()), posn.getY(view.getDrawModel()),
                    size * .7f, p, canvas);
    }

    /**
     * @param controller provides context to the handler
     * @return a touch handler which returns true if being used
     * or false otherwise. For example if a button element is activated by being
     * clicked, if the handler detects this in the onDown event it will
     * return true. Otherwise false, in order to allow other handlers
     * to be activated
     */
    @Override
    public TouchHandler getTouchHandler(Controller controller) {
        return this;
    }

    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event current touch event
     * @param control  view being acted on
     * @param manager
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller control, HandlerManager manager) {
        Shape shape = mProperties.getShape();
        RelativePosn posn = mProperties.getPosn();
        float size = mProperties.getSize();
        DrawModel dm = control.getDrawModel();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (shape.isInside(event.getX(), event.getY(),
                        posn.getX(dm), posn.getY(dm), size)) {
                    startLongPress(control);
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                if (!shape.isInside(event.getX(), event.getY(),
                        posn.getX(dm), posn.getY(dm), size)) {
                    cancelLongPress();
                    return false;
                }
                return true;
            case MotionEvent.ACTION_UP:
                cancelLongPress();
                if (mShouldClick) {
                    Action a = onClickAction();
                    if (a != null)
                        control.fire(a);
                }
                return false;
        }
        return false;
    }

    private void startLongPress(Controller control) {
        mShouldClick = true;
        mLongPress = new CountDownTimer(Settings.longPressTime, Settings.longPressTime) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mShouldClick = false;
                Action a = onLongPressAction();
                if (a != null)
                    control.fire(a);
            }
        };
    }

    private void cancelLongPress() {
        if (mLongPress != null)
            mLongPress.cancel();
    }


//    /*
//        Btns will be saved as a Single String, each button will contain it's data and meta data
//        inside a substring of this main string, all buttons are to be separated by "|"
//     */
//    public static ArrayList<Button> btnsFromString(String s) {
//        if (s.equals(Settings.DEFAULT)) {
//            return btnsFromString(
//                    "0," + Math.PI*3/4 + "," + (1+getRadius(SMALL, 1)) + "," + (CIRCLE|SMALL) + "|" +
//                    "1," + Math.PI*1/4 + "," + (1+getRadius(SMALL, 1)) + "," + (CIRCLE|SMALL)
//
//
//            //this is space button
//            + (Settings.hasSpaceBar ?
//                    "|" + "2," + Math.PI/2 + "," + 1.16667f + "," + (ARC|LARGE)
//                    : "")
//            );
//        }
//        String[] b = s.split("[|]");
//        ArrayList<Button> B = new ArrayList<Button>();
//        for (String str : b) {
//            B.add(btnFromString(str));
//        }
//        return B;
//    }
}
