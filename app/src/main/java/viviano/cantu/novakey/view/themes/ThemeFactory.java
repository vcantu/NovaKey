package viviano.cantu.novakey.view.themes;

import java.util.Iterator;
import java.util.NoSuchElementException;

import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.themes.board.BaseTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.view.themes.board.DonutTheme;
import viviano.cantu.novakey.view.themes.board.IconTheme;
import viviano.cantu.novakey.view.themes.board.MaterialTheme;
import viviano.cantu.novakey.view.themes.board.MulticolorDonutTheme;
import viviano.cantu.novakey.view.themes.board.MulticolorTheme;
import viviano.cantu.novakey.view.themes.board.SeparateSectionsTheme;

/**
 * TODO: use reflection to create instead of id
 * TODO: get rid of autocolor and leave as settings flag
 * Created by Viviano on 2/4/2016.
 */
public class ThemeFactory implements Iterable<BoardTheme>, Iterator<BoardTheme> {

    /**
     * Builds a theme from the sharedPref string
     *
     * theme will be a String that can be translated into a BoardTheme, with colors and other data
     * it will have the following format:
     *
     * t = the number theme id
     *
     * numbers represent the color so that:
     * c1 = primaryColor, c2 = accentColor, c3 = contrastColor
     *
     * 'A' or 'X' if theme has auto color enabled
     *
     * '3d' or 'X' if the theme has 3d enabled
     *
     * format:
     *     t,c1,c2,c3,A,3d
     *
     * @param str string to build theme from
     * @return theme built from a string using the above format
     */
    public static BoardTheme themeFromString(String str) {
        if (str.equals(Settings.DEFAULT)) {
            str = "0," +
                    String.valueOf(0xFF616161) + "," +
                    String.valueOf(0xFFF5F5F5) + "," +
                    String.valueOf(0xFFF5F5F5) + "," +
                    "X," + "X";//no auto & no 3d by default
        }
        String[] params = str.split(",");

        BoardTheme theme = createTheme(Integer.valueOf(params[0]));
        theme.setColors(Integer.valueOf(params[1]),
                Integer.valueOf(params[2]),
                Integer.valueOf(params[3]));
        if (params[4].equalsIgnoreCase("A"))
            Settings.autoColor = true;//TODO
        theme.set3D(params[5].equalsIgnoreCase("3d"));
        return theme;
    }

    public static String stringFromTheme(BoardTheme theme) {
        return theme.themeID() + "," +
                theme.primaryColor() + "," +
                theme.accentColor() + "," +
                theme.contrastColor() + "," +
                (Settings.autoColor ? "A" : "X") + "," +
                (theme.is3D() ? "3d" : "X");
    }

    public static BoardTheme createTheme(int i) {
        switch (i) {
            case 0:
            default:
                return new BaseTheme();
            case 1:
                return new MaterialTheme();
            case 2:
                return new SeparateSectionsTheme();
            case 3:
                return new DonutTheme();
            case 4:
                return new MulticolorDonutTheme();
            case 5:
                return new MulticolorTheme();
            case 6:
                return new IconTheme();
        }
    }
    /**
     * @return a new instance of ThemeFactory which implements Iterable
     *  this will iterate through all themes that exist
     */
    public static ThemeFactory allThemes() {
        return new ThemeFactory();
    }

    /**
     * @return the amount of theme's  available
     */
    public static int getThemeCount() {
        int c = 0;
        for (BoardTheme t : allThemes())
            c++;
        return c;
    }

    /**
     * Returns an {@link Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<BoardTheme> iterator() {
        return this;
    }

    private int mCurr = 0;

    /**
     * Returns true if there is at least one more element, false otherwise.
     *
     * @see #next
     */
    @Override
    public boolean hasNext() {
        return createTheme(mCurr) != null;
    }

    /**
     * Returns the next object and advances the iterator.
     *
     * @return the next object.
     * @throws NoSuchElementException if there are no more elements.
     * @see #hasNext
     */
    @Override
    public BoardTheme next() {
        if (mCurr < 0 || !hasNext())
            throw new NoSuchElementException();
        BoardTheme res = createTheme(mCurr);
        mCurr++;
        return res;
    }

    /**
     * Removes the last object returned by {@code next} from the collection.
     * This method can only be called once between each call to {@code next}.
     *
     * @throws UnsupportedOperationException if removing is not supported by the collection being
     *                                       iterated.
     * @throws IllegalStateException         if {@code next} has not been called, or {@code remove} has
     *                                       already been called after the last call to {@code next}.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
