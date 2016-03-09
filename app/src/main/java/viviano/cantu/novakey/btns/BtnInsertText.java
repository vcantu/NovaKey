package viviano.cantu.novakey.btns;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.drawing.Icons;

/**
 * Created by Viviano on 8/16/2015.
 */
public class BtnInsertText extends Btn {

    private String insertText = "";
    public BtnInsertText(double angle, float dist, int shape) {
        super(angle, dist, shape);
        icon = Icons.get("space_bar");
    }
    public void setText(String txt) {
        insertText = txt;
    }

    @Override
    public void onClick() {
        //Controller.input(insertText, 0);
        Controller.vibrate(50);
        Controller.input(" ", 0);
    }

}
