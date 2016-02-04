package viviano.cantu.novakey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.themes.BaseTheme;


public class NovaKeyEditView extends View implements View.OnTouchListener {

    public BaseTheme theme;

    //Main Context
    private NovaKey context;

    //Dimensions
    private int screenWidth, screenHeight;//in pixels
    private int viewWidth, viewHeight;
    private float centerX, centerY;
    //Landscape
    private boolean landscape;
    //circle
    public float radius, smallRadius;
    //Drawing
    private Paint p = new Paint();


    //editing
    public boolean moving = false, resizing = false;
    public float moveX, moveY, resizeDist, oldRadius;

    //buttons
    private int currBtn = 0;

    public NovaKeyEditView(Context context) {
        super(context);
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        //set Listener
        setOnTouchListener(this);

        this.context = (NovaKey)context;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        landscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

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
        resizeView();
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);//fixes title bar
        centerY = viewHeight - Settings.sharedPref.getFloat("y" + (landscape ? "_land" : ""), radius);
    }

    public void resizeView() {
        //set view Dimens
        viewWidth = screenWidth;
        viewHeight = screenHeight;

        //set radius to default or to saved size
        radius = Settings.sharedPref.getFloat("size" + (landscape ? "_land" : ""),
                getResources().getDimension(R.dimen.default_radius));
        //updates size of other values when radius is updated
        resizeBoard();
        //sets location to saved size
        centerX = Settings.sharedPref.getFloat("x" + (landscape ? "_land" : ""), screenWidth/2);
        //centerY will be set after method
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private void resizeBoard() {
        smallRadius = radius / 3;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (canvas != null)
            super.onDraw(canvas);

        drawEditButtons(p, canvas);

        theme.drawBackground(0, (centerY - radius), viewWidth, viewHeight, centerX, centerY,
                radius, smallRadius, canvas);
        canvas.drawRect(0, (centerY - radius), viewWidth, viewHeight, p);

        theme.drawBoard(centerX, centerY, radius, smallRadius, canvas);
    }

    private void drawEditButtons(Paint p, Canvas canvas) {
        float rad = getResources().getDimension(R.dimen.button_radius);
        float disp = getResources().getDimension(R.dimen.button_disp);
        float height = getResources().getDimension(R.dimen.button_shad_drop);

        int back = theme.primaryColor(),
            front = theme.secondaryColor();
        Draw.floatingButton(rad + disp, rad + disp, rad, Icon.cancel, back, front,
                currBtn == 1 ? 2 : height, p, canvas);
        Draw.floatingButton(viewWidth - (rad + disp), rad + disp, rad, Icon.accept, back, front,
                currBtn == 3 ? 2 : height, p, canvas);
        Draw.floatingButton(viewWidth / 2, rad + disp, rad, Icon.refresh, back, front,
                currBtn == 2 ? 2 : height, p, canvas);
    }

    /*
    * Will set currBtn to a number [0-3]
    * representing which edit button is x, y on
    * 0 - no button
    * 1 - cancel
    * 2 - reset
    * 3 - save
     */
    private void updateEditButton(float x, float y) {
        Resources res = getResources();
        float rad = res.getDimension(R.dimen.button_radius);
        float disp = res.getDimension(R.dimen.button_disp);

        if (getDistance(x, y, rad + disp, rad + disp) < rad) {//cancel
            currBtn = 1;
        }
        else if (getDistance(x, y, viewWidth / 2, rad + disp) < rad) {//reset
            currBtn = 2;
        }
        else if (getDistance(x, y, viewWidth - (rad + disp), rad + disp) < rad) {//save
            currBtn = 3;
        }
        else
            currBtn = 0;
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float currX = event.getX(0), currY = event.getY(0);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (getDistance(currX, currY, centerX, centerY) <= radius) {
                    moveX = currX - centerX;
                    moveY = currY - centerY;
                    moving = true;
                }
                updateEditButton(currX, currY);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                if (moving || resizing) {
                    if (moving) {
                        centerX = currX - moveX;
                        centerY = currY - moveY;
                    } else if (resizing && event.getPointerCount() > 1) {
                        radius = oldRadius + (getDistance(currX, currY, event.getX(1), event.getY(1)) - resizeDist) / 2;
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

                    if (radius * 2 > viewWidth) {
                        radius = viewWidth / 2;
                        centerX = radius;
                    }
                    if (radius * 2 > viewHeight) {
                        radius = viewHeight / 2;
                        centerY = radius;
                    }

                    if (radius < 100)
                        radius = 100;
                    resizeBoard();
                }

                updateEditButton(currX, currY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                moving = false;
                resizing = false;

                //check if button is pressed
                if (currBtn != 0) {
                    SharedPreferences.Editor editor = Settings.sharedPref.edit();
                    if (currBtn == 1) {//cancel
                        Controller.setEditing(false);
                    }
                    else if (currBtn == 2) {//reset
                        radius = getResources().getDimension(R.dimen.default_radius);
                        centerX = viewWidth / 2;
                        centerY = viewHeight - radius;
                        resizeBoard();
                    }
                    else if (currBtn == 3) {//save
                        editor.putFloat("size" + (landscape ? "_land" : ""), radius);
                        editor.putFloat("x" + (landscape ? "_land" : ""), centerX);
                        editor.putFloat("y" + (landscape ? "_land" : ""), viewHeight - centerY);
                        editor.commit();
                        Controller.setEditing(false);
                    }
                }
                break;

            //for multitouch
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() > 1 && event.getY(1) >= centerY - radius) {
                    oldRadius = radius;
                    resizeDist = getDistance(currX, currY, event.getX(1), event.getY(1));
                    moving = false;
                    resizing = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                    moving = false;//TODO: maybe take off
                    resizing = false;
                break;
        }
        return true;
    }
}
