package viviano.cantu.novakey.drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import viviano.cantu.novakey.utils.Util;

public class Draw {

    /*
    Helper method - draws line with given information
     */
    public static void lines(float x, float y, float r, float sr, float gap, int color,
                             Paint p, Canvas canvas) {
        for (int i = 0; i < 5; i++)
        {
            double angle = (i * 2 * Math.PI) / 5 + Math.PI / 2;
            angle = (angle > Math.PI * 2 ? angle - Math.PI * 2 : angle);
            line(x, y, sr + gap, r - gap, angle, color, p, canvas);
        }
    }

    /*
    Draws shaded line with origin x, y at an angle from start to end
    */
    public static void line(float x, float y, float start, float end, double angle, int color,
                            Paint p, Canvas canvas) {
        p.setColor(color);
        canvas.drawLine(x + (float) Math.cos(angle) * start,
                y - (float) Math.sin(angle) * start,
                x + (float) Math.cos(angle) * end,
                y - (float) Math.sin(angle) * end, p);
    }

    /*
     Helper method - draws all shaded lines with given information
     */
    public static void shadedLines(float x, float y, float r, float sr,
                                   int color, Paint p, Canvas canvas) {
        float gap = (r - sr) / 10;// gap that will separate the line from the center circle
        for (int i = 0; i < 5; i++)
        {
            double angle = (i * 2 * Math.PI) / 5 + Math.PI / 2;
            angle = (angle > Math.PI * 2 ? angle - Math.PI * 2 : angle);
            shadedLine(x, y, sr + gap, r - gap, angle, color, p, canvas);
        }
    }

    /*
     Draws shaded line with origin x, y at an angle from start to end
     */
    public static void shadedLine(float x, float y, float start, float end, double angle,
                                  int color, Paint p, Canvas canvas) {
        p.setShader(new RadialGradient(x + (float) Math.cos(angle) * ((end - start) / 2 + start),
                y - (float) Math.sin(angle) * ((end - start) / 2 + start),
                (end - start) / 2,
                color, color & 0x00FFFFFF, Shader.TileMode.CLAMP));
        canvas.drawLine(x + (float) Math.cos(angle) * start,
                y - (float) Math.sin(angle) * start,
                x + (float) Math.cos(angle) * end,
                y - (float) Math.sin(angle) * end, p);
        p.setShader(null);
    }

    //Draw text with size
    public static void textFlat(String s, float x, float y, float size, Paint p, Canvas canvas) {
        float temp = p.getTextSize();
        p.setTextSize(size);
        textFlat(s, x, y, p, canvas);
        p.setTextSize(temp);
    }

    public static void textFlat(String s, float x, float y, Paint p, Canvas canvas) {
        canvas.drawText(s, x - p.measureText(s) / 2, y, p);
    }

    //Draws text centered
    public static void text(String s, float x, float y, Paint p, Canvas canvas) {
        String[] S = s.split("\n");//lines
        if (S.length <= 1) {//TODO: draw text containting emoji
            canvas.drawText(s, x - p.measureText(s) / 2, y - (p.ascent() + p.descent()) / 2, p);
        } else {
            float l = p.getTextSize() * (10 / 8);//line size
            for (int i=0; i<S.length; i++) {
                text(S[i], x, ((y - (S.length / 2 * l)) + i * l) - (S.length % 2 != 0 ? l/2 : 0) + l/2,
                        p, canvas);
            }
        }
    }

    //Draw text with size
   public static void text(String s, float x, float y, float size, Paint p, Canvas canvas) {
        float temp = p.getTextSize();
        p.setTextSize(size);
        text(s, x, y, p, canvas);
        p.setTextSize(temp);
    }

    public static void colorItem(int color, float x, float y, float radius, Paint p, Canvas canvas) {
        p.setColor(color);
        canvas.drawCircle(x, y, radius, p);
    }
    //draws white checkmark if selected
    public static void colorItem(int color, float x, float y, float radius, boolean selected,
                                 Paint p, Canvas canvas) {
        colorItem(color, x, y, radius, p, canvas);
        if (selected) {//draw checkmark
            float rw = Util.contrastRatio(Color.WHITE, color);
            Icon.Drawable ic = Icon.get("check");
            p.setColor(rw < 1.1f ? Color.BLACK : Color.WHITE);
            p.clearShadowLayer();
            Icon.draw(ic, x, y, radius * 1.6f, p, canvas);
        }
    }

    public static void floatingButton(float x, float y, float radius, Bitmap icon, int back, int front,
                                   float height, Paint p, Canvas canvas) {
        //Shadow
        p.setShader(new RadialGradient(x, y - height / 3, radius + height + 3,
                0xA0000000, 0x00000000, Shader.TileMode.CLAMP));
        canvas.drawPaint(p);
        p.setShader(null);

        //Circle
        p.setColor(back);
        canvas.drawCircle(x, y - height, radius, p);

        //Icon
        p.setColorFilter(new LightingColorFilter(front, 0));
        bitmap(icon, x, y - height, 1, p, canvas);
        p.setColorFilter(null);
    }

    //centers bitmap and scales it to scale
    public static void bitmap(Bitmap bmp, float x, float y, float scale, Paint p, Canvas canvas) {
        float width = bmp.getWidth() * scale;
        float height = bmp.getHeight() * scale;
        canvas.drawBitmap(bmp, null, new RectF(x - width / 2, y - height / 2, x + width / 2, y + height / 2), p);
    }
}
