package viviano.cantu.novakey.model.elements.keyboards;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.input.Inputable;
import viviano.cantu.novakey.controller.input.KeyInput;
import viviano.cantu.novakey.model.elements.menus.InfiniteMenu;
import viviano.cantu.novakey.model.states.InputState;
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
public class Key implements Inputable {

    private final Character mChar;
    private final KeyInput mInput;//set just 1 to save memory during runtime
    public final int group, loc;
    private final boolean mAltLayout;

    private RelativePosn mPosn;
    private float mSize = 1;
    private final TextDrawable mDrawable;

    public Key(Character c, int group, int loc) {
        this(c, group, loc, false);
    }

    public Key(Character c, int group, int loc, boolean altLayout) {
        mChar = c;
        mInput = new KeyInput(mChar);
        this.group = group;
        this.loc = loc;
        mAltLayout = altLayout;
        mPosn = getDesiredPosn();
        mDrawable = new TextDrawable(mChar.toString(), Font.SANS_SERIF_LIGHT);
    }

    /**
     * @return this key's character in uppercase
     */
    public Character getUppercase() {
        return Character.toUpperCase(mChar);
    }

    /**
     * @return this key's character in lowercase
     */
    public Character getLowercase() {
        return Character.toLowerCase(mChar);
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

    /**
     * @return this key's character code
     */
    public char getChar() {
        return mChar;
    }

    private RelativePosn getDesiredPosn() {
        if (group == 0) {
            if (loc == 0)
                return new DeltaPosn(0, 0);
            else {
                return new SmallRadiusPosn(2f / 3f, getAngle());
            }
        }
        else {
            if (loc == 2)
                return new RadiiPosn(1f / 6f, getAngle());
            if (loc == (mAltLayout ? 0 : 4))
                return new RadiiPosn(1f + 1f/6f, getAngle());
            if (loc == 0)
                return new RadiiPosn(1f - 1f/6f, getAngle());
            return new RadiiPosn(.5f, getAngle());
        }
    }

    private double getAngle() {
        if (group == 0)
            return angleAt(loc);
        double areaWidth = Math.PI / 5;
        if (loc == 3)
            return angleAt(group) - 2 * areaWidth / 3;
        if (loc == 1)
            return angleAt(group) + 2 * areaWidth / 3;
        return angleAt(group);
    }

    private double angleAt(int i) {
        return ((i-1) * 2 * Math.PI) / 5 + Math.PI / 2 + Math.PI / 5;
    }

    /**
     * Returns the hidden keys menu based on this key
     *
     * @return an infinite menu or null if none was found
     */
    public InfiniteMenu getHiddenKeys() {
        return InfiniteMenu.getHiddenKeys(mChar);
    }


    /**
     * Inputs this object
     *
     * @param state      state for context
     * @param shiftState shift state for context
     * @return a side effect to this input action
     */
    @Override
    public Action input(InputState state, ShiftState shiftState) {
        return mInput.input(state, shiftState);
    }
}
