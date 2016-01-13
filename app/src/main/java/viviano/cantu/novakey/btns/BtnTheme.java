package viviano.cantu.novakey.btns;

import android.graphics.Paint;

import viviano.cantu.novakey.Font;

/**
 * Created by Viviano on 7/9/2015.
 */
public class BtnTheme {

    //NOT ALL OF THESE DRAW FEATURES ARE IMPLEMENTED YET
    public static final int
            BACK_OUTLINE = 1,
            FRONT_OUTLINE = 2,//Not available yet
            OWN_COLOR = 4,
            NO_BACK = 8,
            BACK_SHADOW = 16,//Not available yet--vvv
            BACK_SHADOW_FLAT = 32,
            FRONT_SHADOW = 64,
            FRONT_SHADOW_FLAT = 128;

    public int backColor, frontColor, style;
    public Paint p = new Paint();

    public BtnTheme(int style, int back, int front) {
        this.style = style;
        backColor = back;
        frontColor = front;
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        p.setTypeface(Font.SANS_SERIF_LIGHT);
    }

    public boolean hasAttrbute(int attr) {
        return (style & attr) == attr;
    }

}
