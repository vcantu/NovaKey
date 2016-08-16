package viviano.cantu.novakey.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Viviano on 3/8/2016.
 */
public class ControlView extends View {

    public ControlView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        setMeasuredDimension(metrics.widthPixels, 0);
    }
}
