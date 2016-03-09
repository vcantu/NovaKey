package viviano.cantu.novakey.drawing;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Viviano on 12/26/2015.
 */
public class BMPIcon implements Icons.Drawable {

    Bitmap bmp;

    BMPIcon(Resources res, int id) {
        bmp = BitmapFactory.decodeResource(res, id);
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Draw.bitmap(bmp, x, y, size / bmp.getWidth(), p, canvas);
    }
}
