package viviano.cantu.novakey.view.drawing;

import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 3/11/2016.
 */
public class ShadowDimens {

    public static ShadowDimens fromAngle(float degrees, float r) {
        float d = r * 2;
        double a = Math.toRadians(degrees);
        return new ShadowDimens(r, Util.xFromAngle(0, d, a), Util.yFromAngle(0, d, a));
    }

    public final float x, y, r;

    private ShadowDimens(float r, float x, float y) {
        this.r = r;
        this.x = x;
        this.y = y;
    }

    public ShadowDimens fromAngle(float degrees) {
        return fromAngle(degrees, this.r);
    }
}
