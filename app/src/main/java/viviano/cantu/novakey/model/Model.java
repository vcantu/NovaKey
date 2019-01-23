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

package viviano.cantu.novakey.model;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.List;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.elements.keyboards.Keyboard;
import viviano.cantu.novakey.elements.keyboards.overlays.OverlayElement;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 6/6/2016.
 *
 * Provides all starting data
 */
public interface Model {

    /**
     * @return a list of elements where:
     * the first on the list are the first drawn.
     * Used by a view to draw, or touch listeners to send events to
     * the first handlers on the list
     */
    List<Element> getElements();

    /**
     * Replaces or adds the given element to the topmost element which lives
     * on top of the main element
     *
     * @param element element to live on top of the main element
     */
    void setOverlayElement(OverlayElement element);

    /**
     * @return main dimensions of the keyboard, update this object to update dimensions
     */
    MainDimensions getMainDimensions();

    /**
     * @return this model's theme
     */
    MasterTheme getTheme();

    /**
     * @param theme theme to set
     */
    void setTheme(MasterTheme theme);

    /**
     * Syncs the models with the user preferences
     *
     */
    void syncWithPrefs();

    /**
     * @return the current input state
     */
    InputState getInputState();

    /**
     * Uses the given editor info to update the input state
     *
     * @param editorInfo info used to generate input state
     * @param inputConnection
     */
    void onStart(EditorInfo editorInfo, InputConnection inputConnection);

    /**
     * @return the key layout that should be drawn
     */
    Keyboard getKeyboard();

    /**
     * @return the code/index of the current keyboard
     */
    int getKeyboardCode();

    /**
     * @param code key layout code
     */
    void setKeyboard(int code);

    /**
     * @return the shift state of the keyboard
     */
    ShiftState getShiftState();

    /**
     * @param shiftState the shiftState to set the keyboard to
     */
    void setShiftState(ShiftState shiftState);

    /**
     * if cursor mode is 0 both the left and the right are moving,
     * if cursor mode is -1 the left only is moving,
     * if cursor mode is 1 the right only is moving
     *
     * @return cursor mode
     */
    int getCursorMode();

    /**
     * if cursor mode is 0 both the left and the right are moving,
     * if cursor mode is -1 the left only is moving,
     * if cursor mdoe is 1 the right only is moving
     *
     * @param cursorMode cursor mode to set
     * @throws IllegalArgumentException if the param is outside the range [-1, 1]
     */
    void setCursorMode(int cursorMode);
}
