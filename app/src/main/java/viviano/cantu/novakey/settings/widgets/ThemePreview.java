package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.NovaKeyDimen;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.themes.BaseTheme;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 6/6/2015.
 */
public class ThemePreview extends View {

    protected Theme theme;
    protected Paint p;

    private NovaKeyDimen mDimens;

    public ThemePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        p = new Paint();
        theme = new BaseTheme();
        theme.setColors(0xFF616161, 0xFFF5F5F5, 0xFFF5F5F5);//TODO:
        setViewDimen();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setViewDimen();
    }
    
    
    private void setViewDimen() {
        float w = getWidth();
        float h = getHeight();
        float r = Math.min(h - getPaddingTop() - getPaddingBottom(),
                w - getPaddingRight() - getPaddingLeft());
        r /= 2;
        float x = w / 2;
        float y = getPaddingTop() + r;
        float sr = r / 3;

        //Create a keyboard from a resource
        KeyLayout kl = new KeyLayout("English", KeyLayout.convert(R.array.English, getResources()));
        kl.updateCoords(x, y, r, sr);
        
        mDimens = new NovaKeyDimen(x, y, w, h, r, sr, kl);
    }

    @Override
    public void onDraw(Canvas canvas) {
        setViewDimen();

        theme.drawBackground(0, 0, mDimens.w, mDimens.h, mDimens.x, mDimens.y,
                mDimens.r, mDimens.sr, canvas);

        theme.drawBoard(mDimens.x, mDimens.y, mDimens.r, mDimens.sr, canvas);

        //draw main keys
        theme.drawKeys(mDimens.x, mDimens.y, mDimens.r, mDimens.sr, mDimens.kl,
                false, canvas);

        theme.drawButtons(mDimens, canvas);


        //Uncomment this to test contrast formula
//        float ratio = Util.contrastRatio(theme.secondaryColor(), theme.primaryColor());
//        p.setTextSize(40);
//        p.setmColor(theme.secondaryColor());
//        if (theme.primaryColor() == theme.secondaryColor())
//            p.setmColor(Util.contrastColor(theme.primaryColor()));
//        Draw.text(ratio + "", centerX + radius + 70, centerY, p, canvas);
    }

    public void set(Theme t) {
        theme = t;
    }
}
