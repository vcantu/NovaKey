package viviano.cantu.novakey.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import viviano.cantu.novakey.Font;

/**
 * Created by Viviano on 12/26/2015.
 */
public class MaterialIcon implements Icon.Drawable {

    private String name, code;

    MaterialIcon(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Typeface tempTTF = p.getTypeface();
        float tempSize = p.getTextSize();

        p.setTypeface(Font.MATERIAL_ICONS);
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
