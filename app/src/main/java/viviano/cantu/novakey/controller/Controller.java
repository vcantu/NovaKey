package viviano.cantu.novakey.controller;

import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetEditingAction;
import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.model.MainModel;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.utils.CustomTimer;
import viviano.cantu.novakey.view.NovaKeyView;
import viviano.cantu.novakey.view.MainView;

/**
 * Created by Viviano on 7/10/2015.
 */
public class Controller implements Gun, View.OnTouchListener {

    //main stuff
    private final NovaKey mIME;
    private final Model mModel;
    private final NovaKeyView mView;

    //touch
    private TouchHandler mHandler;//current handler
    private CustomTimer mDoublePress;

    /**
     * Controller initializes models and creates private references to
     * the given IME and View
     *
     * @param ime the input method service
     */
    public Controller(NovaKey ime) {
        // context
        mIME = ime;
        // model
        mModel = new MainModel(mIME);
        // view
        mView = new MainView(ime);
        mView.setModel(mModel);
        mView.setOnTouchListener(this);
        mView.setTheme(mModel.getTheme());

        // touch
        mDoublePress = new CustomTimer(1000, () -> fire(new SetEditingAction(true)));
    }

    /**
     * @return returns the main view
     */
    public NovaKeyView getView() {
        return mView;
    }

    /**
     * @return returns the draw model
     */
    public Model getModel() {
        return mModel;
    }

    /**
     * Triggers action
     *
     * @param action action to fire
     * @return returns the result of the action
     */
    @Override
    public <T> T fire(Action<T> action) {
        if (action != null)
            return action.trigger(mIME, this, mModel);
        return null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //for multitouch
            case MotionEvent.ACTION_POINTER_DOWN:
                mDoublePress.begin();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mDoublePress.cancel();
                break;
            case MotionEvent.ACTION_UP:
                mDoublePress.cancel();
                break;
        }

        //if has a handler handle event
        if (mHandler != null) {
            boolean result = mHandler.handle(event, this);
            if (!result)
                mHandler = null;
        }
        else {
            //instantiate new handlers until one returns true
            List<Element> elems = mModel.getElements();
            for (int i = elems.size() - 1; i >= 0; i--) {
                TouchHandler handler = elems.get(i);
                boolean res = handler.handle(event, this);
                if (res) {
                    mHandler = handler;
                    break;
                }
            }
        }
        return true;//take in all events
    }
}