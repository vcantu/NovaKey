/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.elements.buttons;

import android.graphics.Canvas;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.controller.actions.ToggleKeyboardAction;
import viviano.cantu.novakey.elements.keyboards.Keyboards;
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
