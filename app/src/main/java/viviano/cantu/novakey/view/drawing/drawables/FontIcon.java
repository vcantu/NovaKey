package viviano.cantu.novakey.view.drawing.drawables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import viviano.cantu.novakey.view.drawing.Draw;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 12/26/2015.
 */
public class FontIcon implements Drawable {

    private String name, code;
    private Typeface font;

    public FontIcon(String name, String code, Typeface font) {
        this.name = name;
        this.code = code;
        this.font = font;
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Typeface tempTTF = p.getTypeface();
        float tempSize = p.getTextSize();

        p.setTypeface(font);
        p.setTextSize(size);
        Draw.text(code, x, y, p, canvas);

        p.setTypeface(tempTTF);
        p.setTextSize(tempSize);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof String) {
            String str = (String)o;
            return name.equalsIgnoreCase(str) || code.equalsIgnoreCase(str);
        }
        return super.equals(o);
    }
}
