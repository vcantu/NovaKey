package viviano.cantu.novakey.settings.widgets.pickers;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Viviano on 1/5/2016.
 */
public interface PickerItem {
    //TODO: change so it ends with,   Object ... params);
    void draw(float x, float y, float dimen, boolean selected, Object metaData, Paint p, Canvas canvas);
}
