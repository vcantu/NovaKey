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

package viviano.cantu.novakey.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.Themeable;

/**
 * Created by Viviano on 6/9/2016.
 */
public abstract class NovaKeyView extends View implements Themeable {

    protected Model mModel;
    protected MasterTheme mTheme;


    public NovaKeyView(Context context) {
        this(context, null);
    }


    public NovaKeyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public NovaKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setModel(Model model) {
        mModel = model;
    }


    @Override
    public void setTheme(MasterTheme theme) {
        mTheme = theme;
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
