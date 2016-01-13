package viviano.cantu.novakey;

import java.util.ArrayList;

import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.menus.OnUpMenu;

/**
 * Created by Viviano on 8/2/2015.
 */
public class Clipboard {

    private static final int MAX_CLIP_SIZE = 20;
    private static ArrayList<String> clips = new ArrayList<String>();

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

    public static void createMenu() {
        Object[] list = new Object[] {
                Icon.get("copy"),
                Icon.get("select_all"),
                Icon.get("paste"),
                Icon.get("deselect_all"),
                Icon.get("cut")
        };
        MENU = new OnUpMenu(list, new OnUpMenu.Action() {
            @Override
            public void onUp(int selected) {
                Controller.performClipboardAction(selected);
            }

            @Override
            public void onLongPress(int selected) {
                //if paste is long pressed
                if (selected == 3) {// == 3 made negative 1 to turn off
                    ArrayList<String> lst = new ArrayList<String>();
                    for (int i=0; i<Clipboard.clipCount(); i++) {
                        try {
                            String text = Clipboard.get(i);
                            if (text != null)
                                lst.add(text);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                    lst.add(InfiniteMenu.CANCEL);
                    Object[] arr = new Object[lst.size()];
                    arr = lst.toArray(arr);
                    Controller.startInfiniteMenu(new InfiniteMenu(arr,
                            new InfiniteMenu.Action() {
                                @Override
                                public void onSelected(Object selected) {
                                    if (!InfiniteMenu.isCancel(selected))
                                        Controller.input(selected, 0);
                                }
                            }), MENU.fingerX, MENU.fingerY);
                }
                //If select_all is longPressed
                else if (selected == 2) {
                    Object[] arr = new Object[] {
                            Icon.get("select_all"),
                            Icon.get("select_all_copy"),
                            Icon.get("select_all_cut"),
                            Icon.get("select_all_clear"),
                            InfiniteMenu.CANCEL//char is not an object so double cast is required
                    };
                    Controller.startInfiniteMenu(new InfiniteMenu(arr, new InfiniteMenu.Action() {
                        @Override
                        public void onSelected(Object selected) {
                            if (!InfiniteMenu.isCancel(selected)) {
                                Controller.performClipboardAction(NovaKey.CB_SELECT_ALL);
                                if (selected == Icon.get("select_all_copy"))
                                    Controller.performClipboardAction(NovaKey.CB_COPY);
                                else if (selected == Icon.get("select_all_cut"))
                                    Controller.performClipboardAction(NovaKey.CB_CUT);
                                else if (selected == Icon.get("select_all_clear"))
                                    Controller.input("", 0);
                            }
                        }
                    }), MENU.fingerX, MENU.fingerY);
                }
            }
        });
    }
}
