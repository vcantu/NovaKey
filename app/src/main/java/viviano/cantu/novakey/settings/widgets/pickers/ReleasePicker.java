package viviano.cantu.novakey.settings.widgets.pickers;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.animations.DelayableInterpolator;
import viviano.cantu.novakey.animations.MultiValueAnimator;

/**
 * Created by Viviano on 1/8/2016.
 */
public class ReleasePicker extends View {

    private float dimen;
    private Paint p;
    private int mIndex = -1, mPrevIndex = -1;

    private float mCenterX, mCenterY;
    private float mFingX, mFingY;

    private PickerItem[] mItems;//todo change to just one PickerItem
    private Object[] mMetaData;
    private ItemData[] mData;

    private OnItemListener mListener;

    public ReleasePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        p.setShadowLayer(10, 0, 10, 0x80000000);
        dimen = context.getResources().getDimension(R.dimen.picker_dimen);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mFingX = event.getX();
                mFingY = event.getY();
                updateData();
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    if (mIndex == -1)
                        mListener.onCancel();
                    else
                        mListener.onItemSelected(mIndex);
                }
                invalidate();
                setVisibility(INVISIBLE);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < mItems.length; i++) {
            if (i != mIndex)
                drawItem(i, canvas);
        }
        if (mIndex != -1) {
            drawItem(mIndex, canvas);
        }
    }

    private void drawItem(int i, Canvas canvas) {
        mItems[i].draw(
                Util.xFromAngle(mCenterX, mData[i].distance, mData[i].angle),
                Util.yFromAngle(mCenterY, mData[i].distance, mData[i].angle),
                dimen * mData[i].scale * mData[i].scaleMultiplier,
                mIndex == i, mMetaData[i], p, canvas);
    }

    public void onStart(float startX, float startY, PickerItem[] items, Object[] metaData) {
        mCenterX = startX; mCenterY = startY;
        mFingX = startX; mFingY = startY;
        mItems = items;
        mMetaData = metaData;
        mData = new ItemData[mItems.length];

        double angleDiv = Math.PI / mItems.length;
        for (int i = 0; i < mData.length; i++) {
            double a = angleDiv * i;
            mData[i] = new ItemData(0, 0, a);
        }
        updateData();
        //animate
//        for (int i = 0; i < mItems.length; i++) {
//            animateItem(i);
//        }
        //animateItems();
        animateFancyItems();
        //animateBoringItems();
    }

    private void animateItem(final int itemIndex) {
        final ValueAnimator anim = ValueAnimator.ofFloat(0, 1)
                .setDuration(500);
        anim.setInterpolator(new OvershootInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac = (float) anim.getAnimatedValue();
                mData[itemIndex].scale = frac;
                mData[itemIndex].distance = Util.fromFrac(0, dimen * 2, frac);
                invalidate();
            }
        });
        anim.setStartDelay(itemIndex * 50);
        anim.start();
    }

    private void animateItems() {
        long singleDur = 500;
        long secDelay = 100;
        final TimeInterpolator[] tInters = new TimeInterpolator[mData.length];

        long duration = singleDur + (tInters.length - 1) * secDelay;

        for (int i = 0; i < tInters.length; i++) {
            tInters[i] = new DelayableInterpolator(secDelay * i, singleDur, duration,
                    new OvershootInterpolator());
        }

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1)
                .setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                for (int i = 0; i < tInters.length; i++) {
                    float actual = tInters[i].getInterpolation(v);
                    mData[i].scale = actual;
                    mData[i].distance = Util.fromFrac(0, dimen * 2, actual);
                }
                invalidate();
            }
        });
        anim.start();
    }

    //optimal
    private void animateFancyItems() {
        MultiValueAnimator anim = MultiValueAnimator.create();

        long singleDur = 500;
        long delay = 50;
        for (int i = 0; i < mData.length; i++) {
            anim.addInterpolator(new OvershootInterpolator(), delay * i, singleDur);
        }

        anim.setMultiUpdateListener(new MultiValueAnimator.MultiUpdateListener() {
            @Override
            public void onValueUpdate(ValueAnimator animator, float value, int index) {
                mData[index].scale = value;
                mData[index].distance = Util.fromFrac(0, dimen * 2, value);
            }

            @Override
            public void onAllUpdate(ValueAnimator animator, float value) {
                invalidate();
            }
        });
        // safe lock
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                for (int i = 0; i < mData.length; i++) {
                    mData[i].scale = 1;
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    //all grow at the same time
    private void animateBoringItems() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1)
                .setDuration(400);
        anim.setInterpolator(new OvershootInterpolator(2f));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac = (float)animation.getAnimatedValue();
                for (int i = 0; i < mData.length; i++) {
                    mData[i].scale = frac;
                    mData[i].distance = Util.fromFrac(0, dimen * 2, frac);
                }
                invalidate();
            }
        });
        anim.start();
    }

    /**
     * Updates the size of all of the PickerItems according to the current Finger position
     * It also checks if currently animating
     */
    public void updateData() {
        mIndex = -1;
        for (int i = 0; i < mData.length; i++) {
            float x = Util.xFromAngle(mCenterX, mData[i].distance, mData[i].angle),
                    y = Util.yFromAngle(mCenterY, mData[i].distance, mData[i].angle);

            float dist = Util.distance(x, y, mCenterX, mCenterY);
            float size = (float) Math.pow(dist / Util.distance(x, y, mFingX, mFingY), 1.0/3);
            if (size >= 1.5f) {
                if (i != mPrevIndex) {
                    mPrevIndex = i;
                    if (mListener != null)
                        mListener.onItemUpdated(mItems[i]);
                }
                mIndex = i;
                size = 1.5f;
            }
            mData[i].scaleMultiplier = size;
        }
        invalidate();
    }

    /**
     * Simple data class that will hold information for each picker
     */
    private class ItemData {
        float scaleMultiplier, distance, scale;
        double angle;

        public ItemData(float scale, float distance, double angle) {
            this.scaleMultiplier = 1;
            this.scale = scale;
            this.distance = distance;
            this.angle = angle;
        }
    }

    public void setOnItemListener(OnItemListener listener) {
        mListener = listener;
    }

    public interface OnItemListener {
        void onItemUpdated(Object item);
        void onItemSelected(Object item);
        void onCancel();
    }
}
