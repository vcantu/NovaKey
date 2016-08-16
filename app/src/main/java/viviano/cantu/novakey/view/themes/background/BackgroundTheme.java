package viviano.cantu.novakey.view.themes.background;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.themes.ChildTheme;

/**
 * Created by Viviano on 8/14/2016.
 */
public interface BackgroundTheme extends ChildTheme {

    /**
     * Draw background of keyboard
     *
     * @param l left position
     * @param t top position
     * @param rt right position
     * @param b bottom position
     * @param x center X position
     * @param y center Y position
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param canvas canvas to draw on
     */
    void drawBackground(float l, float t, float rt, float b, float x, float y,
                        float r, float sr, Canvas canvas);
}
