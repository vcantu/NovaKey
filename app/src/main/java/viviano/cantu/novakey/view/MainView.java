package viviano.cantu.novakey.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import java.util.List;

import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.elements.Element;

public class MainView extends NovaKeyView {

	private Model mModel;

	public MainView(Context context) {
		this(context, null);

	}

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    /**
	 * Sets this view's model which determines
	 * how to draw
	 *
	 * @param model model to set
	 */
	@Override
	public void setModel(Model model) {
		mModel = model;
        mModel.setUpdateListener(() -> invalidate());
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mModel.getWidth(), mModel.getHeight());
    }

	@Override
	public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		if (mModel.getElements() == null)
            return;
        for (Element e : mModel.getElements()) {
            e.draw(mModel, mModel.getTheme(), canvas);
        }
	}
}







