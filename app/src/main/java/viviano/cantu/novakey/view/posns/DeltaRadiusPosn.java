package viviano.cantu.novakey.view.posns;

import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 9/10/2016.
 *
 * Takes in an angle and a distance(in pixels) and places
 * the coordinate at a the given distance from the radius + the radius
 */
public class DeltaRadiusPosn extends RelativePosn {

    private final float mDistance;
    private final double mAngle;

    public DeltaRadiusPosn(float distance, double angle) {
        mDistance = distance;
        mAngle = angle;
    }

    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    @Override
    public float getX(MainDimensions model) {
        return Util.xFromAngle(model.getX(), model.getRadius() + mDistance, mAngle);
    }

    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    @Override
    public float getY(MainDimensions model) {
        return Util.yFromAngle(model.getY(), model.getRadius() + mDistance, mAngle);
    }
}
