package viviano.cantu.novakey.themes;

import java.util.Iterator;
import java.util.NoSuchElementException;

import viviano.cantu.novakey.settings.Settings;

/**
 * Created by Viviano on 2/4/2016.
 */
public class ThemeBuilder implements Iterable<Theme>, Iterator<Theme> {

    public static Theme getTheme(int i) {
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
        }
    }

    /**
     * @return a new instance of ThemeBuilder which implements Iterable
     *  this will iterate through all themes that exist
     */
    public static ThemeBuilder allThemes() {
        return new ThemeBuilder();
    }

    /**
     * Returns an {@link Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<Theme> iterator() {
        return this;
    }

    /// Total amount of themes available
    public static int THEME_COUNT = 6;

    private int mCurr = 0;

    /**
     * Returns true if there is at least one more element, false otherwise.
     *
     * @see #next
     */
    @Override
    public boolean hasNext() {
        return mCurr < THEME_COUNT;
    }

    /**
     * Returns the next object and advances the iterator.
     *
     * @return the next object.
     * @throws NoSuchElementException if there are no more elements.
     * @see #hasNext
     */
    @Override
    public Theme next() {
        if (mCurr < 0 || mCurr >= THEME_COUNT)
            throw new NoSuchElementException();
        Theme res = getTheme(mCurr);
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
