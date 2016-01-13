package viviano.cantu.novakey.setup;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.Font;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.Util;

/**
 * Created by Viviano on 7/31/2015.
 */
public class TaskView extends View {

    private float dimen;
    private Paint p = new Paint();

    private TextButton back, forward;

    private int index = 0;
    private float currX = 0;

    private OnIndexChangeListener listener;

    private ArrayList<Task> tasks;

    public TaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dimen = getResources().getDimension(R.dimen.tut_text_size);

        float dp = getResources().getDimension(R.dimen.tut_btn_dimen);
        back = new TextButton("Back", dp, dimen/2);
        forward = new TextButton("Next", getWidth() - dp, dimen/2);
        updateButtonStates();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==  MotionEvent.ACTION_DOWN) {
                        if (event.getY() <= dimen * 1.5f) {
                            if (event.getX() <= getWidth() / 2) {
                                if (back.state != DISABLED && back.state != INVISIBLE)//back pressed
                                    back();
                            }
                            else {
                                if (forward.state != DISABLED && forward.state != INVISIBLE)//forward pressed
                                    forward();
                            }
                        }
                        invalidate();
                }
                return true;
            }
        });
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, (int) dimen * 7);
        //first line for buttons
        //second line for space
        //last 5 for text
        float dp = getResources().getDimension(R.dimen.tut_btn_dimen);
        back = new TextButton("Back", dp, dimen);
        forward = new TextButton("Next", getWidth() - dp, dimen);
        updateButtonStates();
    }

    @Override
    public void onDraw(Canvas canvas) {
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        p.setTypeface(Font.SANS_SERIF_LIGHT);
        p.setTextSize(dimen * .8f);
        for (int i=0; i< tasks.size(); i++) {
            drawTask(i, currX + getWidth() * i, canvas);
        }
        drawMap(canvas);
        back.draw(canvas);
        forward.draw(canvas);
    }

    private void drawTask(int index, float x, Canvas canvas) {
        float maxLength = Math.min(getWidth() - getPaddingRight() - getPaddingRight(),
                getResources().getDimension(R.dimen.tut_min_text_length));
        String str = Util.toMultiline(tasks.get(index).mainText(), p, maxLength);
        p.setColor(0xFFF0F0F0);
        Draw.text(str, x + getWidth() / 2, getHeight() - dimen * 2.5f, p, canvas);
    }

    private void drawMap(Canvas canvas) {
        int count = tasks.size();
        float mapHeight = dimen / 2,
                length = mapHeight * count,
                mapX = length / (getWidth() * tasks.size()) * currX;
        mapX *= -1;
        p.setColor(0x50ffffff);

        for (int i=0; i<count; i++) {
            canvas.drawCircle(getWidth() / 2 - length / 2 + (i * mapHeight + mapHeight / 2),
                    dimen /2, mapHeight / 4, p);
        }
        p.setColor(0xFFffffff);
        canvas.drawCircle((getWidth() / 2 - length / 2 + (mapHeight / 2)) + mapX,
                dimen / 2, mapHeight / 4, p);
    }

    private static int DEFAULT = 0, DISABLED = 1, INVISIBLE = 2;
    private class TextButton {
        public String text;
        private float x, y;
        public int state = DEFAULT;

        TextButton(String text, float x, float y) {
            this.x = x;
            this.y = y;
            this.text = text;
        }

        void draw(Canvas canvas) {
            p.setColor(0xFFF0F0F0);
            if (state != INVISIBLE) {
                if (state == DISABLED)
                    p.setColor(0xFF909090);
                Draw.text(text, x, y, p, canvas);
            }
        }
    }

    private void beginAnimation(final int i, boolean complete) {
        final ValueAnimator anim = ValueAnimator.ofFloat(currX, -i*getWidth())
                .setDuration(400);
        anim.setInterpolator(!complete ? new AnticipateOvershootInterpolator(.5f) :
                new OvershootInterpolator(.5f));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currX = (Float) anim.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int prev = index;
                index = i;
                updateButtonStates();
                if (listener != null)
                    listener.onNewIndex(index, prev);
                invalidate();
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

    private void back() {
        if (index > 0)
            beginAnimation(index - 1, true);
        else
            reset();
    }

    private void forward() {
        if (index < tasks.size() - 1)
            beginAnimation(index + 1, false);
    }

    private void reset() {
        beginAnimation(0, false);
    }

    private void updateButtonStates() {
        if (index == 0)
            back.state = INVISIBLE;
        else
            back.state = DEFAULT;

        if (tasks != null && index == tasks.size()-1)
            forward.text = "Done";
        else
            forward.text = "Next";
    }

    public void setListener(OnIndexChangeListener listener) {
        this.listener = listener;
    }

    public interface OnIndexChangeListener {
        void onNewIndex(int index, int prev);
    }
}
