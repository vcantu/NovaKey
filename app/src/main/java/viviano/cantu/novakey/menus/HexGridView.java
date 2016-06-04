package viviano.cantu.novakey.menus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.drawing.Icons;

/**
 * Created by Viviano on 3/30/2016.
 */
public class HexGridView extends View implements GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;

    private Icons.Drawable[][] mGrid;
    private float dimen;
    private int currX = 0, currY = 0;
    private float offX = 0, offY = 0;
    private Paint p;

    public HexGridView(Context context) {
        this(context, null);
    }

    public HexGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HexGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dimen = getResources().getDimension(R.dimen.hex_grid_dimen);

        p = new Paint();
        p.setAntiAlias(true);

        setDrawingCacheEnabled(true);

        mDetector = new GestureDetectorCompat(context, this);
    }

    public void setGrid(Icons.Drawable[][] grid) {
        this.mGrid = grid;
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGrid == null)
            throw new IllegalStateException("Grid has not been set");

        for (int x = -1; currX + x < 9; x++) {

            int actualX = currX + x;
            if (actualX >= 0 && actualX < mGrid.length) {
                for (int y = 0; y < 9; y++) {

                    int actualY = currY + y;
                    if (actualY >= 0 && actualY < mGrid[actualX].length) {
                        Icons.Drawable d = mGrid[actualX][actualY];
                        if (d != null) {
                            boolean odd = actualY % 2 != 0;
                            d.draw(x * dimen + (dimen / 2) * (odd ? 2 : 1) - offX,
                                    y * dimen + (dimen / 2) - offY, dimen * .6f, p, canvas);
                        }
                    }
                }
            }
        }
    }

    private void addScrollDist(float deltaX, float deltaY) {
        offX += deltaX;
        while (offX >= dimen) {
            currX++;
            offX -= dimen;
        }
        while (offX < 0) {
            currX--;
            offX += dimen;
        }

        offY += deltaY;
        while (offY >= dimen) {
            currY++;
            offY -= dimen;
        }
        while (offY < 0) {
            currY--;
            offY += dimen;
        }
    }

    /**
     * Implement this method to handle touch screen motion events.
     * <p/>
     * If this method is used to detect click actions, it is recommended that
     * the actions be performed by implementing and calling
     * {@link #performClick()}. This will ensure consistent system behavior,
     * including:
     * <ul>
     * <li>obeying click sound preferences
     * <li>dispatching OnClickListener calls
     * <li>handling  when
     * accessibility features are enabled
     * </ul>
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }


    /**
     * Notified when a tap occurs with the down {@link MotionEvent}
     * that triggered it. This will be triggered immediately for
     * every down event. All other events should be preceded by this.
     *
     * @param e The down motion event.
     */
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    /**
     * The user has performed a down {@link MotionEvent} and not performed
     * a move or up yet. This event is commonly used to provide visual
     * feedback to the user to let them know that their action has been
     * recognized i.e. highlight an element.
     *
     * @param e The down motion event
     */
    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     * Notified when a tap occurs with the up {@link MotionEvent}
     * that triggered it.
     *
     * @param e The up motion event that completed the first tap
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * Notified when a scroll occurs with the initial on down {@link MotionEvent} and the
     * current move {@link MotionEvent}. The distance in x and y is also supplied for
     * convenience.
     *
     * @param e1        The first down motion event that started the scrolling.
     * @param e2        The move motion event that triggered the current onScroll.
     * @param distanceX The distance along the X axis that has been scrolled since the last
     *                  call to onScroll. This is NOT the distance between {@code e1}
     *                  and {@code e2}.
     * @param distanceY The distance along the Y axis that has been scrolled since the last
     *                  call to onScroll. This is NOT the distance between {@code e1}
     *                  and {@code e2}.
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        addScrollDist(distanceX, distanceY);
        invalidate();
        return true;
    }

    /**
     * Notified when a long press occurs with the initial on down {@link MotionEvent}
     * that trigged it.
     *
     * @param e The initial on down motion event that started the longpress.
     */
    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * Notified of a fling event when it occurs with the initial on down {@link MotionEvent}
     * and the matching up {@link MotionEvent}. The calculated velocity is supplied along
     * the x and y axis in pixels per second.
     *
     * @param e1        The first down motion event that started the fling.
     * @param e2        The move motion event that triggered the current onFling.
     * @param velocityX The velocity of this fling measured in pixels per second
     *                  along the x axis.
     * @param velocityY The velocity of this fling measured in pixels per second
     *                  along the y axis.
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
