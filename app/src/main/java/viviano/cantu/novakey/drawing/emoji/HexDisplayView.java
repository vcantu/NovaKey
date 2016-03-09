package viviano.cantu.novakey.drawing.emoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.drawing.Icons;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 12/6/2015.
 */
public class HexDisplayView extends View {

    private int index = 200;
    private float offsetX = 0, offsetY = 0;
    private float emojiSize;
    private ArrayList<Integer> alreadyDrawn;
    private Paint p;

    public HexDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        emojiSize = this.getResources().getDimension(R.dimen.emojiSize);

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
        alreadyDrawn = new ArrayList<>();
        drawEmoji(Emoji.emojis.get(index),
                getWidth() / 2 + offsetX, getHeight() / 2 + offsetY,
                canvas);
    }

    private void drawEmoji(Emoji e, float x, float y, Canvas canvas) {
        if (!alreadyDrawn.contains(e.id())) {
            //first draw itself
            Icons.draw(e, x, y, emojiSize * .8f, p, canvas);
            alreadyDrawn.add(e.id());
            //draw neightbors
            for (int i = 0; i < 6; i++) {
                if (e.getNeighbor(i) != null) {
                    boolean canDraw = false;
                    if (i == 0 || i == 5) {
                        //check if not too high
                        canDraw = y > 0;
                    } else if (i == 1) {
                        //check if not too left
                        canDraw = x > 0;
                    } else if (i == 4) {
                        //check if not too right
                        canDraw = x < getWidth();
                    } else {
                        //check if not too low
                        canDraw = y < getHeight();
                    }

                    if (canDraw) {
                        drawEmoji(e.getNeighbor(i), x + getXdiff(i), y + getYdiff(i), canvas);
                    }
                }
            }
        }
    }

    private double getAngle(int index) {
        double sixth = Math.PI * 2 / 6;
        return (sixth * 2) + (sixth * index);
    }

    private float getXdiff(int index) {
        double a = getAngle(index);
        return (float)(Math.cos(a) * emojiSize * 2);
    }

    private float getYdiff(int index) {
        double a = getAngle(index);
        return (float)(Math.sin(a) * emojiSize * 2);
    }

    // if the index should change, return the hex index of the current index
    // ele return -1
    private int hexIndex() {
        float dist = Util.distance(0, 0, offsetX, offsetY);
        if (dist > emojiSize) {
            int i = (int)(Util.getAngle(offsetX, offsetY) / (Math.PI * 2 / 6));
            i += 4;
            return i % 6;
        }

        return -1;
    }
}
