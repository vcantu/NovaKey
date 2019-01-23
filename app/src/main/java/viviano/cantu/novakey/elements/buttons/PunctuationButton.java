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

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.actions.input.KeyAction;
import viviano.cantu.novakey.elements.keyboards.overlays.menus.InfiniteMenu;
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
        return new KeyAction('.');
    }

    /**
     * @return action to fire, or null if no action is needed
     */
    @Override
    protected Action onLongPressAction() {
        return new SetOverlayAction(InfiniteMenu.getHiddenKeys('.'));
    }
}
