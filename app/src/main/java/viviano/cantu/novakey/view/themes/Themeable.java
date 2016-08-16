package viviano.cantu.novakey.view.themes;

/**
 * Created by Viviano on 8/14/2016.
 *
 * Any UI element which wishes to use a theme to determine how to be drawn
 * should implement this interface
 */
public interface Themeable {

    /**
     * Will set this object's theme
     *
     * @param theme a Master Theme
     */
    void setTheme(MasterTheme theme);
}
