package viviano.cantu.novakey;

/**
 * Created by Viviano on 10/7/2015.
 */
public class NovaKeyDimen {
    public float x, y, w, h, r, sr;
    public KeyLayout kl;
    //TODO: Btns

    public NovaKeyDimen() {

    }
    public NovaKeyDimen(float x, float y, float w, float h, float r, float sr, KeyLayout kl) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.sr = sr;
        this.kl = kl;
    }
}
