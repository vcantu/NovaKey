package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.settings.Colors;

/**
 * Created by Viviano on 1/8/2016.
 */
public class ColorPicker extends HorizontalPicker {

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs, Colors.ALL);

        metaData = new Integer[Colors.ALL.length];
        for (int i=0; i<metaData.length; i++) {
            metaData[i] = Colors.ALL[i].mainIndex();
        }
    }

    @Override
    protected void onItemDown(Object item) {
        //TODO: open radial picker
        if (releasePicker != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
            releasePicker.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onItemSelected(Object item) {
        //TODO: set to main
    }
}
