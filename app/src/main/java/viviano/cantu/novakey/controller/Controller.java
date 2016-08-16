package viviano.cantu.novakey.controller;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.elements.boards.Board;
import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.touch.HandlerManager;
import viviano.cantu.novakey.controller.touch.NovaKeyListener;
import viviano.cantu.novakey.elements.boards.MainBoard;
import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.model.TrueModel;
import viviano.cantu.novakey.model.MainDrawModel;
import viviano.cantu.novakey.model.NovaKeyModel;
import viviano.cantu.novakey.view.INovaKeyView;
import viviano.cantu.novakey.view.NovaKeyView;

/**
 * Created by Viviano on 7/10/2015.
 */
public class Controller implements Gun {

    private final NovaKey mIME;
    private final NovaKeyModel mTrueModel;
    private final DrawModel mDrawModel;
    private final INovaKeyView mView;
    private final NovaKeyListener mListener;
    private final List<Element> mElements;
    private final Board mMainBoard;

    /**
     * Controller initializes models and creates private references to
     * the given IME and View
     *
     * @param ime the input method service
     */
    public Controller(NovaKey ime) {
        //context
        mIME = ime;
        //view
        mView = new NovaKeyView(ime);
        //models
        mTrueModel = new TrueModel(mIME);
        mDrawModel = new MainDrawModel(mTrueModel);
        //handler
        mListener = new NovaKeyListener(this);
        //elements
        mElements = new ArrayList<>();
        mMainBoard = new MainBoard();
        mElements.add(mMainBoard);

        //initialize view
        mView.setStateModel(mTrueModel);
        mView.setDrawModel(mDrawModel);
        mView.setListener(mListener);
        mView.setElements(mElements);
    }

    /**
     * @return returns the main view
     */
    public INovaKeyView getView() {
        return mView;
    }

    /**
     * Updates the view
     */
    public void invalidate() {
        mView.update();
    }

    /**
     * @return returns the true model
     */
    public NovaKeyModel getTrueModel() {
        return mTrueModel;
    }

    /**
     * @return returns the draw model
     */
    public DrawModel getDrawModel() {
        return mDrawModel;
    }

    /**
     * @return returns this views elements
     */
    public List<Element> getElements() {
        return mElements;
    }

    /**
     * @return returns the main board, or null if there is none
     */
    public Board getMainBoard() {
        return mMainBoard;
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
        return action.trigger(mIME, this, mTrueModel);
    }
}