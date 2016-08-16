package viviano.cantu.novakey.model.properties;

import viviano.cantu.novakey.elements.buttons.Button;
import viviano.cantu.novakey.view.animations.Animation;
import viviano.cantu.novakey.view.drawing.shapes.Shape;
import viviano.cantu.novakey.view.posns.RelativePosn;

/**
 * Created by Viviano on 6/22/2016.
 */
public class ButtonProperties implements Properties {

    private final Button mButton;

    private RelativePosn mPosn;
    private float mSize;
    private Shape mShape;

    public ButtonProperties(Button button) {
        mButton = button;
    }

    /**
     * @return this properties' posn
     */
    public RelativePosn getPosn() {
        return mPosn;
    }

    /**
     * @param posn sets this posn to these properties
     */
    public void setPosn(RelativePosn posn) {
        mPosn = posn;
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
    public void setSize(float size) {
        mSize = size;
    }

    /**
     * @param shape sets the shape of these properties
     */
    public void setShape(Shape shape) {
        mShape = shape;
    }

    /**
     * @return the shape of these properties
     */
    public Shape getShape() {
        return mShape;
    }


}
