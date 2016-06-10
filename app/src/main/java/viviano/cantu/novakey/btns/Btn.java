package viviano.cantu.novakey.btns;

import android.graphics.Canvas;

import java.util.ArrayList;

import viviano.cantu.novakey.NovaKeyDimen;
import viviano.cantu.novakey.drawing.Icons;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.settings.Settings;

/**
 * Created by Viviano on 6/22/2015.
 */
public class Btn {

    //form and location
    public float dist;
    public double angle;
    public int shape;

    //looks and functionality
    public Icons.Drawable icon = null;
    public String text = null;

    public Btn(double angle, float dist, int shape) {
        this.angle = angle;
        this.dist = dist;
        this.shape = shape;
    }

    public boolean onBtn(float x, float y, float cx, float cy, float r) {
        switch (shape & SHAPE) {
            case CIRCLE:
                //get x & y
                float bx = (float)Math.cos(angle) * dist * r + cx;
                float by = (float)Math.sin(angle) * dist * r + cy;
                return Util.distance(x, y, bx, by) <= getRadius(shape, r);
            case ARC:
                double theta = getArcTheta(shape & SIZE)/2,
                        w = arcWidth(1),
                        fa = Util.angle(cx, cy, x, y),
                        fd = Util.distance(x, y, cx, cy)/r;
                return fa >= angle-theta && fa < angle+theta &&
                        fd >= dist-w && fd < dist+w;
        }
        return false;
    }

    public void draw(float x, float y, float r, BtnTheme theme, Canvas canvas) {
        float bx = (float)Math.cos(angle) * dist * r + x,
              by = (float)Math.sin(angle) * dist * r + y;
        theme.draw(icon == null ? text : icon, bx, by, shape, null, canvas);
    }

    public static float getRadius(int size, float r) {
        switch (size) {
            default:
            case SMALL:
                return (float)(Math.sqrt(2 * r * r) - r) / ((float)Math.sqrt(2) + 1);
            case MEDIUM:
                return getRadius(CIRCLE|SMALL, r) * 1.5f;
            case LARGE:
                return getRadius(CIRCLE|SMALL, r) * 2f;
        }
    }

    private static double getArcTheta(int size) {
        switch (size) {
            default:
            case SMALL:
                return 2*Math.PI / 5 / 3;
            case MEDIUM:
                return getArcTheta(ARC | SMALL) * 2;
            case LARGE:
                return getArcTheta(ARC|SMALL) * 3;
        }
    }
    public static float arcWidth(float r) {
        return r / 3;
    }

    public final static int SHAPE = 0xf, CIRCLE = 0x1, ARC = 0x2;
    public final static int SIZE = 0xf0, SMALL = 0x10, MEDIUM = 0x20, LARGE = 0x30;

    public static int getShape(int i) {
        //programatically orders them like shape1(smallest-largest)..shape2("").....
        return (i/3+1)|((i%3+1)*0x10);
    }

    //override to add funcitonality
    //TODO: make on up and down animations
    protected boolean isDown = false;
    public void onDown() {
        isDown = true;
    }
    public void onUp() {
        if (isDown) {
            onClick();
        }
        isDown = false;
    }

    public void onClick() {}

    public interface Clickable {
        void onClicked();
    }
    public interface LongPressable {
        void onLongPress();
    }
    public interface Rotatable {
        void onRotate(boolean clockwise);
    }
    public interface InfiniteMenuable {
        //or some kind of array
        Object[] getInfiniteMenu();
        void onItemSelected(Object selected);
    }
    public interface OnUpMenu {
        Object[] getOnUpMenu();
        void onItemSelected(Object selected);
    }

    /*
        Btns will be saved as a Single String, each button will contain it's data and meta data
        inside a substring of this main string, all buttons are to be separated by "|"
     */
    public static ArrayList<Btn> btnsFromString(String s) {
        if (s.equals(Settings.DEFAULT)) {
            return btnsFromString(
                    "0," + Math.PI*3/4 + "," + (1+getRadius(SMALL, 1)) + "," + (CIRCLE|SMALL) + "|" +
                    "1," + Math.PI*1/4 + "," + (1+getRadius(SMALL, 1)) + "," + (CIRCLE|SMALL)


            //this is space button
            + (Settings.hasSpaceBar ?
                    "|" + "2," + Math.PI/2 + "," + 1.16667f + "," + (ARC|LARGE)
                    : "")

            );
        }
        String[] b = s.split("[|]");
        ArrayList<Btn> B = new ArrayList<Btn>();
        for (String str : b) {
            B.add(btnFromString(str));
        }
        return B;
    }

    /*
        Each value of the btn String will be separated by a ","
        All strings are to be formatted like so:
        [int > instanceID],[double > angle],[float > dist],[int > shape],[meta-data...]

        [meta-data...] will depend on the instanceID
     */
    public static Btn btnFromString(String s) {
        String[] data = s.split(",");
        int id = Integer.valueOf(data[0]);
        double angle = Double.valueOf(data[1]);
        float dist = Float.valueOf(data[2]);
        int shape = Integer.valueOf(data[3]);

        switch (id) {
            default:
            case 0:
                return new BtnToggleModeChange(angle, dist, shape);
            case 1:
                return new BtnPunctuation(angle, dist, shape);
            case 2:
                return new BtnInsertText(angle, dist, shape);
        }
    }

    public static String stringFromBtn(Btn btn) {
        String res = "";
        res += getInstanceId(btn) + "," + btn.angle + "," + btn.dist + "," + btn.shape;
        //TODO: metadata
        return res;
    }

    private static int getInstanceId(Btn btn) {
        if (btn instanceof BtnToggleModeChange)
            return 0;
        else if (btn instanceof BtnPunctuation)
            return 1;
        return -1;
    }
}
