package viviano.cantu.novakey.menus;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 7/14/2015.
 */
public class InfiniteMenu {

    private Object parent;
    private Object[] list;
    private int index = 0;
    private Action action;

    private float fingerX, fingerY;

    public InfiniteMenu(Object[] list, Action action) {
        this(list);
        this.action = action;
    }

    //It's private because the constructor above is preferred
    private InfiniteMenu(Object[] list) {
        this.list = list;
        if (list.length>0)
            parent = list[0];
    }

    public void reset() {
        index = 0;
    }

    public boolean matches(Object o) {
        return o.equals(parent) || o == parent;
    }

    public void replaceParent(Object o) {
        list[0] = o;
    }

    public void resetParent() {
        list[0] = parent;
    }

    public void up() {
        index++;
        if (index >= list.length)
            index = 0;
    }
    public void down() {
        index--;
        if (index < 0) {
            index = list.length-1;
        }
    }
    private int indexInBounds(int i) {
        if (i<0)//if negative add length
            i += list.length;
        if (i<0)//if still negative, call recursively
            return indexInBounds(i);
        return i % list.length;
    }


    public void updateCoords(float x, float y) {
        fingerX = x;
        fingerY = y;
    }

    public void draw(float x, float y, float r, float sr, int color, Paint p, Canvas canvas) {
        float size = sr * 1.3f;
        //get distance
        float distanceFromMiddle = 0;
        double angle = Util.getAngle(fingerX - x, y - fingerY);

        angle = (angle < Math.PI / 2 ? Math.PI * 2 + angle : angle);//sets angle to [90, 450]
        for (int i=0; i < 5; i++) {
            double angle1 = (i * 2 * Math.PI) / 5  + Math.PI / 2;
            double angle2 = ((i+1) * 2 * Math.PI) / 5  + Math.PI / 2;
            if (angle >= angle1 && angle < angle2) {
                distanceFromMiddle = (float)(Math.PI/5 - (angle - angle1));//gets the difference in angle
                distanceFromMiddle = distanceFromMiddle/(float)(Math.PI/5);//gets the percentage
                break;
            }
        }

        //draw letters
        //center
        if (list.length > 0) {
            for (int i=0; i<4; i++) {
                //----------------------------------DRAW MIDDLE------------------------------------
                if (i==0) {
                    float factor = (float) (distanceFromMiddle / Math.pow(2, i+1));
                    if (Math.abs(distanceFromMiddle) < .25)//TODO: THIS SNAPS MAKE PRETTY
                        draw(index, x, y, size, color, p, canvas);
                    else
                        draw(index, x + r * factor, y + r * (factor * factor / 2),
                                size * (1 - Math.abs(factor)), color, p, canvas);
                }
                else {
                    //----------------------------------DRAW RIGHT---------------------------------
                    float addTo = 0;
                    for (int j=1; j<i+1; j++) {
                        addTo += 1 / Math.pow(2, j);
                    }
                    float factor = (float)(addTo + (distanceFromMiddle < 0 ? 0 :
                            distanceFromMiddle / Math.pow(2, i+1)));
                    draw(indexInBounds(index + i),x + r * factor, y + r * (factor * factor / 2),
                            size * (1 - Math.abs(factor)), color, p, canvas);
                    //----------------------------------DRAW LEFT----------------------------------
                    addTo = 0;
                    for (int j=1; j<i+1; j++) {
                        addTo -= 1 / Math.pow(2, j);
                    }
                    factor = (float)(addTo + (distanceFromMiddle >= 0 ? 0 :
                            distanceFromMiddle / Math.pow(2, i+1)));
                    draw(indexInBounds(index - i), x + r * factor, y + r * (factor * factor / 2),
                            size * (1 - Math.abs(factor)), color, p, canvas);
                }
            }
        }
    }

    private void draw(int i, float x, float y, float size, int color, Paint p, Canvas canvas) {
        Object o = list[i];
        p.setColor(color);
        if (o instanceof Icon.Drawable) {
            ((Icon.Drawable)o).draw(x, y, size, p, canvas);
        }
        else {
            String s = "";
            if (o instanceof Character)
                s = Character.toString((Character)o);
            if (o instanceof String)
                s = (String) o;
            else
                try {
                    s = s.toString();
                } catch (Exception e) {}

            if (s.equals(CANCEL))
                Icon.get("clear").draw(x, y, size, p, canvas);
            else {
                int MAX = 12;
                if (s.length() > 4) {
                    size /= 4;
                    s = Util.toMultiline(s, MAX);
                    if (s.split("\n").length > 6) {
                        int last = Util.nthIndexOf(s, 5, '\n');
                        if (last < 3)//Prevents a negative length in the substring method
                            last = 3;
                        s = s.substring(0, last-3) + "...";
                    }
                }
                Draw.text(s, x, y, size, p, canvas);
            }
        }
    }

    public void setAction(Action action) {
        this.action = action;
    }
    public interface Action {
        void onSelected(Object selected);
    }
    public void performSelection() {
        if (action != null)
            action.onSelected(list[index]);
    }

    public static String CANCEL = String.valueOf((char)24);
    public static boolean isCancel(Object o) {
        return o instanceof String && o.equals(CANCEL);
    }

    public static ArrayList<InfiniteMenu> HIDDEN_KEYS;
    public static void setHiddenKeys(String[] S) {
        HIDDEN_KEYS = new ArrayList<InfiniteMenu>();
        for (String s : S) {
            if (s.length() > 0) {
                Character[] C = new Character[s.length()];
                for (int i=0; i<s.length(); i++) {
                    C[i] = s.charAt(i);
                }
                InfiniteMenu im = new InfiniteMenu(C, new Action() {
                    @Override
                    public void onSelected(Object selected) {
                        Controller.input(selected, 0);
                    }
                });
                HIDDEN_KEYS.add(im);
            }
        }
    }
    public static Character[] getChars(char parent) {
        for (InfiniteMenu im : HIDDEN_KEYS) {
            if ((Character)im.parent == parent) {
                return (Character[])im.list;
            }
        }
        return null;
    }
}
