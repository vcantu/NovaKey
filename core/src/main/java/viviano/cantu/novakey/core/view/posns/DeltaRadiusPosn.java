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

package viviano.cantu.novakey.core.view.posns;

import viviano.cantu.novakey.core.model.MainDimensions;
import viviano.cantu.novakey.core.utils.Util;

/**
 * Created by Viviano on 9/10/2016.
 * <p>
 * Takes in an angle and a distance(in pixels) and places
 * the coordinate at a the given distance from the radius + the radius
 */
public class DeltaRadiusPosn extends RelativePosn {

    private final float mDistance;
    private final double mAngle;


    public DeltaRadiusPosn(float distance, double angle) {
        mDistance = distance;
        mAngle = angle;
    }


    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    @Override
    public float getX(MainDimensions model) {
        return Util.xFromAngle(model.getX(), model.getRadius() + mDistance, mAngle);
    }


    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    @Override
    public float getY(MainDimensions model) {
        return Util.yFromAngle(model.getY(), model.getRadius() + mDistance, mAngle);
    }
}
