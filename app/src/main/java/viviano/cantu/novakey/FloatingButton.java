package viviano.cantu.novakey;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.drawing.Icons;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 3/8/2016.
 */
public class FloatingButton extends View {

    private int mBackground, mFront;
    private int mHeight, mRealHeight;
    private Icons.Drawable mIcon;
    private float mRadius;

    private Paint p;

    public FloatingButton(Context context) {
        this(context, null);
    }

    public FloatingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        p = new Paint();
        p.setAntiAlias(true);
        mRadius = (int)getResources().getDimension(R.dimen.button_radius);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.FloatingButton, defStyleAttr, 0);

        try {
            mHeight = a.getInteger(R.styleable.FloatingButton_button_height,
                    (int) getResources().getDimension(R.dimen.default_button_height));
            mBackground = a.getColor(R.styleable.FloatingButton_back_color, 0xFF616161);
            mFront = a.getColor(R.styleable.FloatingButton_front_color, 0xFFF0F0F0);
            String str = a.getString(R.styleable.FloatingButton_button_icon);
            mIcon = Icons.get(str);
        } finally {
            a.recycle();
        }
        mRealHeight = mHeight;
    }

    @Override
    public void onMeasure(int w, int h) {
        setMeasuredDimension(mHeight * 2 + (int)mRadius * 2, mHeight * 2 + (int)mRadius * 2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        //Circle
        p.setShadowLayer(mRealHeight + 2, 0, mRealHeight, 0x60000000);
        p.setColor(mBackground);
        canvas.drawCircle(x, y - mRealHeight, mRadius, p);
        p.clearShadowLayer();

        //Icons
        p.setColor(mFront);
        Icons.draw(mIcon, x, y - mRealHeight, mRadius, p, canvas);
    }

    public int getBackColor() {
        return mBackground;
    }

    public void setBackColor(int backColor) {
        this.mBackground = backColor;
    }

    public Icons.Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Icons.Drawable icon) {
        this.mIcon = icon;
    }

    public int getButtonHeight() {
        return mHeight;
    }

    public void setButtonHeight(int height) {
        this.mHeight = height;
    }

    public int getFrontColor() {
        return mFront;
    }

    public void setFrontColor(int front) {
        this.mFront = front;
    }

    public void setTheme(Theme theme) {
        setBackColor(theme.primaryColor());
        setFrontColor(theme.contrastColor());
    }

}
