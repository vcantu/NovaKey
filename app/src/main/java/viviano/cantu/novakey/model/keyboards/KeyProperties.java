package viviano.cantu.novakey.model.keyboards;

import viviano.cantu.novakey.model.posns.RelativePosn;

/**
 * Created by Viviano on 6/10/2016.
 */
public class KeyProperties {
    public final Key key;

    public RelativePosn posn;
    public float size = 1;
    public float color = 0;

    public KeyProperties(Key key) {
        this.key = key;
        this.posn = key.getDesiredPosn();
    }
}
