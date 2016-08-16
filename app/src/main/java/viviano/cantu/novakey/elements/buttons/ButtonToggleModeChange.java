package viviano.cantu.novakey.elements.buttons;

import android.graphics.Canvas;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.controller.actions.ToggleKeyboardAction;
import viviano.cantu.novakey.model.keyboards.KeyLayout;
import viviano.cantu.novakey.view.INovaKeyView;
import viviano.cantu.novakey.view.drawing.drawables.TextDrawable;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 7/9/2015.
 *
 * changed from onClicked() to onDown()
 */
public class ButtonToggleModeChange extends Button {

    /**
     * Draws the button. Button must handle it's own paint.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *  @param view   view given for context
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(INovaKeyView view, MasterTheme theme, Canvas canvas) {
        if (view.getStateModel().getKeyboardCode() == KeyLayout.PUNCTUATION ||
                view.getStateModel().getKeyboardCode() == KeyLayout.SYMBOLS)
            setIcon(new TextDrawable("AZ"));//TODO: other languages
        else
            setIcon(new TextDrawable("#!"));
        super.draw(view, theme, canvas);
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
        return new SetKeyboardAction(KeyLayout.SYMBOLS);
    }
}
