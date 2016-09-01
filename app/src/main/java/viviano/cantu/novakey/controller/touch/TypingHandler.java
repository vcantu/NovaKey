package viviano.cantu.novakey.controller.touch;

import android.inputmethodservice.Keyboard;
import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.actions.SetUserStateAction;
import viviano.cantu.novakey.elements.menus.InfiniteMenu;
import viviano.cantu.novakey.model.states.UserState;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/14/2016.
 */
public class TypingHandler extends AreaCrossedHandler {

    private final List<Integer> mAreas;

    private CountDownTimer mTimer;

    public TypingHandler() {
        mAreas = new ArrayList<>();
    }

    /**
     * Srarts longpress timer
     * @param controller
     */
    private void start(Controller controller) {
        mTimer = new CountDownTimer(Settings.longPressTime, Settings.longPressTime) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                controller.fire(new SetOverlayAction(InfiniteMenu.getHiddenKeys(
                        (char) controller.getModel().getKeyboard()
                                .getKey(mAreas,
                                        controller.getModel().getShiftState()))
                ));
            }
        };
    }
    private void cancel() {
        if (mTimer != null)
            mTimer.cancel();
    }

    /**
     * Override this to specify onDown behaviour
     * @param x       current x position
     * @param y       current y position
     * @param area    current area
     * @param controller    view being called on
     * @param manager use this to switch handlers
     */
    @Override
    protected boolean onDown(float x, float y, int area,
                             Controller controller, HandlerManager manager) {
        mAreas.add(area);
        start(controller);
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param event describes the event
     * @param controller
     * @param manager
     */
    @Override
    protected boolean onCross(CrossEvent event, Controller controller, HandlerManager manager) {
        cancel();
        start(controller);
        mAreas.add(event.newArea);
        if (mAreas.size() >= 3) {
            int idx = repeatingIndex();
            if (idx != -1) {
                //switch to repeat handler
                int repeatingChar = controller.getModel().getKeyboard()
                        .getKey(mAreas,
                                controller.getModel().getShiftState());

                manager.setHandler(new RepeatHandler(repeatingChar,
                        mAreas.get(idx), mAreas.get(idx + 2)));
            }
            if (Util.getGesture(mAreas) == Keyboard.KEYCODE_DELETE) {
                //switch to delete handler
                controller.fire(new SetUserStateAction(UserState.DELETING));
            }
            if (isRotating(mAreas)) {
                controller.fire(new SetUserStateAction(UserState.SELECTING));
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
        cancel();
        return false;
    }

    /**
     * finds the index of the first area which has a duplicate
     * exactly two spaces away. Which means the user swiped back
     * and forth
     * @return an index or -1 if no repetition is found
     */
    private int repeatingIndex() {
        for (int i = 0; i< mAreas.size() - 2; i++) {
            if (mAreas.get(i) == mAreas.get(i+2))
                return i;
        }
        return -1;
    }

    /**
     * //TODO: base rotation of keyLayout
     * Determines if the user began rotating
     *
     * @param areasCrossed list of areas crossed
     * @return if the user began rotating
     */
    private boolean isRotating(List<Integer> areasCrossed) {
        int checkIdx = 0;
        if (areasCrossed.get(0) == -1)
            checkIdx = 1;

        //if  either the user began from the inside,
        // or the ouside
        if ((checkIdx == 0 && areasCrossed.size() == 3) ||
                (checkIdx == 1 && areasCrossed.size() == 4)) {
            int one = areasCrossed.get(checkIdx + 0);
            int two = areasCrossed.get(checkIdx + 1);
            int three = areasCrossed.get(checkIdx + 2);
            boolean hasZero = one == 0 || two == 0 || three == 0;
            if (two != 3 && !hasZero && (
                    ((one+1)%5 == two%5 && (two+1)%5 == three%5) ||
                            ((three+1)%5 == two%5 && (two+1)%5 == one%5) )) {
                return true;
            }
        }
        return false;
    }
}
