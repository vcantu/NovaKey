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

package viviano.cantu.novakey.core.elements.keyboards.overlays.menus;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.core.controller.Controller;
import viviano.cantu.novakey.core.actions.SetOverlayAction;
import viviano.cantu.novakey.core.actions.input.KeyAction;
import viviano.cantu.novakey.core.controller.touch.RotatingHandler;
import viviano.cantu.novakey.core.controller.touch.TouchHandler;
import viviano.cantu.novakey.core.elements.keyboards.overlays.OverlayElement;
import viviano.cantu.novakey.core.model.MainDimensions;
import viviano.cantu.novakey.core.model.Model;
import viviano.cantu.novakey.core.utils.Util;
import viviano.cantu.novakey.core.utils.drawing.drawables.Drawable;
import viviano.cantu.novakey.core.utils.drawing.drawables.TextDrawable;
import viviano.cantu.novakey.core.view.themes.MasterTheme;
import viviano.cantu.novakey.core.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 6/16/2016.
 */
public class InfiniteMenu implements OverlayElement, Menu {

    private static List<InfiniteMenu> HIDDEN_KEYS;


    private final List<Entry> mEntries;
    private final TouchHandler mHandler;
    private float fingX, fingY;
    private int mIndex = 0;//current entry


    public InfiniteMenu(List<Menu.Entry> entries) {
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

        float size = sr * 1.3f;
        //get distance
        float distanceFromMiddle = 0;
        double angle = Util.getAngle(fingX - x, y - fingY);

        angle = (angle < Math.PI / 2 ? Math.PI * 2 + angle : angle);//sets angle to [90, 450]
        for (int i = 0; i < 5; i++) {
            double angle1 = (i * 2 * Math.PI) / 5 + Math.PI / 2;
            double angle2 = ((i + 1) * 2 * Math.PI) / 5 + Math.PI / 2;
            if (angle >= angle1 && angle < angle2) {
                distanceFromMiddle = (float) (Math.PI / 5 - (angle - angle1));//gets the difference
                // in angle
                distanceFromMiddle = distanceFromMiddle / (float) (Math.PI / 5);//gets the percentage
                break;
            }
        }

        //draw letters
        //center
        if (mEntries.size() > 0) {
            for (int i = 0; i < 4; i++) {
                //----------------------------------DRAW MIDDLE------------------------------------
                if (i == 0) {
                    float factor = (float) (distanceFromMiddle / Math.pow(2, i + 1));
                    if (Math.abs(distanceFromMiddle) < .25)//TODO: infinite menu pretty snap
                        draw(mIndex, x, y, size, bt, canvas);
                    else
                        draw(mIndex, x + r * factor, y + r * (factor * factor / 2),
                                size * (1 - Math.abs(factor)), bt, canvas);
                } else {
                    //----------------------------------DRAW RIGHT---------------------------------
                    float addTo = 0;
                    for (int j = 1; j < i + 1; j++) {
                        addTo += 1 / Math.pow(2, j);
                    }
                    float factor = (float) (addTo + (distanceFromMiddle < 0 ? 0 :
                            distanceFromMiddle / Math.pow(2, i + 1)));
                    draw(indexInBounds(mIndex + i), x + r * factor, y + r * (factor * factor / 2),
                            size * (1 - Math.abs(factor)), bt, canvas);
                    //----------------------------------DRAW LEFT----------------------------------
                    addTo = 0;
                    for (int j = 1; j < i + 1; j++) {
                        addTo -= 1 / Math.pow(2, j);
                    }
                    factor = (float) (addTo + (distanceFromMiddle >= 0 ? 0 :
                            distanceFromMiddle / Math.pow(2, i + 1)));
                    draw(indexInBounds(mIndex - i), x + r * factor, y + r * (factor * factor / 2),
                            size * (1 - Math.abs(factor)), bt, canvas);
                }
            }
        }
    }


    private int indexInBounds(int i) {
        while (i < 0) {//add length until positive
            i += mEntries.size();
        }
        return i % mEntries.size();
    }


