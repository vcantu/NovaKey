package viviano.cantu.novakey.settings.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by Viviano on 1/19/2016.
 */
public class ObservableHorizontalScrollView extends HorizontalScrollView {

    OnScrollListener mListener;

    public ObservableHorizontalScrollView(Context context) {
        super(context);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(mListener != null) {
            mListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mListener = listener;
    }

    public interface OnScrollListener {
        void onScrollChanged(View view, int x, int y, int oldx, int oldy);
    }
}
