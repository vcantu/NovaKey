package viviano.cantu.novakey.view;

import android.content.Context;
import android.view.ViewGroup;

import viviano.cantu.novakey.model.TrueModel;

/**
 * Created by Viviano on 6/6/2016.
 */
public class NovaKeyViewGroup extends ViewGroup {

    private MainView mView;

    /**
     * Initializes the view group
     *
     * @param context
     */
    public NovaKeyViewGroup(Context context) {
        super(context);
        this.mView = new MainView(context);
        this.addView(mView);
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
        this.mView.layout(l, t, r, b);
    }
}
