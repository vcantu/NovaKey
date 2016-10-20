package viviano.cantu.novakey.controller;

import android.content.Context;

/**
 * Created by vcantu on 9/18/16.
 */
public interface Corrections {

    /**
     * This is where all data should be loaded
     *
     * @param context used to load data saved on device
     */
    void initialize(Context context);

    /**
     * TODO: this corrections method will change drastically this is just a place holder
     * @return
     */
    String correction(String composing);

}
