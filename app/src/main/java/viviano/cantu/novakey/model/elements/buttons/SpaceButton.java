package viviano.cantu.novakey.model.elements.buttons;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.typing.InputAction;
import viviano.cantu.novakey.view.drawing.Icons;

/**
 * Created by Viviano on 8/16/2015.
 */
public class SpaceButton extends Button {

    public SpaceButton(ButtonData data) {
        super(data);
        setIcon(Icons.get("space_bar"));
    }

    /**
     * @return action to fire, or null if no action is needed
     */
    @Override
    protected Action onClickAction() {
        return new InputAction(" ");
    }

    /**
     * @return action to fire, or null if no action is needed
     */
    @Override
    protected Action onLongPressAction() {
        return null;
    }
}
