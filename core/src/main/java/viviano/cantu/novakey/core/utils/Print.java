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

package viviano.cantu.novakey.core.utils;

import android.content.SharedPreferences;
import android.view.inputmethod.ExtractedText;

import java.util.ArrayList;
import java.util.Map;

import viviano.cantu.novakey.core.model.InputState;

/**
 * Created by Viviano on 7/15/2015.
 */
public class Print {

    public static void ln(Object o) {
        try {
            System.out.println(o);
        } catch (Exception e) {
            System.out.println("print failed!");
        }
    }


    public static void angle(double a) {
        ln(Math.toDegrees(a));
    }


    public static void hex(int i) {
        ln("0x" + Integer.toHexString(i));
    }


    public static void intList(ArrayList<Integer> l) {
        String s = "";
        for (int i : l) {
            s += i + " ";
        }
        ln(s);
    }


    public static void sharedPref(SharedPreferences pref) {
        Map<String, ?> keys = pref.getAll();
        ln("-----Shared Pref-----");
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            ln(entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }


    public static void stringArr(String[] arr) {
        for (String s : arr) {
            ln(s);
        }
    }


    public static void et(ExtractedText et) {
        if (et == null)
            ln("null");
        else
            ln('"' +
                    et.text.toString().substring(0, et.selectionStart) +
                    "|" + (et.selectionEnd == et.selectionStart ? "" :
                    et.text.toString().substring(et.selectionStart, et.selectionEnd) + "|") +
                    et.text.toString().substring(et.selectionEnd));
    }
}
