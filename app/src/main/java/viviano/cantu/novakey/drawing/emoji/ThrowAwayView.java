package viviano.cantu.novakey.drawing.emoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.drawing.Icons;

/**
 * Created by Viviano on 12/6/2015.
 */
public class ThrowAwayView extends View implements View.OnTouchListener {

    private float emojiSize;
    private int maxLine = 7;
    private int index = 0;

    public onClickListener listener;
    private Paint p;

    public ThrowAwayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);

        p = new Paint();
        p.setAntiAlias(true);
    }

    @Override
    public void onMeasure(int w, int h) {
        super.onMeasure(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float size = getResources().getDimension(R.dimen.emojiSize);
        emojiSize = getWidth() / maxLine;

        int x = 0;
        int y = 0;


        for (int i=0; i<5*maxLine; i++) {
            if (index + i >= Emoji.emojis.size())
                break;
            Emoji e = Emoji.emojis.get(index + i);
            Icons.draw(e, x * emojiSize + (emojiSize / 2), y * emojiSize + (emojiSize / 2), size, p, canvas);
            x++;
            if (x >= maxLine) {
                x = 0;
                y++;
            }
        }



    }


    private boolean isClick = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startClickTimer();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (isClick) {
                    int x = (int)(event.getX() / getWidth() * maxLine);
                    int y = (int)(event.getY() / emojiSize);

                    if (y >= 5) {
                        if (x < 3)
                            back();
                        else
                            forward();
                    }
                    else if (listener != null) {
                        int i = index + y * maxLine + x;
                        if (i < Emoji.emojis.size())
                            listener.onClik(Emoji.emojis.get(i));
                    }
                }
                isClick = false;
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

    public void setListener(onClickListener list) {
        listener = list;
    }

    public interface onClickListener {
        void onClik(Emoji e);
    }

    private void back() {
        index -= 6 * maxLine;
        if (index < 0)
            index = 0;
        invalidate();
    }
    private void forward() {
        index += 6 * maxLine;
        if (index >= Emoji.emojis.size()) {
            index = Emoji.emojis.size() - 1;
        }
        invalidate();

    }
}
