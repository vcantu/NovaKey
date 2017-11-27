package viviano.cantu.novakey.view.posns;

import viviano.cantu.novakey.model.MainDimensions;

/**
 * Created by Viviano on 6/10/2016.
 */
public abstract class RelativePosn {

    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    public abstract float getX(MainDimensions model);

    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    public abstract float getY(MainDimensions model);
}
