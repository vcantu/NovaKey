package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 6/7/2015.
 */
public class ThemePicker extends View {

    private Paint p;
    private float dimen;
    private int index = 0;

    private int themeCount;

    private boolean isClick = false;

    private OnChangeListener changeListener;

    public ThemePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        dimen = context.getResources().getDimension(R.dimen.picker_dimen);
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTimer();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isClick) {
                            index = (int)(event.getX() / dimen);
                            if (changeListener != null)
                                changeListener.onChange(index);
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
        themeCount = Theme.COUNT;
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

    @Override
    public void onMeasure(int w, int h) {
        setMeasuredDimension((int)(themeCount * dimen),(int)dimen);
    }

    @Override
    public void onDraw(Canvas canvas) {
//        p.setColor(0xFFFFFFFF);
//        canvas.drawPaint(p);
        for (int i=0; i<themeCount; i++) {
            Theme.getTheme(i).drawPickerIcon(
                    i * dimen + dimen / 2, dimen / 2, dimen / 2 * .8f,
                    (dimen / 2 * .8f) / 3, canvas);
            //draw selected
            if (index == i) {
                p.setStyle(Paint.Style.STROKE);
                p.setColor(0xFF58ACFA);
                p.setStrokeWidth(15);
                canvas.drawCircle(i * dimen + dimen / 2, dimen / 2, dimen / 2 * .8f, p);
                p.setStrokeWidth(0);
                p.setStyle(Paint.Style.FILL);
            }
        }
    }

    public void set(int t) {
        if (t < 0)
            index = Theme.COUNT-1;
        else
            index = t;
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        changeListener = onChangeListener;
    }

    public interface OnChangeListener {
        void onChange(int newIndex);
    }

}
