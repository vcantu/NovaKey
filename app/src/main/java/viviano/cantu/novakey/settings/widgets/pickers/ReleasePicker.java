/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.settings.widgets.pickers;

import android.animation.Animator;
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
import viviano.cantu.novakey.animations.utils.MultiValueAnimator;

/**
 * Created by Viviano on 1/8/2016.
 */
public class ReleasePicker extends View {

    private float dimen;
    private Paint p;
    private int mIndex = -1, mPrevIndex = -1;

    private float mCenterX, mCenterY, mRadius;
    private float mFingX, mFingY;

    private PickerItem mItems;
    private int[] mSubIndexes;
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
                setVisibility(GONE);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < mSubIndexes.length; i++) {
            if (i != mIndex)
                drawItem(i, canvas);
        }
        if (mIndex != -1) {
            drawItem(mIndex, canvas);
        }
    }

    private void drawItem(int i, Canvas canvas) {
        mItems.drawPickerItem(
                Util.xFromAngle(mCenterX, mData[i].distance, mData[i].angle),
                Util.yFromAngle(mCenterY, mData[i].distance, mData[i].angle),
                dimen * mData[i].scale * mData[i].scaleMultiplier,
                mIndex == i, mSubIndexes[i], p, canvas);
    }

    /**
     * Updates the size of all of the PickerItems according to the current Finger position
     * It also checks if currently animating
     */
    private void updateData() {
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
                        mListener.onItemUpdated(mSubIndexes[i]);
                }
                mIndex = i;
                size = 1.5f;
            }
            mData[i].scaleMultiplier = size;
        }
        invalidate();
    }

    public void onStart(float startX, float startY, PickerItem items, int[] subIndexes) {
        mCenterX = startX; mCenterY = startY;
        mFingX = startX; mFingY = startY;
        mItems = items;
        mSubIndexes = subIndexes;
        mRadius = dimen * 2;
        mData = new ItemData[mSubIndexes.length];

        double startAngle = getFixedAngle(startX, startY);
        double angleDiv = Math.PI / (mSubIndexes.length + 1);
        for (int i = 0; i < mData.length; i++) {
            double a = angleDiv * (i + 1);
            mData[i] = new ItemData(0, 0, startAngle + a);
        }
        updateData();
        //animate
        animateItems();
    }

    private double getFixedAngle(float startX, float startY) {
        boolean clockwise = startX < getWidth() / 2;
        double div = Math.PI / 2 / 8;

        for (int i = 0; i < 8; i++) {
            double checkA = div * i * (clockwise ? -1 : 1);
            float checkX = Util.xFromAngle(startX, mRadius * 1.1f * (clockwise ? -1 : 1), checkA);
            float checkY = Util.yFromAngle(startY, mRadius * 1.1f * (clockwise ? -1 : 1), checkA);

            if (inside(checkX, checkY)) {
                return checkA;
            }
        }
        return Math.PI / 2 * (clockwise ? -1 : 1);
    }

    /**
     * @param x x position
     * @param y y position
     * @return whether the x & y coordinates are within the bounds of this view
     */
    private boolean inside(float x, float y) {
        return x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight();
    }

    //optimal
    private void animateItems() {
        MultiValueAnimator<Integer> anim = MultiValueAnimator.create();

        long singleDur = 500;
        long delay = 50;
        for (int i = 0; i < mData.length; i++) {
            anim.addInterpolator(i, new OvershootInterpolator(), delay * i, singleDur);
        }

        anim.setMultiUpdateListener(new MultiValueAnimator.MultiUpdateListener<Integer>() {
            @Override
            public void onValueUpdate(ValueAnimator animator, float value, Integer index) {
                mData[index].scale = value;
                mData[index].distance = Util.fromFrac(0, mRadius, value);
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
                float frac = (float) animation.getAnimatedValue();
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
        void onItemUpdated(int index);
        void onItemSelected(int index);
        void onCancel();
    }
}
