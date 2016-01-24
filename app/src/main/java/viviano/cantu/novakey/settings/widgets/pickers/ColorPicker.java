package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.util.AttributeSet;

import viviano.cantu.novakey.settings.Colors;

/**
 * Created by Viviano on 1/8/2016.
 */
public class ColorPicker extends HorizontalPicker {

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs, Colors.ALL);

        mSubIndexes = new int[Colors.ALL.length];
        for (int i=0; i<mSubIndexes.length; i++) {
            mSubIndexes[i] = Colors.ALL[i].mainIndex();
        }
    }

    @Override
    protected void onItemLongPress(int index, float startX, float startY) {
        if (mReleasePicker != null) {
            getParent().requestDisallowInterceptTouchEvent(true);

            Colors color = (Colors)mItems[index];

            if (color.size() > 1) {
                mReleasePicker.setVisibility(VISIBLE);
                int[] subIndex = new int[color.size()];
                for (int i = 0; i < color.size(); i++) {
                    subIndex[i] = i;
                }
                mReleasePicker.onStart(startX, startY, mItems[index], subIndex);
            }
        }
    }
}
