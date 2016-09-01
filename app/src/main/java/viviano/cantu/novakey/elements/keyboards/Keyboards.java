package viviano.cantu.novakey.elements.keyboards;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.R;

/**
 * Created by Viviano on 8/21/2016.
 */
public class Keyboards {

    public static final int SYMBOLS = -2;
    public static final int PUNCTUATION = -1;
    public static final int DEFAULT = 0;

    private final Keyboard mSymbols, mPunctuation;
    private final List<Keyboard> mLanguages;

    public Keyboards(Context context) {
        Resources res = context.getResources();

        mSymbols = new Keyboard("Symbols", convert(R.array.Symbols, res));
        mPunctuation = new Keyboard("Punctuation", convert(R.array.Punctuation, res));

        mLanguages = new ArrayList<>();
        mLanguages.add(new Keyboard("English", convert(R.array.English, res)));
    }

    /**
     * @param code keyboard code, which can be symbols or punctuation
     * @return a keyboard based on the given code
     */
    public Keyboard get(int code) {
        switch (code) {
            case Keyboards.SYMBOLS:
                return mSymbols;
            case Keyboards.PUNCTUATION:
                return mPunctuation;
            default:
                return mLanguages.get(code);
        }
    }


    //converts a String[] to a String[][] of single characters
    private static Key[][] convert(int ID, Resources res) {
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
