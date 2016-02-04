package viviano.cantu.novakey.settings;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import viviano.cantu.novakey.R;

public abstract class AbstractPreferenceActivity extends AppCompatActivity {

    private boolean done = false;

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
                done = true;
                finish();
            }
        });
    }

    abstract int getLayoutId();

    abstract void onActivityClosed(boolean positiveResult);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!done)
            onActivityClosed(false);
    }

}
