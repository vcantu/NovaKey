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

package viviano.cantu.novakey.setup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 10/22/2015.
 */
public class IconView extends View implements View.OnTouchListener {

    private boolean touched = false;
    private Drawable icon;
    private float size = 1.0f;
    private Paint p;
    private int mColor = 0xFFA0A0A0;

    private OnClickListener listener;


    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setOnTouchListener(this);
        p = new Paint();
        p.setAntiAlias(true);
    }


    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


    public void setSize(float size) {
        this.size = size;
    }


    public void setColor(int color) {
        mColor = color;
    }


    @Override
    public void onDraw(Canvas canvas) {
        float w = getWidth(), h = getHeight();
        p.setColor(touched ? mColor : mColor);//TODO: make color lighter
        icon.draw(w / 2, h / 2, w * size, p, canvas);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touched = true;
                if (listener != null)
                    listener.onClick();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                invalidate();
                break;
        }
        return true;
    }


    public void setClickListener(OnClickListener listener) {
        this.listener = listener;
    }


    public interface OnClickListener {
        void onClick();
    }
}
