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

package viviano.cantu.novakey.core.model.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import viviano.cantu.novakey.core.R;
import viviano.cantu.novakey.core.model.MainDimensions;

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
        int h = (int) (mSharedPref.getFloat("height" + (isLandscape() ? "_land" : ""), r * 2 + p));

        float x = mSharedPref.getFloat("x" + (isLandscape() ? "_land" : ""), w / 2);
        //TODO: if legacy Y
        float y = mSharedPref.getFloat("y" + (isLandscape() ? "_land" : ""), r + p);

        return new MainDimensions(x, y, r, sr, p, w, h);
    }


    @Override
    public void save(MainDimensions md) {
        // W - //TODO: for now it's based on the width of the screen
        mSharedPref.edit()
                .putFloat("x" + (isLandscape() ? "_land" : ""), md.getX())
                .putFloat("y" + (isLandscape() ? "_land" : ""), md.getY())
                .putFloat("size" + (isLandscape() ? "_land" : ""), md.getRadius())
                .putFloat("smallRadius", md.getSmallRadius())
                .putFloat("padd" + (isLandscape() ? "_land" : ""), md.getPadding())
                .putFloat("height" + (isLandscape() ? "_land" : ""), md.getHeight())
                .apply();
    }
}
