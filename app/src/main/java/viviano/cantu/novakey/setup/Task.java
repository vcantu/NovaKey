package viviano.cantu.novakey.setup;

import android.graphics.Paint;
import android.widget.EditText;

import viviano.cantu.novakey.Util;

/**
 * Created by Viviano on 11/3/2015.
 */
public abstract class Task {

    private String mainText, hintText;

    Task(String main, String hint) {
        this.mainText = main;
        this.hintText = hint;
    }

    public String mainText() {
        return mainText;
    }

    public String hintText() {
        return hintText;
    }

    abstract void onTeach();

    abstract void onStart(EditText text);

    abstract boolean isComplete(String currText);

    void onEnd() {
        //Does nothing is only overriden if necessary
    }
}