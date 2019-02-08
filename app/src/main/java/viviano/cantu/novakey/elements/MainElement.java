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

package viviano.cantu.novakey.elements;

import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.view.MotionEvent;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.elements.keyboards.overlays.OverlayElement;
import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 6/20/2016.
 * <p>
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
        for (int i = 0; i < 5; i++) {
            double angle1 = (i * 2 * Math.PI) / 5 + Math.PI / 2;
            double angle2 = ((i + 1) * 2 * Math.PI) / 5 + Math.PI / 2;
            if (angle >= angle1 && angle < angle2)
                return i + 1;
        }
        return Keyboard.KEYCODE_CANCEL;
    }
}