    /*
     * Draws specific Entry based on it's index
     */
    private void draw(int index, float x, float y, float size, BoardTheme theme, Canvas canvas) {
        Object o = mEntries.get(index).data;
        if (o == null)
            return;
        if (o instanceof Drawable) {
            theme.drawItem((Drawable) o, x, y, size, canvas);
        } else {
            String s = "";
            if (o instanceof Character)
                s = Character.toString((Character) o);
            if (o instanceof String)
                s = (String) o;
            else
                try {
                    s = s.toString();
                } catch (Exception ex) {
                }

            int MAX = 12;
            if (s.length() > 4) {
                size /= 4;
                s = Util.toMultiline(s, MAX);
                if (s.split("\n").length > 6) {
                    int last = Util.nthIndexOf(s, 5, '\n');
                    if (last < 3)//Prevents a negative length in the substring method
                        last = 3;
                    s = s.substring(0, last - 3) + "...";
                }
            }
            theme.drawItem(new TextDrawable(s), x, y, size, canvas);
        }
    }


    @Override
    public boolean handle(MotionEvent event, Controller control) {
        return mHandler.handle(event, control);
    }


    /**
     * This will be the
     */
    private class Handler extends RotatingHandler {

        /**
         * Called when the user enters or exits the inner circle.
         * Call unrelated to onMove()
         *
         * @param entered    true if event was triggered by entering the
         *                   inner circle, false if was triggered by exit
         * @param controller
         */
        @Override
        protected boolean onCenterCross(boolean entered, Controller controller) {
            //do nothing
            return true;
        }


        /**
         * Called for every move event so that the handler can update
         * display properly. Called before onRotate()
         *
         * @param x          current fing x position
         * @param y          current fing y position
         * @param controller
         */
        @Override
        protected boolean onMove(float x, float y, Controller controller) {
            fingX = x;
            fingY = y;
            controller.invalidate();
            return true;
        }


        /**
         * Called when the touch listener detects that there
         * has been a cross, either in sector or range
         *
         * @param clockwise  true if rotation is clockwise, false otherwise
         * @param inCenter   if fing position is currently in the center
         * @param controller
         */
        @Override
        protected boolean onRotate(boolean clockwise, boolean inCenter, Controller controller) {
            if (clockwise) {
                mIndex--;
                if (mIndex < 0)
                    mIndex = mEntries.size() - 1;
            } else {
                mIndex++;
                if (mIndex >= mEntries.size())
                    mIndex = 0;
            }
            controller.invalidate();//because onRotate is after on move
            return true;
        }


        /**
         * Called when the user lifts fing, typically this
         * method expects a finalized action to be triggered
         * like typing a character
         *
         * @param controller
         */
        @Override
        protected boolean onUp(Controller controller) {
            controller.fire(mEntries.get(mIndex).action);
            controller.fire(new SetOverlayAction(controller.getModel().getKeyboard()));
            mIndex = 0;//reset index
            return false;
        }
    }


    /**
     * Loads the default hidden keys based on a String array
     *
     * @param arr string array containing all the hidden keys
     */
    public static void setHiddenKeys(String[] arr) {
        HIDDEN_KEYS = new ArrayList<>();
        for (String s : arr) {
            if (s.length() > 0) {
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < s.length(); i++) {
                    entries.add(new Entry(
                            s.charAt(i),
                            new KeyAction(s.charAt(i))));
                }
                HIDDEN_KEYS.add(new InfiniteMenu(entries));
                //TODO: make hidden keys not an array but a string array instead
            }
        }
    }


    /**
     * Returns the hidden keys menu based
     *
     * @param parent parent of the hidden keys
     * @return an infinite menu or null if none was found
     */
    public static InfiniteMenu getHiddenKeys(char parent) {
        for (InfiniteMenu menu : HIDDEN_KEYS) {
            if (((Character) menu.mEntries.get(0).data).charValue() == parent) {
                return menu;
            }
        }
        return null;
    }
}
