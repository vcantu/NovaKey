package viviano.cantu.novakey.settings.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.Util;
import viviano.cantu.novakey.settings.Colors;

/**
 * Created by Viviano on 8/4/2015.
 */
public class ShadePicker extends View {

    private float dimen;
    private Paint p;

    private float centerX, centerY, radius;

    private Colors colors;

    private SelectedListener listener;
    private int index = -1;

    private boolean isClick = true;

    public ShadePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        dimen = getResources().getDimension(R.dimen.picker_dimen);
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        setVisibility(INVISIBLE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float d = Util.distance(getWidth() / 2, getHeight() / 2, event.getX(), event.getY());
                double a = Util.angle(getWidth()/2, getHeight()/2, event.getX(), event.getY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTimer();
                    case MotionEvent.ACTION_MOVE:
                        if (d >= dimen && d <= dimen * 2) {
                            updateShade(a, false);
                            invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isClick) {
                            if (d <= dimen * 2) {
                                if (d >= dimen)
                                    updateShade(a, true);
                                else {
                                    if (listener != null)
                                        listener.onShadeSelected(colors.shade(index));
                                    invalidate();
                                }
                                deactivate();
                            }
                                else {
                                if (listener != null)
                                    listener.onCancel();
                            }
                            deactivate();
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

    private void updateShade(double a, boolean commit) {
        int newIndex = getIndex(a, colors.size(), Math.PI * 2 / colors.size() / 2);
        newIndex = (newIndex + colors.size()) % colors.size();
        if (!commit) {
            if (index != newIndex) {
                index = newIndex;
                if (listener != null)
                    listener.onShadeUpdated(index, colors.shade(index));
                invalidate();
            }
        }
        else {
            if (listener != null)
                listener.onShadeSelected(colors.shade(index));
            invalidate();
        }
    }

    public void deactivate() {
        setVisibility(INVISIBLE);
    }

    public void activate(Colors c, int shadeIndex, final float x, final float y) {
        setVisibility(VISIBLE);
        colors = c;
        index = shadeIndex;
        if (listener != null)
            listener.onShadeUpdated(index, c.shade(index));

        final float desX = centerX, desY = centerY, desR = radius;
        final ValueAnimator anim = ValueAnimator.ofFloat(0, 100)
                .setDuration(300);
        anim.setInterpolator(new OvershootInterpolator(2));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac = (Float)anim.getAnimatedValue() / 100;
                centerX = x + (desX-x) * frac;
                centerY = y + (desY-y) * frac;
                radius = desR * frac;
                invalidate();
            }
        });
        anim.start();
    }

    @Override
    public void onMeasure(int w, int h) {
        super.onMeasure(w, h);
        centerX = getWidth()/2;
        centerY = getHeight()/2;
        radius = dimen * 2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        p.setColor(0xFFFFFFFF);
        canvas.drawCircle(centerX, centerY, radius, p);
        p.setStyle(Paint.Style.FILL);
        for (int i=0; i<colors.size(); i++) {
            double a = (Math.PI*2)/colors.size() * i;
            float d = (radius/2) * 1.5f;
            drawShade(colors.shade(i), centerX + (float)Math.cos(a) * d,
                                   centerY + (float)Math.sin(a) * d,
                    index != -1 && index == i, p, canvas);
        }
        Draw.colorItem(colors.shade(index), centerX, centerY, radius / 4, true, p, canvas);
    }

    private void drawShade(int color, float x, float y, boolean selected, Paint p, Canvas canvas) {
        float r = radius / 4 * .8f;
        Draw.colorItem(color, selected, x, y, r, p, canvas);
    }

    public int getIndex(double angle, int size, double startAngle) {
        angle = (angle < startAngle ? Math.PI * 2 + angle : angle);
        for (int i=0; i<size; i++) {
            double angle1 = (i * 2 * Math.PI) / size  + startAngle;
            double angle2 = ((i+1) * 2 * Math.PI) / size  + startAngle;
            if (angle >= angle1 && angle < angle2)
                return i+1;
        }
        return -1;
    }


    public void setSelectedListener(SelectedListener selectedListener) {
        this.listener = selectedListener;
    }
    public interface SelectedListener {
        void onShadeSelected(int color);
        void onShadeUpdated(int index, int color);
        void onCancel();
    }
}
