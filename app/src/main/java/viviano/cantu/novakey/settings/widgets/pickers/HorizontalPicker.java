package viviano.cantu.novakey.settings.widgets.pickers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
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
    private boolean isClick = true;

    private CountDownTimer mLongPressTimer;
    private boolean mOnReleasePicker = false;
    private float pickerX, pickerY;

    private int mIndex = 0, mTempIndex = 0;

    private ObservableHorizontalScrollView mScrollView;

    protected OnItemSelectedListener mListener;
    protected PickerItem[] mItems;

    protected int[] mSubIndexes;

    protected ReleasePicker mReleasePicker;

    public HorizontalPicker(Context context, AttributeSet attrs, PickerItem ... pickerItems) {
        super(context, attrs);
        mItems = pickerItems;
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        p.setShadowLayer(10, 0, 10, 0x80000000);
        dimen = context.getResources().getDimension(R.dimen.picker_dimen);
        //removes hardware acceleration to allow svgs
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        setOnTouchListener(this);
    }

    public void setReleasePicker(ReleasePicker releasePicker) {
        mReleasePicker = releasePicker;
    }

    private void setItemListener() {
        mReleasePicker.setOnItemListener(new ReleasePicker.OnItemListener() {
            @Override
            public void onItemUpdated(int index) {
                if (mListener != null)
                    mListener.onItemSelected(mItems[mTempIndex], index);
                invalidate();
            }

            @Override
            public void onItemSelected(int index) {
                if (mListener != null)
                    mListener.onItemSelected(mItems[mTempIndex], index);
                mIndex = mTempIndex;
                invalidate();
            }

            @Override
            public void onCancel() {
                if (mListener != null)
                    mListener.onItemSelected(mItems[mIndex], mSubIndexes[mIndex]);
                invalidate();
            }
        });
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
        mScrollView = (ObservableHorizontalScrollView) getParent();
        mScrollView.setOnScrollListener(new ObservableHorizontalScrollView.OnScrollListener() {
            @Override
            public void onScrollChanged(View view, int x, int y, int oldx, int oldy) {
                cancelLongPressTimer();
            }
        });
        if (mItems == null)
            super.onMeasure(w, h);
        else
            setMeasuredDimension((int) dimen * mItems.length, (int) dimen);
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
                if (mReleasePicker != null) {
                    startLongPressTimer((int)(event.getX() / dimen));
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mReleasePicker != null && mOnReleasePicker) {
                    event.setLocation(pickerX, pickerY);
                    return mReleasePicker.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                cancelLongPressTimer();
                if (isClick) {
                    int selectedIndex = (int) (event.getX() / dimen);
                    mIndex = selectedIndex;

                    if (mListener != null)
                        mListener.onItemSelected(mItems[selectedIndex], mSubIndexes[selectedIndex]);
                    invalidate();
                }
                if (mReleasePicker != null && mOnReleasePicker) {
                    event.setLocation(pickerX, pickerY);
                    return mReleasePicker.onTouchEvent(event);
                }
                break;
        }
        return true;
    }

    /**
     * Starts click timer to determine whether user clicked
     */
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
                mTempIndex = currIndex;
                mOnReleasePicker = true;
                setItemListener();
                onItemLongPress(currIndex, pickerX, pickerY);
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

    public void setItem(final int index) {
        mIndex = index;
        invalidate();
        final HorizontalScrollView parent = (HorizontalScrollView)getParent();
        parent.post(new Runnable() {
            public void run() {
                parent.smoothScrollTo(getDesiredX(index, parent.getWidth()), 0);
            }
        });
    }

    /**
     * Takes the desired index & the width of the view and returns the
     * x position it is at minus half of the width
     *
     * @param index index of item
     * @param width
     * @return x position at the given index
     */
    private int getDesiredX(int index, float width) {
        float indexX = index * dimen + dimen/2;
        indexX -= width / 2;
        if (indexX < 0)
            return 0;
        if (indexX > getWidth()-width)
            return (int)(getWidth()-width);
        return (int)indexX;
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i=0; i< mItems.length; i++) {
            mItems[i].draw(i * dimen + dimen / 2, dimen / 2, dimen, mIndex == i,
                    mSubIndexes[i], p, canvas);
        }
    }

    /**
     * Will be called when the given item is long pressed. Used to communicate,
     * with the ReleasePicker
     *
     * @param index index of item which has been long pressed
     * @param startX corrected finger X position
     * @param startY corrected finger Y position
     */
    protected abstract void onItemLongPress(int index, float startX, float startY);

    /**
     * Sets the on item selected listener
     *
     * @param listener OnItemSelectedListener to set
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Listener that is called when an item is selected
     */
    public interface OnItemSelectedListener {
        /**
         * Callback method that will be called when an item is selected
         *
         * @param item item which has been selected
         * @param subIndex subIndex of the item
         */
        void onItemSelected(PickerItem item, int subIndex);
    }

}
