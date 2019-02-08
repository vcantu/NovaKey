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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import viviano.cantu.novakey.NovaKey;

public class SetupView extends View {

    private Paint p = new Paint();
    private int backgroundColor = 0xFF626262, lineColor = 0xFFF0F0F0, doneColor = 0xFF909090;
    private float screenWidth, screenHeight;
    private int deviceDensity;

    private int progress;


    public SetupView(Context context) {
        super(context);
        final Activity parent = (Activity) context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        deviceDensity = metrics.densityDpi;

        SharedPreferences pref = parent.getApplicationContext().getSharedPreferences(NovaKey.MY_PREFERENCES, parent.MODE_PRIVATE);
        final Editor editor = pref.edit();
        editor.commit();
        progress = pref.getInt("progress", 0);

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (progress == 0) {
                            progress = 1;
                            editor.putInt("progress", 1);
                            editor.commit();
                            invalidate();
                            parent.startActivity(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS));
                        } else if (progress == 1) {
                            progress = 2;
                            editor.putInt("progress", 2);
                            editor.commit();
                            invalidate();
                            InputMethodManager im = (InputMethodManager) parent.getSystemService(parent.INPUT_METHOD_SERVICE);
                            im.showInputMethodPicker();
                        } else if (progress == 2) {
                            editor.putBoolean("has_setup", true);
                            editor.commit();
                            invalidate();
                            parent.startActivity(new Intent(parent, TutorialActivity.class));
                        }
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        p.setStrokeWidth(4);
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        float a = screenWidth / 4;

        int dp = 50 * (deviceDensity / 160);
        drawShadedLine(screenWidth / 2, screenHeight / 2 + dp, -a, a, 0, p, canvas);
        drawShadedLine(screenWidth / 2, screenHeight / 2 - dp, -a, a, 0, p, canvas);

        p.setColor(lineColor);
        p.setTextSize(dp / 1.6f);
        drawText("Welcome to NovaKey!", screenWidth / 2, dp * 1.5f, p, canvas);

        p.setTextSize(dp / 2);
        if (progress > 0)
            p.setColor(doneColor);
        else
            p.setColor(lineColor);
        drawText("1. Activate NovaKey", screenWidth / 2, screenHeight / 2 - dp * 2, p, canvas);
        if (progress > 1)
            p.setColor(doneColor);
        else
            p.setColor(lineColor);
        drawText("2. Make NovaKey primary", screenWidth / 2, screenHeight / 2, p, canvas);
        p.setColor(lineColor);
        drawText("3. Learn to use", screenWidth / 2, screenHeight / 2 + dp * 2, p, canvas);
    }


    private void drawShadedLine(float x, float y, float start, float end, double angle, Paint p, Canvas canvas) {
        p.setShader(new RadialGradient(x + (float) Math.cos(angle) * ((end - start) / 2 + start),
                y - (float) Math.sin(angle) * ((end - start) / 2 + start),
                (end - start) / 2, lineColor, backgroundColor, Shader.TileMode.CLAMP));
        canvas.drawLine(x + (float) Math.cos(angle) * start,
                y - (float) Math.sin(angle) * start,
                x + (float) Math.cos(angle) * end,
                y - (float) Math.sin(angle) * end, p);
        p.setShader(null);
    }


    //Draws text1 centered
    private void drawText(String s, float x, float y, Paint p, Canvas canvas) {
        canvas.drawText(s, x - p.measureText(s) / 2, y - (p.ascent() + p.descent()) / 2, p);
    }

}
