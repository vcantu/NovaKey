package viviano.cantu.novakey.model.elements.buttons;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.elements.Element;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.drawing.shapes.Shape;
import viviano.cantu.novakey.view.posns.RelativePosn;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.button.ButtonTheme;

/**
 * Created by Viviano on 6/22/2015.
 */
public abstract class Button
        implements TouchHandler, Element {

    private Drawable mIcon;

    private CountDownTimer mLongPress;
    private boolean mShouldClick = true;

    private ButtonData mData;

    public Button(ButtonData data) {
        mData = data;
    }

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
     * Draws the button. Button must handle it's own paint.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        ButtonTheme buttonTheme = theme.getButtonTheme();

        Shape shape = mData.getShape();
        RelativePosn posn = mData.getPosn();
        float size = mData.getSize();

        buttonTheme.drawBack(shape, posn.getX(model), posn.getY(model), size, canvas);

        if (mIcon != null)
            buttonTheme.drawIcon(mIcon, posn.getX(model), posn.getY(model), size * .5f, canvas);
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
        return this;
    }

    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event current touch event
     * @param control  view being acted on
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller control) {
        Model model = control.getModel();
        Shape shape = mData.getShape();
        RelativePosn posn = mData.getPosn();
        float size = mData.getSize();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (shape.isInside(event.getX(), event.getY(),
                        posn.getX(model), posn.getY(model), size)) {
                    startLongPress(control);
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                if (!shape.isInside(event.getX(), event.getY(),
                        posn.getX(model), posn.getY(model), size)) {
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
        mLongPress.start();
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
