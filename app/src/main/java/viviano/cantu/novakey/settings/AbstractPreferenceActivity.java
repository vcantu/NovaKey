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

package viviano.cantu.novakey.settings;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import viviano.cantu.novakey.R;

public abstract class AbstractPreferenceActivity extends AppCompatActivity {

    private boolean mDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(
                getResources().getColor(R.color.colorAccent)));
        fab.setColorFilter(0xFFffffff);//icon color
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActivityClosed(true);
                mDone = true;
                finish();
            }
        });
    }

    /**
     * @return must return the layout ID any activity inheriting from this will want
     * to display
     */
    abstract int getLayoutId();

    /**
     * Will be called when the activity is closed
     * @param positiveResult true if the FAB is clicked to exit, false if activity was
     *                       exited another way
     */
    abstract void onActivityClosed(boolean positiveResult);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mDone)
            onActivityClosed(false);
    }

}
