package viviano.cantu.novakey;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import viviano.cantu.novakey.drawing.Icons;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.themes.Theme;

/**
 * Created by Viviano on 3/5/2016.
 */
public class EditView extends RelativeLayout {

    private final NovaKeyEditView mResizeView;
    private final FloatingButton mCancel, mRefresh, mAccept;
    private final IconView mResetSr;
    private final SeekBar mSeekBar;
    private final int MIN = 20, MAX = 35, DEFAULT = 3;

    public EditView(Context context) {
        this(context, null);
    }

    public EditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.edit_view_layout, this, true);

        mResizeView = (NovaKeyEditView)findViewById(R.id.resize_view);

        mCancel = (FloatingButton)findViewById(R.id.cancel);
        mCancel.setIcon(Icons.get("clear"));

        mRefresh = (FloatingButton)findViewById(R.id.refresh);
        mRefresh.setIcon(Icons.get("refresh"));

        mAccept = (FloatingButton)findViewById(R.id.accept);
        mAccept.setIcon(Icons.get("check"));

        mSeekBar = (SeekBar)findViewById(R.id.seek_bar);
        mSeekBar.setMax(MAX - MIN);

        float sr = Settings.sharedPref.getFloat("smallRadius", DEFAULT);
        mSeekBar.setProgress(srToProgress(sr));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float sr = ((MIN + MAX) - (progress + MIN)) / 10f;
                mResizeView.setSmallRadius(sr);
                mResizeView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mResetSr = (IconView)findViewById(R.id.reset_sr);
        mResetSr.setIcon(Icons.get("refresh"));
        mResetSr.setSize(.8f);
        mResetSr.setClickListener(new IconView.OnClickListener() {
            @Override
            public void onClick() {
                mSeekBar.setProgress(srToProgress(DEFAULT));
            }
        });

        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        mRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        mAccept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
    }

    /**
     * Will set this view's theme with the given one
     *
     * @param theme theme to set to
     * @throws IllegalArgumentException if the theme passed is null
     */
    public void setTheme(Theme theme) {
        mResizeView.setTheme(theme);

        mCancel.setTheme(theme);
        mRefresh.setTheme(theme);
        mAccept.setTheme(theme);

        mResetSr.setColor(theme.contrastColor());

        Drawable d = mSeekBar.getBackground();
        if (d != null)
            d.setColorFilter(theme.primaryColor(), PorterDuff.Mode.MULTIPLY);
        d = mSeekBar.getProgressDrawable();
        if (d != null)
            d.setColorFilter(theme.contrastColor(), PorterDuff.Mode.MULTIPLY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            d = mSeekBar.getThumb();
            d.setColorFilter(theme.contrastColor(), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void onCancel() {
        Controller.setEditing(false);
    }

    private void onRefresh() {
        mResizeView.resetDimens();
        mSeekBar.setProgress(srToProgress(DEFAULT));
    }

    private void onSave() {
        mResizeView.saveDimens();
        Controller.setEditing(false);
    }

    private int srToProgress(float sr) {
        sr *= 10;
        return (int)(MAX - sr);
    }
}
