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

package viviano.cantu.novakey.controller.actions;


import android.os.Handler;
import android.widget.Toast;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;

/**
 * Created by vcantu on 9/18/16.
 */
public class ShowToastAction implements Action<Void> {

    private final String mMessage;
    private final int mLength;

    public ShowToastAction(String message, int length) {
        mMessage = message;
        mLength = length;
    }

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     *
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, Model model) {
        Handler h = new Handler(ime.getMainLooper());
        h.post(() -> Toast.makeText(ime, mMessage, mLength).show());
        return null;
    }
}
