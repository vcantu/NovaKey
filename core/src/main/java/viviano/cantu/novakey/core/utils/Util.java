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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Viviano on 7/4/2015.
 */
public class Util {

    //----------------------------------------Number Utils---------------------------------------
    public static int bounded(int value, int min, int max) {
        return Math.min(max, Math.max(min, value));
    }

    //----------------------------------------Trig Utils---------------------------------------
    //returns distance between two points
    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }


    public static RectF square(float x, float y, float r) {
        return new RectF(x - r, y - r, x + r, y + r);
    }


    public static double angle(float cx, float cy, float x, float y) {
        return Math.atan2(y - cy, x - cx);
    }


    //Gets angle from 0, 0
    public static float getAngle(float x, float y) {
        double angle = Math.atan2(y, x);
        return (float) (angle < 0 ? Math.PI * 2 + angle : angle);
    }


    public static float xFromAngle(float cx, float r, double a) {
        return cx + (float) (Math.cos(a) * r);
    }


    public static float yFromAngle(float cy, float r, double a) {
        return cy - (float) (Math.sin(a) * r);
    }


    //----------------------------String utils---------------------------------------
    public static String capsFirst(String text) {
        if (text.length() < 0)
            return text;
        Character first = text.charAt(0);
        first = Character.toUpperCase(first);
        try {
            return first + text.substring(1);
        } catch (Exception e) {
            return first.toString();
        }
    }


    public static String uppercaseFirst(String text) {
        String res = "";
        for (int i = 0; i < text.length(); i++) {
            if (i == 0 || text.charAt(i - 1) == ' ')
                res += Character.toUpperCase(text.charAt(i));
            else
                res += text.charAt(i);
        }
        return res;
    }


    public static int nthIndexOf(String s, int n, char c) {
        if (n <= 1)
            return s.indexOf(c);
        else {
            String sub = s.substring(s.indexOf(c) + 1);
            return (s.length() - sub.length()) + nthIndexOf(sub, n - 1, c);
        }
    }


    public static String toMultiline(String str, Paint p, float max) {
        if (max > 0) {
            int s = 0;
            int lastSpace = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == ' ')
                    lastSpace = i;
                float len = p.measureText(str, s, i);
                if (len >= max) {
                    if (lastSpace <= s) {//cut long word
                        str = newLineAt(str, i);
                        s = i + 1;
                        i--;
                    } else {
                        str = newLineAt(str, lastSpace);
                        s = lastSpace + 1;
                    }
                }
            }
        }
        return str;
    }


    public static String newLineAt(String str, int index) {
        if (index < str.length() - 1) {
//            "01234 6789";
            String prev = str.substring(0, index + 1),
                    next = str.substring(index + 1);
            if (next.length() > 0 && next.charAt(0) == ' ')
                return prev + '\n' + next.substring(1);
            return prev + '\n' + next;
        }
        return str;
    }


    public static String toMultiline(String s, int lineMax) {
        String[] S = s.split(" ");
        String res = "";
        for (int i = 0; i < S.length; i++) {
            String[] divStr = new String[S[i].length() / lineMax + 1];
            for (int j = 0; j < divStr.length; j++) {
                int start = lineMax * j, end = lineMax * (j + 1);
                String add = S[i].substring(start, end > S[i].length() ? S[i].length() : end);

                String[] lines = res.split("\n");
                String currLine = lines[lines.length - 1];
                if (currLine.length() + add.length() > lineMax)
                    res += "\n" + add + " ";
                else
                    res += add + " ";
            }

        }
        return res;
    }


    public static int webToColor(String webColor) {
        String s = webColor.substring(webColor.length() - 6, webColor.length());
        return Integer.valueOf(s, 16) + 0xFF000000;
    }


    public static int countMatches(String str, String match) {
        int count = 0;
        if (match.length() < str.length()) {
            for (int i = 0; i < str.length() - (match.length() - 1); i++) {
                if (str.substring(i, i + match.length()).equals(match))
                    count++;
            }
        }
        if (match.equals(str))
            count++;
        return count;
    }


    public static String[] splitEmoji(String str) {
        ArrayList<String> list = new ArrayList<>();
        int s = 0;
        for (int e = 0; e < str.length(); e++) {
            if (isEmoji(str.codePointAt(e))) {
                if (s == e) {
                    //TODO
                }
            }
            String curr = str.substring(s, e);
        }
        return (String[]) list.toArray();
    }

    //-------------------------------------Character Utils---------------------------------------


    public static boolean isNumber(int keyCode) {
        return keyCode >= '0' && keyCode <= '9';
    }


    public static boolean isEmoji(int codePoint) {
        return false;
    }

    //--------------------------------------Color Utils-------------------------------------------


    public static int colorShade(int c, int f) {
        if (c == Color.BLACK)
            c = 0xFF202020;
        float mult = 1 + f * .075f;
        return redestributeRGB((int) (Color.red(c) * mult), (int) (Color.green(c) * mult),
                (int) (Color.blue(c) * mult));
    }


    private static int clampRGB(int r, int g, int b) {
        return Color.argb(255, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }


    private static int redestributeRGB(int r, int g, int b) {
        int m = Math.max(r, Math.max(g, b));
        if (m <= 255)
            return Color.argb(255, r, g, b);
        int total = r + g + b;
        if (total >= 3 * 255)
            return Color.argb(255, 255, 255, 255);//white
        int x = (3 * 255 - total) / (3 * m - total);
        int gray = 255 - x * m;
        return Color.argb(255, gray + x * r, gray + x * g, gray + x * b);
    }


    private static boolean whiteDoesContrast(int color) {
        float yiq = relativeLuminance(color);
        return yiq < 128;
    }


    /**
     * @param color background color
     * @return best contrast color either Black or White
     */
    public static int contrastColor(int color) {
        return whiteDoesContrast(color) ? Color.WHITE : Color.BLACK;
    }


    /**
     * @param color color to check
     * @return the relative luminance of the given color
     */
    public static float relativeLuminance(int color) {
        return (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
    }


    /**
     * @param color1 first color
     * @param color2 second color
     * @return the contrast ratio between both of them
     */
    public static float contrastRatio(int color1, int color2) {
        float l1 = relativeLuminance(color1),
                l2 = relativeLuminance(color2);
        return Math.max(l1 / l2, l2 / l1);
    }


    /**
     * Get the best color to fit against a given background
     *
     * @param preferred  first choice of color
     * @param secondary  second choice of color
     * @param background background to check
     * @return first choice it it has a good contrast otherwise it returns the one with the best contrast
     */
    public static int bestColor(int preferred, int secondary, int background) {
        float r1 = contrastRatio(preferred, background);
        if (r1 >= 1.1f)
            return preferred;
        else {
            float r2 = contrastRatio(secondary, background);
            return r2 > r1 ? secondary : preferred;
        }
    }


    // -----------------------------------BaseAnimation Util----------------------------------------
    public static AnimatorSet sequence(Animator[] anims, long delay) {
        return sequence(anims, delay, null);
    }


    public static AnimatorSet sequence(Animator[] anims, long delay, int[] skipDelayAt) {
        AnimatorSet set = new AnimatorSet();
        if (anims.length == 1) {
            set.play(anims[0]);
            return set;
        }
        int skipMax = skipDelayAt.length;
        int skip = 0;
        for (int i = 0; i < anims.length - 1; i++) {
            if (skip < skipMax && skipDelayAt[skip] == i) {
            } else {
                anims[i + 1].setStartDelay(delay);
            }
            set.play(anims[i]).before(anims[i + 1]);
        }
        return set;
    }


    public static float fromFrac(float beg, float end, float frac) {
        return beg + (end - beg) * frac;
    }

}
