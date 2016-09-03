package viviano.cantu.novakey.model.elements.buttons;

import android.graphics.Canvas;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.controller.actions.ToggleKeyboardAction;
import viviano.cantu.novakey.model.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.view.drawing.drawables.TextDrawable;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 7/9/2015.
 *
 * changed from onClicked() to onDown()
 */
public class ButtonToggleModeChange extends Button {

    public ButtonToggleModeChange(ButtonData data) {
        super(data);
    }


    /**
     * Draws the button. Button must handle it's own paint.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *
     * @param model
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        if (model.getKeyboardCode() == Keyboards.PUNCTUATION ||
                model.getKeyboardCode() == Keyboards.SYMBOLS)
            setIcon(new TextDrawable("AZ"));//TODO: other languages
        else
            setIcon(new TextDrawable("#!"));
        super.draw(model, theme, canvas);
    }


    /**
     * @return action to fire, or null if no action is needed
     */
    @Override
    protected Action onClickAction() {
        return new ToggleKeyboardAction();
    }

    /**
     * @return action to fire, or null if no action is needed
     */
    @Override
    protected Action onLongPressAction() {
        return new SetKeyboardAction(Keyboards.SYMBOLS);
    }
}
