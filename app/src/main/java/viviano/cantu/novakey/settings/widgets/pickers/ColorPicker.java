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
    protected void onItemLongPress(Object item, float startX, float startY) {
        if (releasePicker != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
            releasePicker.setVisibility(VISIBLE);
            Colors color = (Colors)item;

            Colors[] colors = new Colors[color.size()];
            Integer[] metaData = new Integer[color.size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = color;
                metaData[i] = i;
            }

            releasePicker.setOnItemListener(new ReleasePicker.OnItemListener() {
                @Override
                public void onItemUpdated(Object item) {

                }

                @Override
                public void onItemSelected(Object item) {
                    if (mListener != null)
                        mListener.onItemSelected(item);
                }

                @Override
                public void onCancel() {

                }
            });
            releasePicker.onStart(startX, startY, colors, metaData);
        }
    }
}
