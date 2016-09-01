package viviano.cantu.novakey.controller;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.touch.HandlerManager;
import viviano.cantu.novakey.controller.touch.NovaKeyListener;
import viviano.cantu.novakey.model.MainModel;
import viviano.cantu.novakey.model.TrueModel;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.NovaKeyView;
import viviano.cantu.novakey.view.MainView;

/**
 * Created by Viviano on 7/10/2015.
 */
public class Controller implements Gun {

    private final NovaKey mIME;
    private final Model mModel;
    private final NovaKeyView mView;
    private final NovaKeyListener mListener;

    /**
     * Controller initializes models and creates private references to
     * the given IME and View
     *
     * @param ime the input method service
     */
    public Controller(NovaKey ime) {
        //context
        mIME = ime;
        //models
        mModel = new MainModel(mIME);
        //view
        mView = new MainView(ime);
        //handler
        mListener = new NovaKeyListener(this);

        //initialize
        mView.setModel(mModel);
        mView.setOnTouchListener(mListener);
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
     * @return this controller's handler manager
     */
    public HandlerManager getManager() {
        return mListener;
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
}