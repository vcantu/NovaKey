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

package viviano.cantu.novakey.elements.buttons;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.core.utils.drawing.drawables.Drawable;
import viviano.cantu.novakey.core.utils.drawing.shapes.Shape;
import viviano.cantu.novakey.view.posns.RelativePosn;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.button.ButtonTheme;

/**
 * Created by Viviano on 6/22/2015.
 */
public abstract class Button implements Element {

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
     *
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        MainDimensions d = model.getMainDimensions();
        ButtonTheme buttonTheme = theme.getButtonTheme();

        Shape shape = mData.getShape();
        RelativePosn posn = mData.getPosn();
        float size = mData.getSize();

        buttonTheme.drawBack(shape, posn.getX(d), posn.getY(d), size, canvas);

        if (mIcon != null)
            buttonTheme.drawIcon(mIcon, posn.getX(d), posn.getY(d), size * .7f, canvas);
    }


    /**
     * Handles the logic given a touch event and
     * a view
     *
     * @param event   current touch event
     * @param control view being acted on
     * @return true to continue action, false otherwise
     */
    @Override
    public boolean handle(MotionEvent event, Controller control) {
        Model model = control.getModel();
        MainDimensions d = model.getMainDimensions();
        Shape shape = mData.getShape();
        RelativePosn posn = mData.getPosn();
        float size = mData.getSize();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (shape.isInside(event.getX(), event.getY(),
                        posn.getX(d), posn.getY(d), size)) {
                    startLongPress(control);
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                if (!shape.isInside(event.getX(), event.getY(),
                        posn.getX(d), posn.getY(d), size)) {
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
