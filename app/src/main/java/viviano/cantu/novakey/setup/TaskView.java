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
import viviano.cantu.novakey.utils.Util;

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
    private OnFinishListener mOnFinishList;

    private ArrayList<Task> tasks;


    /**
     * Constructor also sets touch listener
     *
     * @param context application context
     * @param attrs XML attributes
     */
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
                                if (forward.state != DISABLED && forward.state != INVISIBLE) {//forward pressed
                                    if (index < tasks.size() - 1)
                                        forward();
                                    else {
                                        if (mOnFinishList != null)
                                            mOnFinishList.onFinish();
                                    }
                                }
                            }
                        }
                        invalidate();
                }
                return true;
            }
        });
    }

    /**
     * Sets the forward button to DISABLED
     */
    public void disableNext() {
        if (forward != null) {
            forward.state = DISABLED;
            invalidate();
        }
    }

    /**
     * Sets the forward button to ENABLED
     */
    public void enableNext() {
        if (forward != null) {
            forward.state = ENABLED;
            invalidate();
        }
    }

    /**
     * Calls the isComplete() on the current task
     * @param test String to test
     * @return returns the result of the isComplete() method
     */
    public boolean isComplete(String test) {
        if (tasks != null) {
            return tasks.get(index).isComplete(test);
        }
        return false;
    }

    /**
     * Sets the list of tasks
     * @param tasks
     */
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * @return returns current index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the size of the view & creates the buttons
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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

    /**
     * Draws view on given canvas
     * @param canvas canvas given
     */
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

    /**
     * Draws task at the given index, at the given x, on the vigen canvas
     *
     * @param index index of task to draw
     * @param x Left position of task
     * @param canvas canvas to draw on
     */
    private void drawTask(int index, float x, Canvas canvas) {
        float maxLength = Math.min(getWidth() - getPaddingRight() - getPaddingRight(),
                getResources().getDimension(R.dimen.tut_min_text_length));
        String str = Util.toMultiline(tasks.get(index).mainText(), p, maxLength);
        p.setColor(0xFFF0F0F0);
        Draw.text(str, x + getWidth() / 2, getHeight() - dimen * 2.5f, p, canvas);
    }

    /**
     * Draws progress map on the top
     *
     * @param canvas canvas to draw on
     */
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

    /**
     * Private Text button class for the BACK & NEXT buttons
     */
    private final static int ENABLED = 0, DISABLED = 1, INVISIBLE = 2;
    private class TextButton {
        public String text;
        private float x, y;
        public int state = ENABLED;

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

    /**
     * Animates task to the given index
     * @param i index to animate to
     * @param complete whether the animation is being completed or starting from 0
     */
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

    /**
     * Animates backward according to the current index
     */
    private void back() {
        if (index > 0)
            beginAnimation(index - 1, true);
        else
            reset();
    }

    /**
     * Animates forward according to the current index
     */
    private void forward() {
        if (index < tasks.size() - 1)
            beginAnimation(index + 1, false);
    }

    /**
     * Animates to the first task
     */
    private void reset() {
        beginAnimation(0, false);
    }

    /**
     * update button states to work correctly.
     * Back button will be invisible at the first task
     * Forward button will be "Done" at the last task
     */
    private void updateButtonStates() {
        if (index == 0)
            back.state = INVISIBLE;
        else
            back.state = ENABLED;

        if (tasks != null && index == tasks.size()-1) {
            forward.text = "Done";
            forward.state = ENABLED;
        } else
            forward.text = "Next";
    }


    /**
     * Sets the on index change listener
     * @param listener
     */
    public void setOnIndexChangeListener(OnIndexChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Interface listener that will call onNewIndex(int, int)
     */
    public interface OnIndexChangeListener {
        /**
         * Callback method that is called when the used changes to new task
         *
         * @param index index it was changed to
         * @param prev index it was changed from
         */
        void onNewIndex(int index, int prev);
    }

    /**
     * Sets the onFinishListener
     * @param listener
     */
    public void setOnFinishListener(OnFinishListener listener) {
        this.mOnFinishList = listener;
    }

    /**
     * Interface listener that will call onFinish()
     */
    public interface OnFinishListener {
        /**
         * called when the user presses Done
         */
        void onFinish();
    }
}
