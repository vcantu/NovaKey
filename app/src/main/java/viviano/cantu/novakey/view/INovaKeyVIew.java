package viviano.cantu.novakey.view;

import android.content.Context;
import android.view.View;

import java.util.List;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.controller.touch.NovaKeyListener;
import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.model.StateModel;

/**
 * Created by Viviano on 6/9/2016.
 */
public abstract class INovaKeyView extends View {

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public INovaKeyView(Context context) {
        super(context);
    }

    /**
     * Sets this view's touch listener
     * @param listener listener to set
     */
    public abstract void setListener(NovaKeyListener listener);

    /**
     * List of elements which this view will draw, in order
     *
     * @param elements list to set
     */
    public abstract void setElements(List<Element> elements);

    /**
     * @return returns this views elements
     */
    public abstract List<Element> getElements();

    /**
     * Sets this view's model which determines
     * what to draw
     *
     * @param model model to set
     */
    public abstract void setStateModel(StateModel model);

    /**
     * @return returns this view's state model
     */
    public abstract StateModel getStateModel();

    /**
     * Sets this view's model which determines
     * how to draw
     *
     * @param model model to set
     */
    public abstract void setDrawModel(DrawModel model);

    /**
     * @return returns this view's draw model
     */
    public abstract DrawModel getDrawModel();

    /**
     * redraws the view according to the model
     */
    public abstract void update();
}
