package viviano.cantu.novakey.model.loaders;

/**
 * Created by Viviano on 8/13/2016.
 *
 * Saves and loads data into shared preferences
 */
public interface Loader<T> {

    /**
     * Loads the element from where ever this interface saved it from
     * @return a new loaded object
     */
    T load();

    /**
     * Saves the element to be loaded later
     * @param t object to be saved
     */
    void save(T t);
}
