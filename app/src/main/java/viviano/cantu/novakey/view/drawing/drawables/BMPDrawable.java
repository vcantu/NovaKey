package viviano.cantu.novakey.view.drawing.drawables;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.view.drawing.Draw;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;

/**
 * Created by Viviano on 12/26/2015.
 */
public class BMPDrawable implements Drawable {

    Bitmap bmp;

    public BMPDrawable(Resources res, int id) {
        bmp = BitmapFactory.decodeResource(res, id);
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Draw.bitmap(bmp, x, y, size / bmp.getWidth(), p, canvas);
    }
}
