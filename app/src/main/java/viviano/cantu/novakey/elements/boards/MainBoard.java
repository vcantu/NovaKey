package viviano.cantu.novakey.elements.boards;

import android.graphics.Canvas;
import android.graphics.drawable.Icon;
import android.inputmethodservice.Keyboard;

import java.util.List;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.touch.TouchHandler;
import viviano.cantu.novakey.controller.touch.TypingHandler;
import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.model.StateModel;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.model.keyboards.KeyLayout;
import viviano.cantu.novakey.model.properties.KeyProperties;
import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.INovaKeyView;
import viviano.cantu.novakey.view.drawing.Icons;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;

/**
 * Created by Viviano on 6/20/2016.
 */
public class MainBoard implements Board {

    //Dimensions updated every time the draw method is called
    private int w, h;
    private float x, y;
    private float r, sr;

    /**
     * @param x x position
     * @param y y position
     * @return returns the current area based on the given coordinates
     */
    @Override
    public int getArea(float x, float y) {
        if (Util.distance(this.x, this.y, x, y) <= this.sr) //inner circle
            return 0;
        else if (Util.distance(this.x, this.y, x, y) <= this.r)
            return getSector(x, y);
        return -1;//outside area
    }

    /**
     * @param x x position
     * @param y y position
     * @return returns the current rotational sector based on the given coordinates
     */
    @Override
    public int getSector(float x, float y) {
        return getSectorFromCenter(x, y, this.x, this.y);
    }

    /*
    * Will return a number [1, 5]
    * representing which sector, the x and y is in
    * returns Keyboard.KEYCODE_CANCEL if invalid
    */
    private int getSectorFromCenter(float x, float y, float centX, float centY) {
        x -= centX;
        y = centY - y;
        double angle = Util.getAngle(x, y);
        angle = (angle < Math.PI / 2 ? Math.PI * 2 + angle : angle);//sets angle to [90, 450]
        for (int i=0; i < 5; i++) {
            double angle1 = (i * 2 * Math.PI) / 5  + Math.PI / 2;
            double angle2 = ((i+1) * 2 * Math.PI) / 5  + Math.PI / 2;
            if (angle >= angle1 && angle < angle2)
                return i+1;
        }
        return Keyboard.KEYCODE_CANCEL;
    }


    /**
     * Draws the element.
     * Never call this method directly unless inside of a
     * View's onDraw() method
     *  @param view   view given for context. Use this for access to the model
     * @param theme  theme for drawing properties
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(INovaKeyView view, MasterTheme theme, Canvas canvas) {
        StateModel stateModel = view.getStateModel();
        DrawModel drawModel = view.getDrawModel();
        BoardTheme board = theme.getBoardTheme();

        updateDimensions(view.getDrawModel());

        theme.getBackgroundTheme().drawBackground(0, 0, w, h, x, y, r, sr, canvas);

        board.drawBoard(x, y, r, sr, canvas);

        switch (stateModel.getUserState()) {
            case TYPING:
            default:
                if (!(stateModel.getKeyboardCode() > 0 && //on an alphabet
                        Settings.hideLetters || (Settings.hidePassword
                        && stateModel.getInputState().onPassword()))) {
                    //CODE4: Drawing keys
                    for (KeyProperties kp : drawModel.getKeyProperties()) {
                        board.drawItem(kp.getDrawable(stateModel.getShiftState()),
                                kp.getPosn().getX(drawModel),
                                kp.getPosn().getY(drawModel),
                                kp.getSize() * (r / (16 / 3)), canvas);
                    }
                }
                break;
            case SELECTING:
                int cursorCode = view.getStateModel().getCursorMode();
                board.drawItem(Icons.cursors, x, y, 1, canvas);
                if (cursorCode >= 0)
                    board.drawItem(Icons.cursorLeft, x, y, 1, canvas);
                if (cursorCode <= 0)
                    board.drawItem(Icons.cursorRight, x, y, 1, canvas);
                break;
            case ON_MENU://if on a menu which overlays the keyboard
                //not responsible for drawing anything here
        }
    }

    private void updateDimensions(DrawModel drawModel) {
        w = drawModel.getWidth();
        h = drawModel.getHeight();
        x = drawModel.getX();
        y = drawModel.getY();
        r = drawModel.getRadius();
        sr = drawModel.getSmallRadius();
    }

    /**
     * @param controller provides context to the handler
     * @return a touch handler which returns true if being used
     * or false otherwise. For example if a button element is activated by being
     * clicked, if the handler detects this in the onDown event it will
     * return true. Otherwise false, in order to allow other handlers
     * to be activated
     */
    @Override
    public TouchHandler getTouchHandler(Controller controller) {
        return new TypingHandler(this);
    }
}
