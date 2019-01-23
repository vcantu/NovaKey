/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

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
