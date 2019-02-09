/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.elements.keyboards.overlays.menus;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.view.MotionEvent;

import java.util.List;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.touch.AreaCrossedHandler;
import viviano.cantu.novakey.controller.touch.CrossEvent;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.elements.keyboards.overlays.OverlayElement;
import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.core.utils.Util;
import viviano.cantu.novakey.core.utils.drawing.drawables.Drawable;
import viviano.cantu.novakey.core.utils.drawing.drawables.TextDrawable;
import viviano.cantu.novakey.view.themes.MasterTheme;
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
     *
     * @param model  for context
     * @param theme  used to determine the color
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        MainDimensions d = model.getMainDimensions();
        BoardTheme bt = theme.getBoardTheme();
        float x = d.getX();
        float y = d.getY();
        float r = d.getRadius();
        float sr = d.getSmallRadius();

        float dist = (r - sr) / 2 + sr;
        double a = Math.PI / 2 - Math.PI * 2 / 5 / 2;
        for (int j = 0; j < mEntries.size(); j++) {
            a += Math.PI * 2 / 5;
            draw(j, x + (float) Math.cos(a) * dist, y - (float) Math.sin(a) * dist,
                    sr, dist, bt, canvas);
        }
    }


    private void draw(int index, float x, float y, float size, float dist,
                      BoardTheme theme, Canvas canvas) {
        size *= Math.pow(dist / Util.distance(x, y, fingerX, fingerY), 1.0 / 3);
        size = size >= dist * 5 / 6 ? dist * 5 / 6 : size;

        //TODO: abstract this behavior(same in infinite menu)
        Object o = mEntries.get(index).data;
        if (o == null)
            return;
        if (o instanceof Drawable)
            theme.drawItem((Drawable) o, x, y, size, canvas);
        else {
            String s = "";
            if (o instanceof Character)
                s = Character.toString((Character) o);
            else
                try {
                    s = s.toString();
                } catch (Exception e) {
                }
            theme.drawItem(new TextDrawable(s), x, y, size, canvas);
        }
    }


    @Override
    public boolean handle(MotionEvent event, Controller control) {
        return mHandler.handle(event, control);
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
                        control.fire(((OnUpMenu.Entry) entry).longPress);
                }
            }.start();
        }


        private void cancelTimer() {
            if (mTimer != null)
                mTimer.cancel();
        }


        /**
         * Override this to specify onMove behaviour
         *
         * @param x          current x position
         * @param y          current y position
         * @param controller view being called on
         */
        @Override
        protected boolean onMove(float x, float y, Controller controller) {
            fingerX = x;
            fingerY = y;
            controller.invalidate();
            return true;
        }


        /**
         * Called when the touch listener detects that there
         * has been a cross, either in sector or range
         *
         * @param event      describes the event
         * @param controller view being called on
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
         *
         * @param controller view being called on
         */
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
