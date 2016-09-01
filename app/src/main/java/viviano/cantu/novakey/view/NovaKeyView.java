package viviano.cantu.novakey.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.controller.touch.NovaKeyListener;
import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 6/9/2016.
 */
public abstract class NovaKeyView extends View {

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public NovaKeyView(Context context) {
        this(context, null);
    }

    public NovaKeyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public NovaKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets this view's model which determines
     * how to draw
     *
     * @param model model to set
     */
    public abstract void setModel(Model model);
}
