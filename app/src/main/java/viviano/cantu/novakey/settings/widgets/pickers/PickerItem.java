package viviano.cantu.novakey.settings.widgets.pickers;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Viviano on 1/5/2016.
 */
public interface PickerItem {

    /**
     * Draw method for the picker item
     *
     * @param x        center x position
     * @param y        center y position
     * @param dimen    dimension equivalent to the maximum height
     * @param selected whether it is selected
     * @param index    sub index of picker item
     * @param p        paint used
     * @param canvas   canvas to draw on
     */
    void drawPickerItem(float x, float y, float dimen, boolean selected, int index, Paint p, Canvas canvas);
}
