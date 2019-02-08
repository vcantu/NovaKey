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

package viviano.cantu.novakey.view.posns;

import viviano.cantu.novakey.model.MainDimensions;

/**
 * RelativePosn that returns coordinates translated
 * based on a given deltaX and deltaY(in pixels)
 * <p>
 * Created by Viviano on 6/10/2016.
 */
public class DeltaPosn extends RelativePosn {

    private float dx, dy;


    public DeltaPosn(float deltaX, float deltaY) {
        this.dx = deltaX;
        this.dy = deltaY;
    }


    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    @Override
    public float getX(MainDimensions model) {
        return model.getX() + dx;
    }


    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    @Override
    public float getY(MainDimensions model) {
        return model.getY() + dy;
    }
}
