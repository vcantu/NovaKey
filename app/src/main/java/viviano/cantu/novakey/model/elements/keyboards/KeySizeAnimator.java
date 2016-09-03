package viviano.cantu.novakey.model.elements.keyboards;

import viviano.cantu.novakey.animations.utils.Animator;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 9/2/2016.
 *
 * Will change the size of the key from the given factors
 */
public class KeySizeAnimator implements Animator<Key> {

    private final float mStart, mEnd;

    public KeySizeAnimator(float start, float end) {
        mStart = start;
        mEnd = end;
    }

    /**
     * Takes in a T and a fraction and updates the T according
     * to the fraction
     *
     * @param key      object to update
     * @param fraction percentage of animation where 0 is the start & 1 is the end
     */
    @Override
    public void update(Key key, float fraction) {
        key.setSize(Util.fromFrac(mStart, mEnd, fraction));
    }
}
