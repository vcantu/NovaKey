package viviano.cantu.novakey.model.elements;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.model.elements.keyboards.Keyboard;
import viviano.cantu.novakey.model.elements.keyboards.overlays.OverlayElement;

/**
 * Created by Viviano on 8/21/2016.
 *
 * Manages the elements with the default behavior.
 *
 * Default behavior is to always have a MainElement
 * The main element is always at the end of the list
 */
public class DefaultElementManager implements ElementManager {

    private final List<Element> mList;
    private MainElement mMain;

    public DefaultElementManager(Keyboard keyboard) {
        mMain = new MainElement(keyboard);

        mList = new ArrayList<>();
    }

    /**
     * @return a list of elements where:
     * the first on the list are the first drawn
     */
    @Override
    public List<Element> getElements() {
        List<Element> list = new ArrayList<>(mList);
        list.add(0, mMain);//first element
        return list;
    }

    /**
     * Replaces or adds the given element to the topmost element which lives
     * on top of the main element
     *
     * @param element element to live on top of the main element
     */
    @Override
    public void setOverlayElement(OverlayElement element) {
        mMain.setOverlay(element);
    }

    /**
     * clears the overlaying element
     */
    @Override
    public void removeOverlayElement() {
        mMain.setOverlay(null);
    }

    /**
     * adds a new element to the stack below the main and overlay elements
     *
     * @param element element to add
     */
    @Override
    public void addElement(Element element) {
        mList.add(element);
    }
}
