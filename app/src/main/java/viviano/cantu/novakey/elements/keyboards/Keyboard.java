package viviano.cantu.novakey.elements.keyboards;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.controller.touch.TypingHandler;
import viviano.cantu.novakey.elements.keyboards.overlays.OverlayElement;
import viviano.cantu.novakey.model.MainDimensions;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.themes.MasterTheme;

public class Keyboard implements OverlayElement, Iterator<Key>, Iterable<Key> {

    private final TouchHandler mHandler;
    private final Key[][] keys;
    private final String name;

    private int currG = 0, currL = 0;

    public Keyboard(String name, Key[][] keys) {
        this.keys = keys;
        this.name = name;
        mHandler = new TypingHandler(this);
    }

    @Override
    public Iterator<Key> iterator() {
        currG = 0;
        currL = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return currG < keys.length && currL < keys[currG].length;
    }

    @Override
    public Key next() {
        if (currG >= keys.length || currL >= keys[currG].length)
            throw new NoSuchElementException();
        Key k = keys[currG][currL];
        currL++;
        if (currL >= keys[currG].length) {
            currG++;
            currL=0;
        }
        return k;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private Key getKey(int group, int loc) {
        if (loc > keys[group].length)//for alt layouts
            return keys[group][0];
        return keys[group][loc];
    }

    /**
     * Draws the element.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *
     * @param model
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Model model, MasterTheme theme, Canvas canvas) {
        MainDimensions d = model.getMainDimensions();
        if (!(model.getKeyboardCode() > 0 && //on an alphabet
                Settings.hideLetters || (Settings.hidePassword
                && model.getInputState().onPassword()))) {
            //CODE4: Drawing keys
            for (Key k : this) {
                theme.getBoardTheme().drawItem(
                        k.getDrawable(model.getShiftState()),
                        k.getPosn().getX(d),
                        k.getPosn().getY(d),
                        k.getSize() * (d.getRadius() / (16 / 3)), canvas);
            }
        }
    }

    @Override
    public boolean handle(MotionEvent event, Controller control) {
        return mHandler.handle(event, control);
    }

    /**
     * will return the keyCode for the actions done
     * or Keyboard.KEYCODE_CANCEL if invalid
     */
    public Action getKey(List<Integer> areasCrossed) {
        if (areasCrossed.size() <= 0)
            return null;
        //regular areas
        //gets first and last of list
        int firstArea = areasCrossed.get(0);
        //Inside circle
        if (firstArea >= 0) {
            Action act = Util.getGesture(areasCrossed);
            if (act != null) {
                return act;
            }
            Location l = getLoc(areasCrossed);
            if (l != null)
                return getKey(l.x, l.y);
        }
        return null;
    }

    private Location getLoc(List<Integer> areasCrossed) {
        if (areasCrossed.size() <= 0)
            return null;
        //regular areas
        //gets first and last of list
        int firstArea = areasCrossed.get(0);
        int lastArea = areasCrossed.get(areasCrossed.size() > 1 ? areasCrossed.size() - 1 : 0);
        //sets to last or first if there is only one value
        int secondArea = areasCrossed.get(areasCrossed.size() > 1 ? 1 : 0);
        //sets to second value or first if there is only one value

        //Inside circle
        if (firstArea >= 0) {
            //loops twice checks first and last area first, then checks first and second area
            int check = lastArea;
            for (int i=0; i<2; i++) {
                if (firstArea == 0 && check >= 0)//center
                    return new Location(0, check);
                else {
                    if (firstArea == check)
                        return new Location(firstArea, 0);
                    else if (check == firstArea+1 || (firstArea == 5 && check == 1))
                        return new Location(firstArea, 1);
                    else if (check == 0)
                        return new Location(firstArea, 2);
                    else if (check == firstArea-1 || (firstArea == 1 && check == 5))
                        return new Location(firstArea, 3);
                    else if (check == -1 && areasCrossed.size() == 2)
                        return new Location(firstArea, 4);
                }
                check = secondArea;
            }
        }
        return null;
    }

    public static class Location {
        final int x, y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
