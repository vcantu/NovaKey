package viviano.cantu.novakey.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.Themeable;

/**
 * Created by Viviano on 6/9/2016.
 */
public abstract class NovaKeyView extends View implements Themeable {

    protected Model mModel;
    protected MasterTheme mTheme;

    public NovaKeyView(Context context) {
        this(context, null);
    }

    public NovaKeyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovaKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setModel(Model model) {
        mModel = model;
    }

    @Override
    public void setTheme(MasterTheme theme) {
        mTheme = theme;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mModel.getElements() == null)
            return;
        for (Element e : mModel.getElements()) {
            e.draw(mModel, mModel.getTheme(), canvas);
        }
    }

}
