package viviano.cantu.novakey.view;

import android.content.Context;
import android.util.AttributeSet;

public class MainView extends NovaKeyView {

	public MainView(Context context) {
		this(context, null);

	}

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }


	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(
				mModel.getMainDimensions().getWidth(),
				mModel.getMainDimensions().getHeight());
    }

}







