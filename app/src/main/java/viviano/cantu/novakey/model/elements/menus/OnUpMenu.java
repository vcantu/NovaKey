package viviano.cantu.novakey.model.elements.menus;

import android.graphics.Canvas;
import android.os.CountDownTimer;

import java.util.List;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.touch.AreaCrossedHandler;
import viviano.cantu.novakey.controller.touch.CrossEvent;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.elements.OverlayElement;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.drawing.drawables.TextDrawable;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 7/15/2015.
 */
public class OnUpMenu implements OverlayElement, Menu {

    private final List<Menu.Entry> mEntries;
    private final TouchHandler mHandler;
    public float fingerX, fingerY;

    public OnUpMenu(List<Menu.Entry> entries) {
        mEntries = entries;
        mHandler = new Handler();
    }


    /**
     * This draw method will be called if this is not a
     * stand alone View, otherwise this method will never be called
     * by outside sources
     * @param model for context
     * @param theme used to determine the color
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        BoardTheme bt = theme.getBoardTheme();
        float x = model.getX();
        float y = model.getY();
        float r = model.getRadius();
        float sr = model.getSmallRadius();

        float dist = (r - sr) / 2 + sr;
        double a = Math.PI / 2 - Math.PI * 2 / 5 / 2;
        for (int j=0; j<mEntries.size(); j++) {
            a += Math.PI * 2 / 5;
            draw(j, x + (float) Math.cos(a) * dist, y - (float) Math.sin(a) * dist,
                    sr, dist, bt, canvas);
        }
    }

    /**
     * WARNING: this should not instanciate a new handler
     *
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


    private void draw(int index, float x, float y, float size, float dist,
                      BoardTheme theme, Canvas canvas) {
        size *= Math.pow(dist / Util.distance(x, y, fingerX, fingerY), 1.0/3);
        size = size >= dist*5/6 ? dist*5/6 : size;

        //TODO: abstract this behavior(same in infinite menu)
        Object o = mEntries.get(index).data;
        if (o == null)
            return;
        if (o instanceof Drawable)
            theme.drawItem((Drawable) o, x, y, size, canvas);
        else {
            String s = "";
            if (o instanceof Character)
                s = Character.toString((Character)o);
            else
                try {
                    s = s.toString();
                } catch (Exception e) {}
            theme.drawItem(new TextDrawable(s), x, y, size, canvas);
        }
    }

    private class Handler extends AreaCrossedHandler {

        private CountDownTimer mTimer;
        private int mArea = 0;

        private void startTimer(Controller control) {
            mTimer = new CountDownTimer(Settings.longPressTime, Settings.longPressTime) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Menu.Entry entry = mEntries.get(mArea - 1);
                    if (entry instanceof OnUpMenu.Entry)
                        control.fire(((OnUpMenu.Entry)entry).longPress);
                }
            }.start();
        }

        private void cancelTimer() {
            if (mTimer != null)
                mTimer.cancel();
        }

        /**
         * Override this to specify onMove behaviour
         * @param x    current x position
         * @param y    current y position
         * @param controller view being called on
         */
        @Override
        protected boolean onMove(float x, float y, Controller controller) {
            fingerX = x;
            fingerY = y;
            controller.getModel().update();
            return true;
        }

        /**
         * Called when the touch listener detects that there
         * has been a cross, either in sector or range
         * @param event describes the event
         * @param controller  view being called on
         */
        @Override
        protected boolean onCross(CrossEvent event, Controller controller) {
            mTimer.cancel();
            mTimer.start();
            mArea = event.newArea;
            return true;
        }

        /**
         * Called when the user lifts finger, typically this
         * method expects a finalized action to be triggered
         * like typing a character
         * @param controller view being called on
         * */
        @Override
        protected boolean onUp(Controller controller) {
            mTimer.cancel();
            controller.fire(mEntries.get(mArea - 1).action);
            controller.fire(new SetOverlayAction(controller.getModel().getKeyboard()));
            return false;
        }
    }

    public static class Entry extends Menu.Entry {
        public final Action longPress;

        public Entry(Object data, Action action, Action longPress) {
            super(data, action);
            this.longPress = longPress;
        }
    }
}
