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

package viviano.cantu.novakey.widgets;

import android.content.Context;
import android.util.AttributeSet;

import viviano.cantu.novakey.core.model.MainDimensions;
import viviano.cantu.novakey.core.view.NovaKeyView;

/**
 * Created by vcantu on 10/2/16.
 */
public class NovaKeyPreview extends NovaKeyView {

    public NovaKeyPreview(Context context) {
        super(context);
    }


    public NovaKeyPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float w = MeasureSpec.getSize(widthMeasureSpec);
        float h = MeasureSpec.getSize(heightMeasureSpec);
        float r = Math.min(h - getPaddingTop() - getPaddingBottom(),
                w - getPaddingRight() - getPaddingLeft());
        r /= 2;
        float x = w / 2;
        float y = getPaddingTop() + r;
        float sr = r / 3;

        MainDimensions dimens = mModel.getMainDimensions();
        dimens.setWidth((int) w);
        dimens.setHeight((int) h);
        dimens.setRadius(r);
        dimens.setSmallRadius(sr);
        dimens.setPadding(getPaddingTop());
        dimens.setX(x);
        dimens.setY(y);
    }
}
