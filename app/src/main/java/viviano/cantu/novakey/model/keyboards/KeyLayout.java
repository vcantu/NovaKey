package viviano.cantu.novakey.model.keyboards;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import viviano.cantu.novakey.R;

/**
 * Holds information about all keyboards
 * Holds all keyboards
 * To get a keyboard use
 *      KeyLayout.get("NAME_HERE");
 * Compare KeyLayouts using
 *      .equals(layout);
 */
public class KeyLayout implements Iterator<Key>, Iterable<Key> {

    private final Key[][] keys;
    private final String name;

    public final List<KeyProperties> properties;

    private int currG = 0, currL = 0;

    public KeyLayout(String name, Key[][] keys) {
        this.keys = keys;
        this.name = name;
        List<KeyProperties> temp = new ArrayList<>();
        for (Key k : this) {
            temp.add(k.properties);
        }
        properties = Collections.unmodifiableList(temp);
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

    /**
     * @param group key group
     * @param loc key location
     * @return the key's char in uppercase at the given group and location
     */
    public Character getUppercase(int group, int loc) {
        return getKey(group, loc).getUppercase();
    }

    /**
     * @param group key group
     * @param loc key location
     * @return the key's char in lowercase at the given group and location
     */
    public Character getLowercase(int group, int loc) {
        return getKey(group, loc).getLowercase();
    }

    private Key getKey(int group, int loc) {
        if (loc > keys[group].length)//for alt layouts
            return keys[group][0];
        return keys[group][loc];
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof KeyLayout)
            return this.name.equalsIgnoreCase(((KeyLayout)that).name);
        if (that instanceof String)
            return this.name.equalsIgnoreCase((String)that);
        return false;
    }

    //--------------------------------Statics------------------------------------
    public static final int SYMBOLS = -2, PUNCTUATION = -1, DEFAULT = 0;
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
            boolean altLayout = i > 0 && S[i].length() > 4;
            for (int j=0; j<S[i].length(); j++) {
                result[i][j] = new Key(S[i].charAt(j), i, j, altLayout);
            }
        }
        return result;
    }
}
