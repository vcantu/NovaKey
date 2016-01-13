package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.R;

/**
 * Created by Viviano on 1/5/2016.
 */
public abstract class HorizontalPicker extends View {

    private float dimen;
    private Paint p;
    private int index = 0;
    private boolean isClick = true;

    protected PickerItem[] items;
    protected Object[] metaData;

    protected ReleasePicker releasePicker;

    public HorizontalPicker(Context context, AttributeSet attrs, PickerItem ... pickerItems) {
        super(context, attrs);
        items = pickerItems;
        p = new Paint();
        dimen = context.getResources().getDimension(R.dimen.picker_dimen);
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTimer();
                        int downIndex = (int) (event.getX() / dimen);

                        onItemDown(items[downIndex]);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (releasePicker != null) {
                            event.setLocation(event.getX(), event.getY() +
                                    ((View)getParent()).getY() + ((View)getParent().getParent()).getY());
                            return releasePicker.onTouchEvent(event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (releasePicker != null) {
                            event.setLocation(event.getX(), event.getY() +
                                    ((View)getParent()).getY() + ((View)getParent().getParent()).getY());
                            return releasePicker.onTouchEvent(event);
                        }
                        //if no release picker
                        else if (isClick) {
                            int selectedIndex = (int) (event.getX() / dimen);
                            index = selectedIndex;

                            onItemSelected(items[selectedIndex]);
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
    }

    public HorizontalPicker(Context context, AttributeSet attrs, PickerItem[] pickerItems, Object ... metaData) {
        this(context, attrs, pickerItems);
        this.metaData = metaData;
    }

    private void startClickTimer() {
        isClick = true;
        new CountDownTimer(200, 200) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                isClick = false;
            }
        }.start();
    }

    public void setReleasePicker(ReleasePicker releasePicker) {
        this.releasePicker = releasePicker;
    }

    @Override
    public void onMeasure(int w, int h) {
        if (items == null)
            super.onMeasure(w, h);
        else
            setMeasuredDimension((int) dimen * items.length, (int) dimen);
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i=0; i<items.length; i++) {
            items[i].draw(i * dimen + dimen / 2, dimen / 2, dimen, index == i, metaData[i], p, canvas);
        }
    }

    protected abstract void onItemDown(Object item);

    protected abstract void onItemSelected(Object item);
}
