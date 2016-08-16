package viviano.cantu.novakey.controller.touch;


import java.util.Stack;


import viviano.cantu.novakey.controller.actions.typing.InputAction;
import viviano.cantu.novakey.elements.boards.Board;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.typing.DeleteAction;

/**
 * Created by Viviano on 6/15/2016.
 */
public class DeleteHandler extends RotatingHandler {

    private final Action<String> mDelete, mBackspace;
    private final Action<String> mFastDelete, mFastBackspace;//TODO: fast delete
    private final Stack<String> mStack;
    private boolean mBackspacing = true;//false if deleting
    private boolean mGoingFast = false;

    public DeleteHandler(Board board, String first) {
        super(board);

        mDelete = new DeleteAction(true);
        mBackspace = new DeleteAction();

        mFastDelete = new DeleteAction(true, true);
        mFastBackspace = new DeleteAction(false, true);

        mStack = new Stack<>();
        if (first != null)
            mStack.add(first);
    }

    /**
     * Called when the user enters or exits the inner circle.
     * Call unrelated to onMove()
     * @param entered true if event was triggered by entering the
     *                inner circle, false if was triggered by exit
     * @param controller
     * @param manager
     */
    @Override
    protected boolean onCenterCross(boolean entered, Controller controller, HandlerManager manager) {
        return true;
    }

    /**
     * Called for every move event so that the handler can update
     * display properly. Called before onRotate()
     * @param x current finger x position
     * @param y current finger y position
     * @param controller
     * @param manager
     */
    @Override
    protected boolean onMove(float x, float y, Controller controller, HandlerManager manager) {
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param clockwise true if rotation is clockwise, false otherwise
     * @param inCenter  if finger position is currently in the center
     * @param controller
     * @param manager
     */
    @Override
    protected boolean onRotate(boolean clockwise, boolean inCenter, Controller controller, HandlerManager manager) {
        if (mBackspacing) {
            if (!clockwise) {//backspace
               String str = controller.fire(mBackspace);
                if (str != null)
                    mStack.add(str);
            } else {//add
                if (mStack.size() >= 1)
                    controller.fire(new InputAction(mStack.pop()));
                if (mStack.size() == 0)
                    mBackspacing = false;
            }
        } else {
            if (clockwise) {//delete
                String str = controller.fire(mDelete);
                if (str != null)
                    mStack.add(str);
            } else {//add
                if (mStack.size() >= 1)
                    controller.fire(new InputAction(mStack.pop(), false));
                //inputs to the right of the cursor
                if (mStack.size() == 0)
                    mBackspacing = true;

            }
        }
        return true;
    }

    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller
     * @param manager
     */
    @Override
    protected boolean onUp(Controller controller, HandlerManager manager) {
        return false;
    }
}
