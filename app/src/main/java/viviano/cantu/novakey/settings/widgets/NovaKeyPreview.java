package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.util.AttributeSet;

import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.view.NovaKeyView;

/**
 * Created by vcantu on 10/2/16.
 */
public class NovaKeyPreview extends NovaKeyView {

    public NovaKeyPreview(Context context) {
        super(context);
    }

    public NovaKeyPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float w = MeasureSpec.getSize(widthMeasureSpec);
        float h = MeasureSpec.getSize(heightMeasureSpec);
        float r = Math.min(h - getPaddingTop() - getPaddingBottom(),
                w - getPaddingRight() - getPaddingLeft());
        r /= 2;
        float x = w / 2;
        float y = getPaddingTop() + r;
        float sr = r / 3;

        MainDimensions dimens = mModel.getMainDimensions();
        dimens.setWidth((int)w);
        dimens.setHeight((int)h);
        dimens.setRadius(r);
        dimens.setSmallRadius(sr);
        dimens.setPadding(getPaddingTop());
        dimens.setX(x);
        dimens.setY(y);
    }
}
