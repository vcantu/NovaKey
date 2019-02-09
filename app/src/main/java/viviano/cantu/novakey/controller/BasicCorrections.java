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

package viviano.cantu.novakey.controller;

import android.content.Context;

import java.util.Locale;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.core.utils.Util;

/**
 * Created by vcantu on 9/18/16.
 */
public class BasicCorrections implements Corrections {

    String[] mContractions;


    /**
     * This is where all data should be loaded
     *
     * @param context used to load data saved on device
     */
    @Override
    public void initialize(Context context) {
        mContractions = context.getResources().getStringArray(R.array.contractions);
    }


    /**
     * TODO: this corrections method will change drastically this is just a place holder
     *
     * @param composing
     * @return
     */
    @Override
    public String correction(String composing) {


        int idx = contractionIndex(composing);
        if (idx != -1) {
            String s = mContractions[idx];
            if (composing.matches("[A-Z]+"))
                return s.toUpperCase(Locale.US);//TODO: other languages
            else if (Character.isUpperCase(composing.charAt(0)))
                return Util.capsFirst(s);
            return s;
        }
        return composing;
    }


    private int contractionIndex(String text) {
        for (int i = 0; i < mContractions.length; i++) {
            String check = mContractions[i].replace("\'", "");
            if (check.equalsIgnoreCase(text.toString()))
                return i;
        }
        return -1;
    }
}
