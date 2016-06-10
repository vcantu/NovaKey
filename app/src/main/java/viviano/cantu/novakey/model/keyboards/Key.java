package viviano.cantu.novakey.model.keyboards;

import viviano.cantu.novakey.model.posns.DeltaPosn;
import viviano.cantu.novakey.model.posns.RadiiPosn;
import viviano.cantu.novakey.model.posns.RelativePosn;
import viviano.cantu.novakey.model.posns.SmallRadiusPosn;

/**
 * Created by Viviano on 10/12/2015.
 */
public class Key {

    private final Character c;
    private final int group, loc;
    private final boolean mAltLayout;
    public final KeyProperties properties;

    public Key(Character c, int group, int loc) {
        this(c, group, loc, false);
    }

    public Key(Character c, int group, int loc, boolean altLayout) {
        this.c = c;
        this.group = group;
        this.loc = loc;
        this.mAltLayout = altLayout;
        this.properties = new KeyProperties(this);
    }

    /**
     * @return this key's character in uppercase
     */
    public Character getUppercase() {
        return Character.toUpperCase(c);
    }

    /**
     * @return this key's character in lowercase
     */
    public Character getLowercase() {
        return Character.toLowerCase(c);
    }


    /**
     * @return this key's desired relative position
     */
    public RelativePosn getDesiredPosn() {
        if (group == 0) {
            if (loc == 0)
                return new DeltaPosn(0, 0);
            else {
                return new SmallRadiusPosn(2f / 3f, getAngle());
            }
        }
        else {
            if (loc == 0)
                return new RadiiPosn(1f / 6f, getAngle());
            if (loc == (mAltLayout ? 4 : 0))
                return new RadiiPosn(1f + 1f/6f, getAngle());
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


}
