package viviano.cantu.novakey.model.elements;

import java.util.List;

/**
 * Created by Viviano on 8/21/2016.
 *
 * THis will store and manage elements.
 * It will receive updates and update the element list appropriately
 *
 */
public interface ElementManager {

    /**
     * @return a list of elements where:
     * the first on the list are the first drawn.
     * Used by a view to draw, or touch listeners to send events to
     * the first handlers on the list
     */
    List<Element> getElements();

    /**
     * Replaces or adds the given element to the topmost element which lives
     * on top of the main element
     *
     * @param element element to live on top of the main element
     */
    void setOverlayElement(OverlayElement element);

    /**
     * clears the overlaying element
     */
    void removeOverlayElement();

    /**
     * adds a new element to the stack below the main and overlay elements
     * @param element element to add
     */
    void addElement(Element element);
}
