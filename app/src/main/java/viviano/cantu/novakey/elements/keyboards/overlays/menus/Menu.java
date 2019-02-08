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

package viviano.cantu.novakey.elements.keyboards.overlays.menus;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.NoAction;
import viviano.cantu.novakey.view.drawing.Icons;

/**
 * Created by Viviano on 6/16/2016.
 */
public interface Menu {

    /**
     * Base class for each entry
     * can be extended to add more functionality
     */
    class Entry {
        public final Object data;
        public final Action action;


        public Entry(Object data, Action action) {
            this.data = data;
            this.action = action;
        }
    }

    Entry CANCEL = new Entry(Icons.get("clear"), new NoAction());
}
