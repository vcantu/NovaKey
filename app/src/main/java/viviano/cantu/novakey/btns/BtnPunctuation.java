package viviano.cantu.novakey.btns;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.menus.InfiniteMenu;

/**
 * Created by Viviano on 7/9/2015.
 */
public class BtnPunctuation extends Btn implements Btn.InfiniteMenuable {

    public BtnPunctuation(double angle, float dist, int shape) {
        super(angle, dist, shape);
        text = ".";
    }

    @Override
    public void onClick() {
        Controller.input('.', 0);
    }

    @Override
    public Object[] getInfiniteMenu() {
        return InfiniteMenu.getChars('.');
    }

    @Override
    public void onItemSelected(Object selected) {
        isDown = false;
        Controller.input(selected, 0);
    }
}
