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
