package viviano.cantu.novakey.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import java.util.List;

import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.elements.Element;

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
		setMeasuredDimension(mModel.getWidth(), mModel.getHeight());
    }

}







