package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 6/6/2015.
 */
public class ThemePreview extends View {

    protected Theme theme;
    protected Paint p;

    //dimensions
    public float radius;
    protected float viewWidth, viewHeight, centerX, centerY, smallRadius;
    public KeyLayout mKeyLayout;

    public ThemePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        theme = new Theme();
        theme.setColor(0xFF616161, 0xFFF5F5F5, 0xFFF5F5F5);//TODO:
        setViewDimen();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setViewDimen();
    }
    private void setViewDimen() {
        viewWidth = getWidth();
        viewHeight = getHeight();
        radius = Math.min(viewHeight - getPaddingTop() - getPaddingBottom(),
                          viewWidth - getPaddingRight() - getPaddingLeft());
        radius /= 2;
        centerX = viewWidth / 2;
        centerY = getPaddingTop() + radius;
        smallRadius = radius / 3;

        //Create a keyboard from a resource
        mKeyLayout = new KeyLayout("English", KeyLayout.convert(R.array.English, getResources()));
        mKeyLayout.updateCoords(centerX, centerY, radius, smallRadius);
    }

    @Override
    public void onDraw(Canvas canvas) {
        setViewDimen();

        theme.drawBackground(0, 0, viewWidth, viewHeight, centerX, centerY,
                radius, smallRadius, canvas);

        theme.drawBoard(centerX, centerY, radius, smallRadius, canvas);

        //draw main keys
        theme.drawKeys(centerX, centerY, radius, smallRadius, mKeyLayout,
                false, canvas);

        theme.drawButtons(centerX, centerY, radius, canvas);


        //Uncomment this to test contrast formula
//        float ratio = Util.contrastRatio(theme.secondaryColor(), theme.primaryColor());
//        p.setTextSize(40);
//        p.setColor(theme.secondaryColor());
//        if (theme.primaryColor() == theme.secondaryColor())
//            p.setColor(Util.contrastColor(theme.primaryColor()));
//        Draw.text(ratio + "", centerX + radius + 70, centerY, p, canvas);
    }

    public void set(Theme t) {
        theme = t;
    }
}
