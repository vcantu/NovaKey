package viviano.cantu.novakey.model.posns;

import viviano.cantu.novakey.model.NovaKeyModel;
import viviano.cantu.novakey.utils.Util;

/**
 * RelativePosn in which the angle is exactly the angle given
 * and the distance is the product between the distance given and the
 * outer radius of the keyboard
 *
 * Created by Viviano on 6/10/2016.
 */
public class RadiusPosn extends RelativePosn {
    private float distance;
    private double angle;

    public RadiusPosn(float distance, double angle) {
        this.distance = distance;
        this.angle = angle;
    }

    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    @Override
    float getX(NovaKeyModel model) {
        return Util.xFromAngle(model.getX(), model.getRadius() * distance, angle);
    }

    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    @Override
    float getY(NovaKeyModel model) {
        return Util.yFromAngle(model.getY(), model.getRadius() * distance, angle);
    }
}
