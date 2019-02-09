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

package viviano.cantu.novakey.core.utils.drawing;

import viviano.cantu.novakey.core.utils.Util;

/**
 * Created by Viviano on 3/11/2016.
 */
public class ShadowDimens {

    public static ShadowDimens fromAngle(float degrees, float r) {
        float d = r * 2;
        double a = Math.toRadians(degrees);
        return new ShadowDimens(r, Util.xFromAngle(0, d, a), Util.yFromAngle(0, d, a));
    }


    public final float x, y, r;


    private ShadowDimens(float r, float x, float y) {
        this.r = r;
        this.x = x;
        this.y = y;
    }


    public ShadowDimens fromAngle(float degrees) {
        return fromAngle(degrees, this.r);
    }
}
