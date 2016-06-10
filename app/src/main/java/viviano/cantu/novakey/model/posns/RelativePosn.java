package viviano.cantu.novakey.model.posns;

import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Created by Viviano on 6/10/2016.
 */
public abstract class RelativePosn {

    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    abstract float getX(NovaKeyModel model);

    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    abstract float getY(NovaKeyModel model);
}
