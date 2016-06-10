package viviano.cantu.novakey.menus;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.drawing.Icons;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.themes.BaseTheme;

/**
 * Created by Viviano on 7/15/2015.
 */
public class OnUpMenu {

    private Object[] list;
    public Action action;
    public float fingerX, fingerY;

    public OnUpMenu(Object[] list, Action action) {
        this.list = list;
        this.action = action;
    }

    public void updateCoords(float x, float y) {
        fingerX = x;
        fingerY = y;
    }



    public void draw(float x, float y, float r, float sr, BaseTheme theme, Canvas canvas) {
        float dist = (r - sr) / 2 + sr;
        double a = Math.PI / 2 - Math.PI * 2 / 5 / 2;
        for (int j=0; j<list.length; j++) {
            a += Math.PI * 2 / 5;
            draw(j, x + (float) Math.cos(a) * dist, y - (float) Math.sin(a) * dist,
            sr, dist, theme.textColors()[j+1], theme.pT, canvas);
        }
    }

    private void draw(int i, float x, float y, float size, float dist, int color, Paint p, Canvas canvas) {
        size *= Math.pow(dist / Util.distance(x, y, fingerX, fingerY), 1.0/3);
        size = size >= dist*5/6 ? dist*5/6 : size;

        Object o = list[i];
        p.setColor(color);
        if (o == null)
            return;
        if (o instanceof Icons.Drawable)
            Icons.draw((Icons.Drawable) o, x, y, size, p, canvas);
        else {
            String s = "";
            if (o instanceof Character)
                s = Character.toString((Character)o);
            else
                try {
                    s = s.toString();
                } catch (Exception e) {}
            Draw.text(s, x, y, size, p, canvas);
        }
    }

    public void setAction(Action action) {
        this.action = action;
    }
    public interface Action {
        void onUp(int selected);
        void onLongPress(int selected);
    }
}
