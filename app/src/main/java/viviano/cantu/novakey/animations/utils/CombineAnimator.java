package viviano.cantu.novakey.animations.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viviano on 9/2/2016.
 */
public class CombineAnimator<T> implements Animator<T> {

    private final List<Animator<T>> mList;

    public CombineAnimator(List<Animator<T>> list) {
        mList = list;
    }

    public CombineAnimator() {
        mList = new ArrayList<>();
    }

    CombineAnimator<T> add(Animator<T> animator) {
        mList.add(animator);
        return this;
    }

    /**
     * Takes in a T and a fraction and updates the T according
     * to the fraction
     *
     * @param t        object to update
     * @param fraction percentage of animation where 0 is the start & 1 is the end
     */
    @Override
    public void update(T t, float fraction) {
        for (Animator<T> a : mList) {
            a.update(t, fraction);
        }
    }
}
