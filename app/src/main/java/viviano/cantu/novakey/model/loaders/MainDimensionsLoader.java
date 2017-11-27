package viviano.cantu.novakey.model.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.model.MainDimensions;

/**
 * Created by viviano on 11/26/2017.
 */

public class MainDimensionsLoader implements Loader<MainDimensions> {

    //Shared preferences
    private final SharedPreferences mSharedPref;
    private final Context mContext;

    public MainDimensionsLoader(Context context) {
        this.mContext = context;
        this.mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private boolean isLandscape() {
        return mContext.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public MainDimensions load() {
        float r = mSharedPref.getFloat("size" + (isLandscape() ? "_land" : ""),
                mContext.getResources().getDimension(R.dimen.default_radius));
        float sr = r / mSharedPref.getFloat("smallRadius", 3);
        float p = mSharedPref.getFloat("padd" + (isLandscape() ? "_land" : ""),
                mContext.getResources().getDimension(R.dimen.default_padding));

        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        int h = (int)(mSharedPref.getFloat("y" + (isLandscape() ? "_land" : ""), r) + r + p);

        float x = mSharedPref.getFloat("x" + (isLandscape() ? "_land" : ""),w / 2);
        float y = mSharedPref.getFloat("y" + (isLandscape() ? "_land" : ""),r + p);

        return new MainDimensions(x, y, r, sr, p, w, h);
    }

    @Override
    public void save(MainDimensions md) {
        // W - //TODO: for now it's based on the width of the screen
        // H - //TODO: for now it's based on the radius and padding
        mSharedPref.edit()
                .putFloat("x" + (isLandscape() ? "_land" : ""), md.getX())
                .putFloat("y" + (isLandscape() ? "_land" : ""), md.getY())
                .putFloat("size" + (isLandscape() ? "_land" : ""), md.getRadius())
                .putFloat("smallRadius", md.getSmallRadius())
                .putFloat("padd" + (isLandscape() ? "_land" : ""), md.getPadding())
                .apply();
    }
}
