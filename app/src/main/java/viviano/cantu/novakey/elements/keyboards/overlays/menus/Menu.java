package viviano.cantu.novakey.elements.keyboards.overlays.menus;

import viviano.cantu.novakey.controller.actions.NoAction;
import viviano.cantu.novakey.controller.actions.Action;
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
