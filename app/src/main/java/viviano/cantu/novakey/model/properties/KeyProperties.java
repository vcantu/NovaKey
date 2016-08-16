package viviano.cantu.novakey.model.properties;

import viviano.cantu.novakey.model.keyboards.Key;
import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.view.drawing.Font;
import viviano.cantu.novakey.view.drawing.drawables.TextDrawable;
import viviano.cantu.novakey.view.posns.RelativePosn;

/**
 * Created by Viviano on 6/10/2016.
 */
public class KeyProperties implements Properties {

    private final Key mKey;

    private RelativePosn mPosn;
    private float mSize = 1;
    private TextDrawable mDrawable;

    public KeyProperties(Key key, RelativePosn posn) {
        mKey = key;
        mPosn = posn;
        mDrawable = new TextDrawable(mKey.getLowercase().toString(), Font.SANS_SERIF_LIGHT);
    }

    /**
     * returns this key properties drawable based on the shift state
     *
     * @param shiftState used to determine text and font of drawable
     * @return a TextDrawable representing this Key
     */
    public TextDrawable getDrawable(ShiftState shiftState) {
        mDrawable.setFont(shiftState == ShiftState.CAPS_LOCKED ?
        Font.SANS_SERIF_CONDENSED : Font.SANS_SERIF_LIGHT);

        mDrawable.setText(shiftState == ShiftState.LOWERCASE ?
        mKey.getLowercase().toString() : mKey.getUppercase().toString());

        return mDrawable;
    }

    public RelativePosn getPosn() {
        return mPosn;
    }

    public void setPosn(RelativePosn posn) {
        mPosn = posn;
    }

    public float getSize() {
        return mSize;
    }

    public void setSize(float size) {
        mSize = size;
    }
}
