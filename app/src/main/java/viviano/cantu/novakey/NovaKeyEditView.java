package viviano.cantu.novakey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.Themeable;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.utils.Util;


public class NovaKeyEditView extends View implements View.OnTouchListener, Themeable {

    //Theme to use
    private MasterTheme mTheme;

    //Dimensions
    private final int screenWidth, screenHeight;//in pixels
    private int viewWidth, viewHeight;
    private float centerX, centerY;
    //Landscape
    private boolean landscape;
    //circle
    private float radius, smallRadius;
    //Drawing
    private Paint p;

    //editing
    private boolean moving = false, resizing = false;
    private float moveX, moveY;//moving
    private float resizeDist, oldRadius;//resizing


    public NovaKeyEditView(Context context) {
        this(context, null);
    }

    public NovaKeyEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovaKeyEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        //set Listener
        setOnTouchListener(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        landscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Will set this object's theme
     *
     * @param theme a Master Theme
     */
    @Override
    public void setTheme(MasterTheme theme) {
        mTheme = theme;
    }

    //Checks if phone orientation has been changed
    @Override
    public void onConfigurationChanged(Configuration config) {
        landscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
        super.onConfigurationChanged(config);
    }

    //When created or resized
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //set view dimensions
        //set view Dimens
        viewWidth = screenWidth;
        viewHeight = screenHeight;

        //set radius to default or to saved size
        radius = Settings.sharedPref.getFloat("size" + (landscape ? "_land" : ""),
                getResources().getDimension(R.dimen.default_radius));

        smallRadius = Settings.sharedPref.getFloat("smallRadius", 3);
        //sets location to saved size
        centerX = Settings.sharedPref.getFloat("x" + (landscape ? "_land" : ""), screenWidth / 2);

        //centerY will be set after method
        setMeasuredDimension(viewWidth, viewHeight);

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);//fixes title bar

        centerY = viewHeight - Settings.sharedPref.getFloat("y" + (landscape ? "_land" : ""), radius);
    }

    @Override
    public void onDraw(Canvas canvas) {
        mTheme.getBackgroundTheme()
                .drawBackground(0, (centerY - radius), viewWidth, viewHeight, centerX, centerY,
                radius, radius / smallRadius, canvas);

        mTheme.getBoardTheme().drawBoard(centerX, centerY, radius, radius / smallRadius, canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float currX = event.getX(0), currY = event.getY(0);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (Util.distance(currX, currY, centerX, centerY) <= radius) {
                    moveX = currX - centerX;
                    moveY = currY - centerY;
                    moving = true;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                if (moving || resizing) {
                    if (moving) {
                        centerX = currX - moveX;
                        centerY = currY - moveY;
                    } else if (resizing && event.getPointerCount() > 1) {
                        radius = oldRadius + (Util.distance(currX, currY, event.getX(1), event.getY(1)) - resizeDist) / 2;
                    }
                    //Checks Edit Bounds
                    if (centerX + radius > viewWidth)
                        centerX = viewWidth - radius;
                    if (centerX - radius < 0)
                        centerX = radius;
                    if (centerY + radius > viewHeight)
                        centerY = viewHeight - radius;
                    if (centerY - radius < 0)
                        centerY = radius;

                    //center
                    if (Math.abs(centerX - viewWidth / 2) < getResources().getDimension(R.dimen.center_threshold))
                        centerX = viewWidth / 2;

                    // max radius
                    if (radius * 2 > viewWidth) {
                        radius = viewWidth / 2;
                        centerX = radius;
                    }
                    if (radius * 2 > viewHeight) {
                        radius = viewHeight / 2;
                        centerY = radius;
                    }
                    //min radius
                    if (radius < getResources().getDimension(R.dimen.min_radius))
                        radius = getResources().getDimension(R.dimen.min_radius);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                moving = false;
                resizing = false;
                break;

            //for multitouch
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() > 1 && event.getY(1) >= centerY - radius) {
                    oldRadius = radius;
                    resizeDist = Util.distance(currX, currY, event.getX(1), event.getY(1));
                    moving = false;
                    resizing = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                moving = false;
                resizing = false;
                break;
        }
        return true;
    }

    public void resetDimens() {
        radius = getResources().getDimension(R.dimen.default_radius);
        centerX = viewWidth / 2;
        centerY = viewHeight - radius;
        smallRadius = 3;
        invalidate();
    }

    public void saveDimens() {
        SharedPreferences.Editor editor = Settings.sharedPref.edit();
        editor.putFloat("size" + (landscape ? "_land" : ""), radius);
        editor.putFloat("smallRadius", smallRadius);
        editor.putFloat("x" + (landscape ? "_land" : ""), centerX);
        editor.putFloat("y" + (landscape ? "_land" : ""), viewHeight - centerY);
        editor.commit();
    }

    public void setSmallRadius(float sr) {
        smallRadius = sr;
    }
}
