package viviano.cantu.novakey;

import android.content.SharedPreferences;

/**
 * Created by Viviano on 10/7/2015.
 */
public class NovaKeyDimen {
    public float x, y, w, h, r, sr;
    public KeyLayout kl;
    //TODO: Btns

    /**
     * Build default dimensions
     */
    public NovaKeyDimen() {

    }

    /**
     * Build custom
     *
     * @param x center X
     * @param y center Y
     * @param w width of keyboard
     * @param h height of keyboard
     * @param r radius of keyboard
     * @param sr small radius of keyboard
     * @param kl KeyLayout
     */
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
