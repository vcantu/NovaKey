package viviano.cantu.novakey.utils;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.inputmethod.ExtractedText;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Viviano on 7/15/2015.
 */
public class Print {

    public static void ln(Object o) {
        System.out.println(o);
    }

    public static void angle(double a) {
        System.out.println(Math.toDegrees(a));
    }

    public static void hex(int i) {
        System.out.println("0x" + Integer.toHexString(i));
    }

    public static void intList(ArrayList<Integer> l) {
        String s = "";
        for (int i : l) {
            s += i + " ";
        }
        System.out.println(s);
    }

    public static void sharedPref(SharedPreferences pref) {
        Map<String,?> keys = pref.getAll();
        System.out.println("-----Shared Pref-----");
        for(Map.Entry<String,?> entry : keys.entrySet()){
            System.out.println(entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }

    public static void stringArr(String[] arr) {
        for (String s : arr) {
            ln(s);
        }
    }

    public static void tryln(Object o) {
        try {
            ln(o);
        } catch (Exception e) {
            ln("try print failed!");
        }
    }

    public static void extText(ExtractedText et) {
        ln('"' +
                et.text.toString().substring(0, et.selectionStart) +
                "|" + (et.selectionEnd == et.selectionEnd ? "" :
                et.text.toString().substring(et.selectionStart, et.selectionEnd) + "|") +
                et.text.toString().substring(et.selectionEnd));
    }
}
