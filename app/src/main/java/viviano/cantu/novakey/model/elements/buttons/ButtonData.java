package viviano.cantu.novakey.model.elements.buttons;

import viviano.cantu.novakey.view.drawing.shapes.Shape;
import viviano.cantu.novakey.view.posns.RelativePosn;

/**
 * Created by Viviano on 6/22/2016.
 */
public class ButtonData {

    private RelativePosn mPosn;
    private float mSize;
    private Shape mShape;

    /**
     * @return this properties' posn
     */
    public RelativePosn getPosn() {
        return mPosn;
    }

    /**
     * @param posn sets this posn to these properties
     */
    public ButtonData setPosn(RelativePosn posn) {
        mPosn = posn;
        return this;
    }

    /**
     * @return this properties' current size
     */
    public float getSize() {
        return mSize;
    }

    /**
     * @param size sets the size of these properties
     */
    public ButtonData setSize(float size) {
        mSize = size;
        return this;
    }

    /**
     * @param shape sets the shape of these properties
     */
    public ButtonData setShape(Shape shape) {
        mShape = shape;
        return this;
    }

    /**
     * @return the shape of these properties
     */
    public Shape getShape() {
        return mShape;
    }


}
