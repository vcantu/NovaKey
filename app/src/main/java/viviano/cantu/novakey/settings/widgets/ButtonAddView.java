package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.elements.buttons.Button;
import viviano.cantu.novakey.view.drawing.Icons;

/**
 * Created by Viviano on 6/30/2015.
 */
public class ButtonAddView extends View {

    private float radius;
    private Button button;
    private BtnTheme mBtnTheme;

    public ButtonAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs
        mBtnTheme = new BtnTheme();
        mBtnTheme.setColors(0xFF616161, 0xFFF5F5F5);
        button = new Button(Math.PI/2,0, Button.CIRCLE| Button.SMALL);//this will be updated right away
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

        if ((button.shape& Button.SHAPE)== Button.ARC) {
            button.dist = 1;
            dimens.y -= radius;
            button.draw(, mBtnTheme, canvas);
        }
        else {
            button.dist = 0;
            button.draw(, mBtnTheme, canvas);
        }
    }

    public void setShape(int shape) {
        button.shape = shape;
        invalidate();
    }

    public int btnShape() {
        return button.shape;
    }

    public void enableAddIcon() {
        button.icon = Icons.get("add");
    }
}
