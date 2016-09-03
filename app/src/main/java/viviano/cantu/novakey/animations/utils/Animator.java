package viviano.cantu.novakey.animations.utils;

/**
 * Created by Viviano on 9/2/2016.
 */
public interface Animator<T> {

    /**
     * Takes in a T and a fraction and updates the T according
     * to the fraction
     *
     * @param t object to update
     * @param fraction percentage of animation where 0 is the start & 1 is the end
     */
    void update(T t, float fraction);
}
