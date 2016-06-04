package viviano.cantu.novakey.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Viviano on 6/3/2016.
 */
public class BoardViewGroup extends ViewGroup {

    private NovaKeyView view;

    /**
     * Constructor for the board view group
     * can only be created programmatically
     *
     * @param context main context of app
     */
    public BoardViewGroup(Context context) {
        super(context);
        this.view = new NovaKeyView(context);
        this.addView(this.view);

    }

    /**
     * <p>
     * Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by {@link #measure(int, int)} and
     * should be overridden by subclasses to provide accurate and efficient
     * measurement of their contents.
     * </p>
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * {@inheritDoc}
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        view.layout(l, t, r, b);
    }
}
