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

package viviano.cantu.novakey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.Actions;
import viviano.cantu.novakey.controller.actions.ClipboardAction;
import viviano.cantu.novakey.controller.actions.NoAction;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.actions.input.DeleteAction;
import viviano.cantu.novakey.controller.actions.input.InputAction;
import viviano.cantu.novakey.elements.keyboards.overlays.menus.InfiniteMenu;
import viviano.cantu.novakey.elements.keyboards.overlays.menus.Menu;
import viviano.cantu.novakey.elements.keyboards.overlays.menus.OnUpMenu;
import viviano.cantu.novakey.view.drawing.Icons;

/**
 * Created by Viviano on 8/2/2015.
 */
public class Clipboard {

    private static final int MAX_CLIP_SIZE = 20;
    private static List<String> clips = new ArrayList<String>();

    public static int COPY = 1, SELECT_ALL = 2, PASTE = 3, DESELECT_ALL = 4, CUT = 5;


    public static void add(String text) {
        clips.add(0, text);
        if (clips.size() > MAX_CLIP_SIZE)
            clips.remove(MAX_CLIP_SIZE);
    }


    public static String get(int index) {
        if (clips.size() > index)
            return clips.get(index);
        return null;
    }


    public static int clipCount() {
        return clips.size();
    }


    ///------------------------------------------On up menu--------------------------------
    public static OnUpMenu MENU;


    /**
     * Loads the clipboard and creates a menu
     *
     * @return returns an infinite menu with all the clipboard entries + a cancel entry
     */
    private static InfiniteMenu createClipboard() {
        List<Menu.Entry> entries = new ArrayList<>();
        for (int i = 0; i < Clipboard.clipCount(); i++) {
            try {
                String text = Clipboard.get(i);
                if (text != null)
                    entries.add(new Menu.Entry(text, new InputAction(text)));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        entries.add(Menu.CANCEL);

        return new InfiniteMenu(entries);
    }


    public static void createMenu() {
        Action copy = new ClipboardAction(COPY);
        Action cut = new ClipboardAction(CUT);
        Action paste = new ClipboardAction(PASTE);
        Action select_all = new ClipboardAction(SELECT_ALL);
        Action deselect_all = new ClipboardAction(DESELECT_ALL);

        //select all list
        List<Menu.Entry> select = Arrays.asList(
                new Menu.Entry(Icons.get("select_all"),
                        select_all),

                new Menu.Entry(Icons.get("select_all_copy"),
                        new Actions(select_all, copy)),

                new Menu.Entry(Icons.get("select_all_cut"),
                        new Actions(select_all, cut)),

                new Menu.Entry(Icons.get("select_all_clear"),
                        new Actions(select_all, new DeleteAction())),

                Menu.CANCEL
        );
        InfiniteMenu selectAll = new InfiniteMenu(select);


        //Main list
        List<Menu.Entry> main = Arrays.asList(
                new OnUpMenu.Entry(Icons.get("content_copy"),
                        copy, new NoAction()),

                new OnUpMenu.Entry(Icons.get("select_all"),
                        select_all, new SetOverlayAction(selectAll)),

                new OnUpMenu.Entry(Icons.get("content_paste"),
                        paste, (ime, control, model) -> {
                    InfiniteMenu clipboard = createClipboard();
                    control.fire(new SetOverlayAction(clipboard));
                    return null;
                }),

                new OnUpMenu.Entry(Icons.get("deselect_all"),
                        deselect_all, new NoAction()),

                new OnUpMenu.Entry(Icons.get("content_cut"),
                        cut, new NoAction())
        );
        MENU = new OnUpMenu(main);
    }
}
