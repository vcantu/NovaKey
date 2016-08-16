package viviano.cantu.novakey.controller;

import viviano.cantu.novakey.view.animations.Animator;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Created by Viviano on 6/11/2016.
 */
public abstract class Transition {

    private NovaKeyModel mTrueModel;
    private DrawModel mDrawModel;

    public Transition(DrawModel drawModel, NovaKeyModel trueModel) {
        this.mDrawModel = drawModel;
        this.mTrueModel = trueModel;
    }

    private void sync() {
        mDrawModel.sync(mTrueModel);
    }
    /**
     * syncs the models begins the intro animation
     * then triggers the action and syncs
     * and finally begins the outro animation and ends
     */
    public void start() {
        sync();
        getIntro().setOnEndListener(() -> {
            getAction().trigger(, , );
            sync();
            getOutro().setOnEndListener(() -> {
                sync();
            }).start();
        }).start();
    }

    /**
     * @return the animation which begins the change
     */
    protected abstract Animator getIntro();

    /**
     * @return the animation whcih ends the change
     */
    protected abstract Animator getOutro();

    /**
     * @return Action that this transition is animating
     */
    protected abstract Action getAction();

}
