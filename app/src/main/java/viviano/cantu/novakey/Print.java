package viviano.cantu.novakey;

import android.content.SharedPreferences;
import android.util.Log;

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
}
