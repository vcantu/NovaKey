package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.settings.widgets.ObservableHorizontalScrollView;

/**
 * Created by Viviano on 1/5/2016.
 */
public abstract class HorizontalPicker extends View implements View.OnTouchListener {

    private float dimen;
    private Paint p;
    private int mIndex = 0;
    private boolean isClick = true;

    private CountDownTimer mLongPressTimer;
    private boolean mOnReleasePicker = false;
    private float pickerX, pickerY;

    private ObservableHorizontalScrollView mScrollView;


    protected OnItemSelectedListener mListener;

    protected PickerItem[] items;
    protected Object[] metaData;

    protected ReleasePicker releasePicker;

    public HorizontalPicker(Context context, AttributeSet attrs, PickerItem ... pickerItems) {
        super(context, attrs);
        items = pickerItems;
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        p.setShadowLayer(10, 0, 10, 0x80000000);
        dimen = context.getResources().getDimension(R.dimen.picker_dimen);
        //removes hardware acceleration to allow svgs
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        setOnTouchListener(this);
    }

    public HorizontalPicker(Context context, AttributeSet attrs, PickerItem[] pickerItems,
                            Object ... metaData) {
        this(context, attrs, pickerItems);
        this.metaData = metaData;
    }

    public void setReleasePicker(ReleasePicker releasePicker) {
        this.releasePicker = releasePicker;
    }

    /**
     * Will set the size of the picker accordingly
     * also sets its parent to mScrollView
     *
     * @param w width measure spec
     * @param h height measure spec
     */
    @Override
    public void onMeasure(int w, int h) {
        mScrollView = (ObservableHorizontalScrollView)getParent();
        mScrollView.setOnScrollListener(new ObservableHorizontalScrollView.OnScrollListener() {
            @Override
            public void onScrollChanged(View view, int x, int y, int oldx, int oldy) {
                cancelLongPressTimer();
            }
        });
        if (items == null)
            super.onMeasure(w, h);
        else
            setMeasuredDimension((int) dimen * items.length, (int) dimen);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pickerX = event.getX() - mScrollView.getScrollX();
        pickerY = event.getY() +
                        ((View)getParent()).getY() +
                        ((View)getParent().getParent()).getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startClickTimer();
                if (releasePicker != null) {
                    startLongPressTimer((int)(event.getX() / dimen));
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (releasePicker != null && mOnReleasePicker) {
                    event.setLocation(pickerX, pickerY);
                    return releasePicker.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                cancelLongPressTimer();
                if (isClick) {
                    int selectedIndex = (int) (event.getX() / dimen);
                    mIndex = selectedIndex;

                    if (mListener != null)
                        mListener.onItemSelected(items[selectedIndex]);
                    invalidate();
                }
                if (releasePicker != null && mOnReleasePicker) {
                    event.setLocation(pickerX, pickerY);
                    return releasePicker.onTouchEvent(event);
                }
                break;
        }
        return true;
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

    /**
     * Starts a CountDownTimer, that will call the onItemLongPress method
     * and activate the release picker if any
     */
    private void startLongPressTimer(final int currIndex) {
        mLongPressTimer = new CountDownTimer(400, 400) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                mOnReleasePicker = true;
                onItemLongPress(items[currIndex], pickerX, pickerY);
            }

        }.start();
    }

    /**
     * Will cancel the mLongPressTimer if its not null
     */
    private void cancelLongPressTimer() {
        if (mLongPressTimer != null) {
            mLongPressTimer.cancel();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i=0; i<items.length; i++) {
            items[i].draw(i * dimen + dimen / 2, dimen / 2, dimen, mIndex == i, metaData[i], p, canvas);
        }
    }

    /**
     * Will be called when the given item is long pressed. Used to communicate,
     * with the ReleasePicker
     *
     * @param item item which has been long pressed
     * @param startX corrected finger X position
     * @param startY corrected finger Y position
     */
    protected abstract void onItemLongPress(Object item, float startX, float startY);


    /**
     * Sets the on item selected listener
     *
     * @param listener OnItemSelectedListener to set
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Listener that is called when an item is slected
     */
    public interface OnItemSelectedListener {
        /**
         * Callback method that will be called when an item is selected
         *
         * @param item item which has been selected
         */
        void onItemSelected(Object item);
    }

}
