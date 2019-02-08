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

package viviano.cantu.novakey.setup;

import android.widget.EditText;

/**
 * Created by Viviano on 11/3/2015.
 */
public abstract class Task {

    private String mainText, hintText;


    Task(String main, String hint) {
        this.mainText = main;
        this.hintText = hint;
    }


    public String mainText() {
        return mainText;
    }


    public String hintText() {
        return hintText;
    }


    /**
     * Used to display hint button if true
     *
     * @return true by default, override to change
     */
    boolean hasHint() {
        return true;
    }


    abstract void onTeach();


    abstract void onStart(EditText text);


    abstract boolean isComplete(String currText);


    void onEnd() {
        //Does nothing is only overriden if necessary
    }
}