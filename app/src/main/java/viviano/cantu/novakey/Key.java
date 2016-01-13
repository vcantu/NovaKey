package viviano.cantu.novakey;

import android.graphics.Canvas;
import android.graphics.Paint;

import viviano.cantu.novakey.drawing.Draw;

/**
 * Created by Viviano on 10/12/2015.
 */
public class Key {
    public Character c;
    public int group, loc;
    public float x, y, size = 1;

    public Key(Character c, int group, int loc) {
        this.c = c;
        this.group = group;
        this.loc = loc;
        //x & y
    }

    public void draw(Paint p, Canvas canvas) {
        Draw.text(c.toString(), x, y, p.getTextSize() * size, p, canvas);
    }

    //updates x and y to default value
    public void updateCoords(float centerX, float centerY, float r, float sr) {
        x = getDesiredX(centerX, r, sr);
        y = getDesiredY(centerY, r, sr);
    }

    public float getDesiredX(float centerX, float r, float sr) {
        return (float)(centerX + Math.cos(getAngle()) * getDistance(r, sr));
    }

    public float getDesiredY(float centerY, float r, float sr) {
        return (float)(centerY - Math.sin(getAngle()) * getDistance(r, sr));
    }


    public double getAngle() {
        if (group == 0)
            return angleAt(loc);
        double areaWidth = Math.PI / 5;
        if (loc == 3)
            return angleAt(group) - 2 * areaWidth / 3;
        if (loc == 1)
            return angleAt(group) + 2 * areaWidth / 3;
        return angleAt(group);
    }
    public float getDistance(float r, float sr) {
        if (group == 0) {
            if (loc == 0)
                return 0;
            return 2 * sr / 3;
        }
        float outerRadius = r - sr;
        if (loc == 2)
            return sr + outerRadius / 6;
        if (loc == 0)
            return r - outerRadius / 6;
        return sr + outerRadius / 2;
    }

    private double angleAt(int i) {
        return ((i-1) * 2 * Math.PI) / 5 + Math.PI / 2 + Math.PI / 5;
    }

    public void toUpperCase() {
        c = Character.toUpperCase(c);
    }

    public void toLowerCase() {
        c = Character.toLowerCase(c);
    }
}
