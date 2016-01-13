package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.R;

/**
 * Created by Viviano on 1/8/2016.
 */
public class ReleasePicker extends View {

    private float dimen;
    private Paint p;
    private int index = 0;

    protected PickerItem[] items;
    protected Object[] metaData;

    public ReleasePicker(Context context, AttributeSet attrs) {//, PickerItem ... pickerItems) {
        super(context, attrs);
        //items = pickerItems;
        p = new Paint();
        dimen = context.getResources().getDimension(R.dimen.picker_dimen);
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs
    }

//    public ReleasePicker(Context context, AttributeSet attrs, PickerItem[] items, Object ... metaData) {
//        this(context, attrs, items);
//        this.metaData = metaData;
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                setVisibility(INVISIBLE);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        p.setColor(0xFFFF0000);
    }

    // Callback method that will be called when the item is released
    // index will be -1 if if no item was selected
    //abstract void onUp(int index);
}
