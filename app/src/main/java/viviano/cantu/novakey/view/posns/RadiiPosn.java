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
import viviano.cantu.novakey.core.utils.Util;

/**
 * RelativePosn based on both the inner and outer radius
 * takes a location and an angle where the angle is the real
 * angle based on the center of the board
 * <p>
 * and distance is the fraction in which the coordinate lies between the
 * inner and outer radius
 * <p>
 * For Example:
 * 0 would lie exactly on the inner radius
 * 1 would lie exactly on the outer radius
 * .5 would lie exactly half-way between the inner and outer radius
 * <p>
 * Anything below 0 or above 1 would be place inside the inner radius
 * or outside the outer radius, respectively, with the distance between the
 * corresponding radii being directly proportional to the difference between both
 * radii
 * this means a distance of 2 would lie exactly (2 * (outer - inner)) distance away
 * from the inner radius
 * <p>
 * Created by Viviano on 6/10/2016.
 */
public class RadiiPosn extends RelativePosn {

    private float distance;
    private double angle;


    public RadiiPosn(float distance, double angle) {
        this.distance = distance;
        this.angle = angle;
    }


    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    @Override
    public float getX(MainDimensions model) {
        float dist = model.getSmallRadius() +
                ((model.getRadius() - model.getSmallRadius()) * distance);
        return Util.xFromAngle(model.getX(), dist, angle);
    }


    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    @Override
    public float getY(MainDimensions model) {
        float dist = model.getSmallRadius() +
                (model.getRadius() - model.getSmallRadius()) * distance;
        return Util.yFromAngle(model.getY(), dist, angle);
    }
}
