package viviano.cantu.novakey.view;

import android.content.Context;
import android.graphics.Canvas;

import java.util.List;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.controller.touch.NovaKeyListener;
import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.model.StateModel;

public class NovaKeyView extends INovaKeyView {

	private StateModel mStateModel;
	private DrawModel mDrawModel;

    private List<Element> mElements;

	public NovaKeyView(Context context) {
		super(context);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
	}

    /**
     * List of elements which this view will draw, in order
     *
     * @param elements list to set
     */
    @Override
    public void setElements(List<Element> elements) {
        mElements = elements;
    }

    /**
     * @return returns this views elements
     */
    @Override
    public List<Element> getElements() {
        return mElements;
    }

    /**
	 * Sets this view's model which determines
	 * what to draw
	 *
	 * @param model model to set
	 */
	@Override
	public void setStateModel(StateModel model) {
		mStateModel = model;
	}

    /**
     * @return returns this view's state model
     */
    @Override
    public StateModel getStateModel() {
        return mStateModel;
    }

    /**
	 * Sets this view's model which determins
	 * how to draw
	 *
	 * @param model model to set
	 */
	@Override
	public void setDrawModel(DrawModel model) {
		mDrawModel = model;
	}

    /**
     * @return returns this view's draw model
     */
    @Override
    public DrawModel getDrawModel() {
        return mDrawModel;
    }


    /**
     * Sets this view's touch listener
     *
     * @param listener listener to set
     */
    @Override
    public void setListener(NovaKeyListener listener) {
        this.setOnTouchListener(listener);
    }


	/**
	 * redraws the view according to the model
	 */
	@Override
	public void update() {
		this.invalidate();
	}


	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mDrawModel.getWidth(), mDrawModel.getHeight());
    }

	@Override
	public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		if (mElements == null)
            return;

        for (Element element : mElements) {
            element.draw(this, mDrawModel.getTheme(), canvas);
        }
	}
}







