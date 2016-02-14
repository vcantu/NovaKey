package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.NovaKeyDimen;
import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.btns.BtnTheme;

/**
 * Created by Viviano on 6/30/2015.
 */
public class ButtonAddView extends View {

    private float radius;
    private Btn btn;
    private BtnTheme mBtnTheme;

    public ButtonAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs
        mBtnTheme = new BtnTheme();
        mBtnTheme.setColors(0xFF616161, 0xFFF5F5F5);
        btn = new Btn(Math.PI/2,0,Btn.CIRCLE|Btn.SMALL);//this will be updated right away
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        NovaKeyDimen dimens =
                new NovaKeyDimen(getWidth()/2, getHeight()/2,
                        radius * 2, radius * 2,
                        radius, radius / 2, null);

        if ((btn.shape&Btn.SHAPE)==Btn.ARC) {
            btn.dist = 1;
            dimens.y -= radius;
            btn.draw(dimens, mBtnTheme, canvas);
        }
        else {
            btn.dist = 0;
            btn.draw(dimens, mBtnTheme, canvas);
        }
    }

    public void setShape(int shape) {
        btn.shape = shape;
        invalidate();
    }

    public int btnShape() {
        return btn.shape;
    }

    public void enableAddIcon() {
        btn.icon = Icon.get("add");
    }
}
