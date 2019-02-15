/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.core.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import viviano.cantu.novakey.core.R;
import viviano.cantu.novakey.core.controller.Gun;
import viviano.cantu.novakey.core.actions.SetEditingAction;
import viviano.cantu.novakey.core.FloatingButton;
import viviano.cantu.novakey.core.IconView;
import viviano.cantu.novakey.core.utils.drawing.Icons;
import viviano.cantu.novakey.core.view.themes.MasterTheme;
import viviano.cantu.novakey.core.view.themes.Themeable;

/**
 * Created by Viviano on 3/5/2016.
 */
public class EditView extends RelativeLayout implements Themeable {

    private final NovaKeyEditView mResizeView;
    private final FloatingButton mCancel, mRefresh, mAccept;
    private final IconView mResetSr;
    private final SeekBar mSeekBar;
    private final int MIN = 20, MAX = 35, DEFAULT = 3;
    private final Gun mGun;


    public EditView(Context context, Gun gun) {
        super(context);
        mGun = gun;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.edit_view_layout, this, true);

        mResizeView = (NovaKeyEditView) findViewById(R.id.resize_view);

        mCancel = (FloatingButton) findViewById(R.id.cancel);
        mCancel.setIcon(Icons.get("clear"));

        mRefresh = (FloatingButton) findViewById(R.id.refresh);
        mRefresh.setIcon(Icons.get("refresh"));

        mAccept = (FloatingButton) findViewById(R.id.accept);
        mAccept.setIcon(Icons.get("check"));

        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mSeekBar.setMax(MAX - MIN);

        float sr = mResizeView.getRadius() / mResizeView.getSmallRadius();
        mSeekBar.setProgress(srToProgress(sr));
        mResizeView.setSmallRadius(sr);
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

        mResetSr = (IconView) findViewById(R.id.reset_sr);
        mResetSr.setIcon(Icons.get("refresh"));
        mResetSr.setSize(.8f);
        mResetSr.setClickListener(() -> mSeekBar.setProgress(srToProgress(DEFAULT)));

        mCancel.setOnClickListener(v -> onCancel());
        mRefresh.setOnClickListener(v -> onRefresh());
        mAccept.setOnClickListener(v -> onSave());
    }


    /**
     * Will set this view's theme with the given one
     *
     * @param theme theme to set to
     * @throws IllegalArgumentException if the theme passed is null
     */
    public void setTheme(MasterTheme theme) {
        mResizeView.setTheme(theme);

        mCancel.setTheme(theme);
        mRefresh.setTheme(theme);
        mAccept.setTheme(theme);

        mResetSr.setColor(theme.getContrastColor());

        Drawable d = mSeekBar.getBackground();
        if (d != null)
            d.setColorFilter(theme.getPrimaryColor(), PorterDuff.Mode.MULTIPLY);
        d = mSeekBar.getProgressDrawable();
        if (d != null)
            d.setColorFilter(theme.getContrastColor(), PorterDuff.Mode.MULTIPLY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            d = mSeekBar.getThumb();
            d.setColorFilter(theme.getContrastColor(), PorterDuff.Mode.SRC_ATOP);
        }
    }


    private void onCancel() {
        mGun.fire(new SetEditingAction(false));
    }


    private void onRefresh() {
        mResizeView.resetDimens();
        mSeekBar.setProgress(srToProgress(DEFAULT));
    }


    private void onSave() {
        mResizeView.saveDimens();
        mGun.fire(new SetEditingAction(false));
    }


    private int srToProgress(float sr) {
        sr *= 10;
        return (int) (MAX - sr);
    }
}
