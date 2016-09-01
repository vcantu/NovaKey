package viviano.cantu.novakey.setup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 10/22/2015.
 */
public class IconView extends View implements View.OnTouchListener {

    private boolean touched = false;
    private Drawable icon;
    private float size = 1.0f;
    private Paint p;
    private int mColor = 0xFFA0A0A0;

    private OnClickListener listener;

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setOnTouchListener(this);
        p = new Paint();
        p.setAntiAlias(true);
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setColor(int color) {
        mColor = color;
    }

    @Override
    public void onDraw(Canvas canvas) {
        float w = getWidth(), h = getHeight();
        p.setColor(touched ? mColor : mColor);//TODO: make color lighter
        icon.draw(w / 2, h / 2, w * size, p, canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touched = true;
                if (listener != null)
                    listener.onClick();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                invalidate();
                break;
        }
        return true;
    }

    public void setClickListener(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onClick();
    }
}
