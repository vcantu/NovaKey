package viviano.cantu.novakey.drawing;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Viviano on 12/26/2015.
 */
public class SVGIcon implements Icon.Drawable {

    private int color = 0xFFFFFFFF;
    private String stream = "", name;
    private SVG svg;

    SVGIcon(String name, InputStream stream) {
        this.name = name;
        try {
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            this.stream = new String(buffer);
        } catch (IOException e) {
            // Error handling
        }
    }

    SVGIcon(String name, Resources res, int id) {
        this(name, res.openRawResource(id));
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        int color = p.getColor();
        try {
//            if (this.color != color)
//                recolor(color);
            float half = size / 2;
            //canvas.drawPicture(getPicture(), new RectF(x - half, y - half, x + half, y + half));
        } catch (Exception e) {}
    }

    private void recolor(int color) {
        svg = new SVGBuilder().readFromString(this.stream)
                .setColorFilter(new LightingColorFilter(color, 0))
                .build();
        this.color = color;
    }

    private Picture getPicture() {
        if (svg == null)
            recolor(color);
        return svg.getPicture();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  SVGIcon)
            return name.equals(((SVGIcon)o).name);
        if (o instanceof String)
            return name.equalsIgnoreCase(((String)o));
        return super.equals(o);
    }
}
