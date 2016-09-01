package viviano.cantu.novakey.elements.keyboards;

import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.view.drawing.Font;
import viviano.cantu.novakey.view.drawing.drawables.TextDrawable;
import viviano.cantu.novakey.view.posns.DeltaPosn;
import viviano.cantu.novakey.view.posns.RadiiPosn;
import viviano.cantu.novakey.view.posns.RelativePosn;
import viviano.cantu.novakey.view.posns.SmallRadiusPosn;

/**
 * Created by Viviano on 10/12/2015.
 */
public class Key {

    public final Character VALUE;
    private final int mGroup, mLoc;
    private final boolean mAltLayout;

    private RelativePosn mPosn;
    private float mSize = 1;
    private final TextDrawable mDrawable;

    public Key(Character c, int group, int loc) {
        this(c, group, loc, false);
    }

    public Key(Character c, int group, int loc, boolean altLayout) {
        VALUE = c;
        mGroup = group;
        mLoc = loc;
        mAltLayout = altLayout;
        mPosn = getDesiredPosn();
        mDrawable = new TextDrawable(VALUE.toString(), Font.SANS_SERIF_LIGHT);
    }

    /**
     * @return this key's character in uppercase
     */
    public Character getUppercase() {
        return Character.toUpperCase(VALUE);
    }

    /**
     * @return this key's character in lowercase
     */
    public Character getLowercase() {
        return Character.toLowerCase(VALUE);
    }

    /**
     * @return this key's relative position
     */
    public RelativePosn getPosn() {
        return mPosn;
    }

    /**
     * set this key's relative position to this
     * @param posn
     */
    public void setPosn(RelativePosn posn) {
        mPosn = posn;
    }

    /**
     * @return this key's size factor from 0-1
     * 0 being as small as possible, 1 being the default size
     * 2 being twice the default size. And so forth
     */
    public float getSize() {
        return mSize;
    }

    /**
     * sets this key's size factor
     * @param size from 0-1
     * 0 being as small as possible, 1 being the default size
     * 2 being twice the default size. And so forth
     */
    public void setSize(float size) {
        mSize = size;
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
                getLowercase().toString() : getUppercase().toString());

        return mDrawable;
    }


    private RelativePosn getDesiredPosn() {
        if (mGroup == 0) {
            if (mLoc == 0)
                return new DeltaPosn(0, 0);
            else {
                return new SmallRadiusPosn(2f / 3f, getAngle());
            }
        }
        else {
            if (mLoc == 0)
                return new RadiiPosn(1f / 6f, getAngle());
            if (mLoc == (mAltLayout ? 4 : 0))
                return new RadiiPosn(1f + 1f/6f, getAngle());
            return new RadiiPosn(.5f, getAngle());
        }
    }

    private double getAngle() {
        if (mGroup == 0)
            return angleAt(mLoc);
        double areaWidth = Math.PI / 5;
        if (mLoc == 3)
            return angleAt(mGroup) - 2 * areaWidth / 3;
        if (mLoc == 1)
            return angleAt(mGroup) + 2 * areaWidth / 3;
        return angleAt(mGroup);
    }

    private double angleAt(int i) {
        return ((i-1) * 2 * Math.PI) / 5 + Math.PI / 2 + Math.PI / 5;
    }

}
