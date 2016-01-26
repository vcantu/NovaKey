package viviano.cantu.novakey;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Holds information about all keyboards
 * Holds all keyboards
 * To get a keyboard use
 *      KeyLayout.get("NAME_HERE");
 * To get a specific key in char format use
 *      .getCharKey(group, loc);
 * To get a specific key in string format use
 *      .getStringKey(group, loc);
 * Compare KeyLayouts using
 *      .equals(layout);
 */

public class KeyLayout implements Iterator<Key>, Iterable<Key> {
    private Key[][] keys;
    public String name;

    private int currG = 0, currL = 0;

    public KeyLayout(String name, Key[][] keys) {
        this.keys = keys;
        this.name = name;
    }

    //Iterator implementation iterates through each key in order by group first then location
    @Override
    public Iterator<Key> iterator() {
        currG = 0;
        currL = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return currG < keys.length && currL < keys[currG].length;
    }

    @Override
    public Key next() {
        if (currG >= keys.length || currL >= keys[currG].length)
            throw new NoSuchElementException();
        Key k = keys[currG][currL];
        currL++;
        if (currL >= keys[currG].length) {
            currG++;
            currL=0;
        }
        return k;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public String getStringKey(int group, int loc) {
        return getCharKey(group, loc).toString();
    }

    public Character getCharKey(int group, int loc) {
        return getKey(group, loc).c;
    }

    public Key getKey(int group, int loc) {
        return keys[group][loc];
    }

    public Key getKey(char c) {
        for (int g=0; g<keys.length; g++) {
            for (int l=0; l<keys[g].length; l++) {
                if (("" + keys[g][l].c).equalsIgnoreCase("" + c))
                    return keys[g][l];
            }
        }
        return null;
    }

    public boolean equals(KeyLayout layout) {
        return name.equalsIgnoreCase(layout.name);
    }
    public boolean equals(String name) {
        return this.name.equalsIgnoreCase(name);
    }


    public int getGroup(char c) {
        Key k = getKey(c);
        if (k != null)
            return k.group;
        return -1;
    }

    public int getLoc(char c) {
        Key k = getKey(c);
        if (k != null)
            return k.loc;
        return -1;
    }

    public void setShifted(boolean status) {
        for (int i=0; i<keys.length; i++) {
            for (int j=0; j<keys[i].length; j++) {
                if (status)
                    keys[i][j].toUpperCase();
                else
                    keys[i][j].toLowerCase();
            }
        }
    }

    //TODO: curr key
    public void drawKeys(int[] textColors, Paint p, Canvas canvas) {
        for (int g=0; g<keys.length; g++) {
            p.setColor(textColors[g]);
            for (int l=0; l<keys[g].length; l++) {
                keys[g][l].draw(p, canvas);
            }
        }
    }

    public void updateCoords(float centerX, float centerY, float radius, float smallRadius) {
        for (int g=0; g<keys.length; g++) {
            for (int l=0; l<keys[g].length; l++) {
                keys[g][l].updateCoords(centerX, centerY, radius, smallRadius);
            }
        }
    }

    //Statics
    private static ArrayList<KeyLayout> KEYBOARDS;
    //Creates keyboards
    public static void CreateKeyboards(Resources res) {
        /*
            Every new keyboard layout added must be added here
            formatted as followed below
         */
        KEYBOARDS = new ArrayList<>();
        KEYBOARDS.add(new KeyLayout("English", convert(R.array.English, res)));
        KEYBOARDS.add(new KeyLayout("Punctuation", convert(R.array.Punctuation, res)));
        KEYBOARDS.add(new KeyLayout("Symbols", convert(R.array.Symbols, res)));
    }

    public static KeyLayout get(String name) {
        for (KeyLayout k : KEYBOARDS) {
            if (k.name.equalsIgnoreCase(name))
                return k;
        }
        return null;
    }

    //converts a String[] to a String[][] of single characters
    public static Key[][] convert(int ID, Resources res) {
        String[] S = res.getStringArray(ID);
        Key[][] result = new Key[S.length][];
        for (int i=0; i<S.length; i++) {
            result[i] = new Key[S[i].length()];
            for (int j=0; j<S[i].length(); j++) {
                result[i][j] = new Key(S[i].charAt(j), i, j);
            }
        }
        return result;
    }
}
