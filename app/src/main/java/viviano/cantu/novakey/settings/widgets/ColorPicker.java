package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.settings.Colors;

/**
 * Created by Viviano on 6/6/2015.
 */
public class ColorPicker extends View {

    private float dimen;
    private Paint p;
    private int index = 0, shadeIndex;

    private int[] shades;

    private ShadePicker shadePicker;

    //detecting
    private OnChangeListener changeListener;
    private boolean isClick = true;

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        dimen = context.getResources().getDimension(R.dimen.picker_dimen);
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs

        shades = new int[Colors.ALL.length];
        for (int i=0; i<shades.length; i++) {
            shades[i] = Colors.ALL[i].mainIndex();
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTimer();
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isClick) {
                            final int pastShadeIndex = shadeIndex,
                                      futureIndex = (int) (event.getX() / dimen);
                            Colors c = Colors.ALL[futureIndex];
                            if (c.size() == 1) {
                                if (changeListener != null)
                                    changeListener.onChange(c.shade(0));
                                index = futureIndex;
                            }
                            else {
                                HorizontalScrollView scrollView = (HorizontalScrollView)getParent();
                                if (scrollView != null) {
                                    int x = (int) ((futureIndex * dimen) - scrollView.getScrollX());
                                    float y = scrollView.getY() + ((View) scrollView.getParent()).getY();
                                    if (shadePicker != null) {
                                        shadePicker.setSelectedListener(new ShadePicker.SelectedListener() {
                                            @Override
                                            public void onShadeSelected(int color) {
                                                if (changeListener != null)
                                                    changeListener.onChange(color);
                                                index = futureIndex;
                                                shadeIndex = Colors.ALL[index].index(color);
                                                shades[index] = shadeIndex;
                                                invalidate();
                                            }

                                            @Override
                                            public void onShadeUpdated(int index, int color) {
                                                if (changeListener != null)
                                                    changeListener.onChange(color);
                                                shades[futureIndex] = index;
                                                invalidate();
                                            }

                                            @Override
                                            public void onCancel() {
                                                shadeIndex = pastShadeIndex;
                                                shades[index] = shadeIndex;
                                                if (changeListener != null)
                                                    changeListener.onChange(Colors.ALL[index].shade(shadeIndex));
                                                invalidate();
                                            }
                                        });
                                        shadePicker.activate(c, shades[futureIndex],
                                                x + dimen / 2, y + dimen / 2);
                                    }
                                }
                            }
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
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

    public void setShadePicker(ShadePicker picker) {
        shadePicker = picker;
    }

    public void setColor(int color) {
        int[] path = Colors.path(color);
        if (path[0] != -1) {
            index = path[0];
            shadeIndex = path[1];
            shades[index] = shadeIndex;
            invalidate();
        }
        final HorizontalScrollView parent = (HorizontalScrollView)getParent();
        parent.post(new Runnable() {
            public void run() {
                parent.smoothScrollTo(getDesiredX(index, parent.getWidth()), 0);
            }
        });
    }
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
    public void onMeasure(int w, int h) {
        setMeasuredDimension((int)dimen * Colors.ALL.length, (int)dimen);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (Colors.ALL == null)
            return;
//        p.setColor(0xFFFFFFFF);
//        canvas.drawPaint(p);
        for (int i=0; i<Colors.ALL.length; i++) {
            int c = Colors.ALL[i].shade(shades[i]);
            Draw.colorItem(c, i * dimen + dimen / 2, dimen / 2, dimen / 2 * .8f, index == i,
                    p, canvas);
        }
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        changeListener = onChangeListener;
    }
    public interface OnChangeListener {
        void onChange(int newColor);
    }
}