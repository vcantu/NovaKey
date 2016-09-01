package viviano.cantu.novakey.model.loaders;

import android.os.AsyncTask;

/**
 * Created by Viviano on 8/26/2016.
 */
public abstract class AsyncLoader<T> {

    private final LoadListener<T> mListener;
    private final Loader<T> mLoader;

    public AsyncLoader(LoadListener<T> listener, Loader<T> loader) {
        mListener = listener;
        mLoader = loader;
    }

    /**
     * Loads the element from where ever this interface saved it from
     * asynchronously
     * @return a new loaded object
     */
    public void load() {
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                return mLoader.load();
            }

            @Override
            protected void onPostExecute(T t) {
                super.onPostExecute(t);
                mListener.onLoad(t);
            }
        }.execute();
    }

    /**
     * Saves the element to be loaded later.
     * May execute asynchronously
     * @param t object to be saved
     */
    public abstract void save(T t);


    public interface LoadListener<T> {
        /**
         * Called when the loader has finished loading the object
         * @param t loaded object
         */
        void onLoad(T t);
    }
}
