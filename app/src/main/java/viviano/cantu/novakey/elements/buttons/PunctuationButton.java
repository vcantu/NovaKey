package viviano.cantu.novakey.elements.buttons;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.actions.typing.InputAction;
import viviano.cantu.novakey.elements.menus.InfiniteMenu;
import viviano.cantu.novakey.view.drawing.drawables.FlatTextDrawable;

/**
 * Created by Viviano on 7/9/2015.
 */
public class PunctuationButton extends Button {

    public PunctuationButton(ButtonData data) {
        super(data);
        setIcon(new FlatTextDrawable("."));
    }

    /**
     * @return action to fire, or null if no action is needed
     */
    @Override
    protected Action onClickAction() {
        return new InputAction('.');
    }

    /**
     * @return action to fire, or null if no action is needed
     */
    @Override
    protected Action onLongPressAction() {
        return new SetOverlayAction(InfiniteMenu.getHiddenKeys('.'));
    }
}
