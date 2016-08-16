package viviano.cantu.novakey.view.posns;

import viviano.cantu.novakey.model.DrawModel;

/**
 * RelativePosn that returns coordinates translated
 * based on a given deltaX and deltaY(in pixels)
 *
 * Created by Viviano on 6/10/2016.
 */
public class DeltaPosn extends RelativePosn {

    private float dx, dy;

    public DeltaPosn(float deltaX, float deltaY) {
        this.dx = deltaX;
        this.dy = deltaY;
    }

    /**
     * @param model model to base posn off
     * @return x coordinate based on the model dimensions
     */
    @Override
    public float getX(DrawModel model) {
        return model.getX() + dx;
    }

    /**
     * @param model model to base posn off
     * @return y coordinate based on the model dimensions
     */
    @Override
    public float getY(DrawModel model) {
        return model.getY() + dy;
    }
}
