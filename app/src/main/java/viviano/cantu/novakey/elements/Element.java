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

import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * An element is anything that can be drawn on the view.
 * Note: for the element to perform any real changes
 * it must fire an action.
 *
 * Created by Viviano on 6/17/2016.
 */
public interface Element extends TouchHandler {

    /**
     * Draws the element.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *
     * @param model
     * @param theme theme for drawing properties
     * @param canvas canvas to draw on
     */
    void draw(Model model, MasterTheme theme, Canvas canvas);

}
