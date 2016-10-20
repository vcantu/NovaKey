package viviano.cantu.novakey.controller;

import android.content.Context;

import viviano.cantu.novakey.R;

/**
 * Created by vcantu on 9/18/16.
 */
public class BasicCorrections implements Corrections {

    String[] mContractions;

    /**
     * This is where all data should be loaded
     *
     * @param context used to load data saved on device
     */
    @Override
    public void initialize(Context context) {
        mContractions = context.getResources().getStringArray(R.array.contractions);
    }

    /**
     * TODO: this corrections method will change drastically this is just a place holder
     *
     * @param composing
     * @return
     */
    @Override
    public String correction(String composing) {//TODO: shift state
        int idx = contractionIndex(composing);
        if (idx != -1)
            return mContractions[idx];
        return composing;
    }

    private int contractionIndex(String text) {
        for (int i = 0; i < mContractions.length; i++) {
            String check = mContractions[i].replace("\'", "");
            if (check.equalsIgnoreCase(text.toString()))
                return i;
        }
        return -1;
    }
}
