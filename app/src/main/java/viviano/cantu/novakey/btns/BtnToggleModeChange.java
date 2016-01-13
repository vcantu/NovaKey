package viviano.cantu.novakey.btns;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.Print;

/**
 * Created by Viviano on 7/9/2015.
 *
 * changed from onClicked() to onDown()
 */
public class BtnToggleModeChange extends Btn implements Btn.LongPressable {

    private boolean ignoreUpEvent = false;
    public BtnToggleModeChange(double angle, float dist, int shape) {
        super(angle, dist, shape);
        setText();
    }

    @Override
    public void onUp() {
        super.onUp();
        setText();
        ignoreUpEvent = false;
    }

    @Override
    public void onClick() {
        if (!ignoreUpEvent) {
            switch (Controller.state & NovaKey.KEYS_MASK) {
                case NovaKey.DEFAULT_KEYS:
                    Controller.setKeys(NovaKey.PUNCTUATION);
                    break;
                default:
                case NovaKey.PUNCTUATION:
                case NovaKey.SYMBOLS:
                    Controller.setKeys(NovaKey.DEFAULT_KEYS);
            }
        }
    }

    @Override
    public void onLongPress() {
        Controller.setKeys(NovaKey.SYMBOLS);
        setText();
        Controller.view().invalidate();
        ignoreUpEvent = true;
        isDown = false;
    }

    private void setText() {
        if (Controller.hasState(NovaKey.PUNCTUATION) ||
                Controller.hasState(NovaKey.SYMBOLS))
            text = "AZ";//TODO: other languages
        else
            text = "#!";
    }
}
