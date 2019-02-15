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

package viviano.cantu.novakey.core.controller.touch;

/**
 * TODO: add velocityX & velocityY to crossEvent
 * Created by Viviano on 6/13/2016.
 */
public class CrossEvent {

    public final int newArea, prevArea;


    public CrossEvent(int newArea, int prevArea) {
        this.newArea = newArea;
        this.prevArea = prevArea;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CrossEvent))
            return false;
        CrossEvent that = (CrossEvent) o;
        return this.newArea == that.newArea &&
                this.prevArea == that.prevArea;
    }


    @Override
    public int hashCode() {
        return newArea * 31 + prevArea;
    }
}
