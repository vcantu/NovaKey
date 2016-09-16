package viviano.cantu.novakey.controller.touch;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.SetOverlayAction;
import viviano.cantu.novakey.controller.actions.typing.InputAction;
import viviano.cantu.novakey.model.elements.keyboards.Keyboard;
import viviano.cantu.novakey.model.elements.menus.InfiniteMenu;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.model.elements.overlays.CursorOverlay;
import viviano.cantu.novakey.model.elements.overlays.DeleteOverlay;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/14/2016.
 */
public class TypingHandler extends AreaCrossedHandler {

    private final List<Integer> mAreas;
    private final Keyboard mKeyboard;

    private CountDownTimer mTimer;

    private int mRepeatingChar;
    public boolean mRepeating = false;
    private int mArea1, mArea2;

    public TypingHandler(Keyboard keyboard) {
        mKeyboard = keyboard;
        mAreas = new ArrayList<>();
    }

    /**
     * Starts longpress timer
     * @param controller
     */
    private void start(Controller controller) {
        mTimer = new CountDownTimer(Settings.longPressTime, Settings.longPressTime) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                InfiniteMenu newMenu = InfiniteMenu.getHiddenKeys(
                        (char) mKeyboard.getKey(mAreas));
                if (newMenu != null)
                    controller.fire(new SetOverlayAction(newMenu));
            }
        };
        if (true)//TODO: make this flag a in tutorial and longpress is disabled
            mTimer.start();
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
     */
    @Override
    protected boolean onDown(float x, float y, int area,
                             Controller controller) {
        mAreas.clear();
        mRepeating = false;
        mAreas.add(area);
        start(controller);
        return true;
    }

    /**
     * Called when the touch listener detects that there
     * has been a cross, either in sector or range
     * @param event describes the event
     * @param controller
     */
    @Override
    protected boolean onCross(CrossEvent event, Controller controller) {
        cancel();
        start(controller);
        mAreas.add(event.newArea);
        if (mRepeating) {
            if (event.newArea == mArea1 || event.newArea == mArea2) {
                controller.fire(new InputAction(mRepeatingChar));
            }
            else {
                mRepeating = false;
            }
        }
        else if (mAreas.size() >= 3) {
            //test if duplicate
            int idx = repeatingIndex();
            if (idx != -1) {
                //switch to repeat handler
                mArea1 = mAreas.get(idx);
                mArea2 = mAreas.get(idx + 1);

                List<Integer> areas = new ArrayList<>(mAreas);
                areas.remove(areas.size() - 1);

                mRepeatingChar = mKeyboard.getKey(Arrays.asList(mArea1, mArea2)
                );
                mRepeating = true;
                controller.fire(new InputAction(mRepeatingChar));
                return true;
            }
            if (Util.getGesture(mAreas) == android.inputmethodservice.Keyboard.KEYCODE_DELETE) {
                //switch to delete handler
                controller.fire(new SetOverlayAction(new DeleteOverlay()));
                cancel();
                return false;
            }
            if (isRotating(mAreas)) {
                controller.fire(new SetOverlayAction(new CursorOverlay()));
                cancel();
                return false;
            }
        }
        return true;
    }

    /**
     * Called when the user lifts finger, typically this
     * method expects a finalized action to be triggered
     * like typing a character
     * @param controller
     *
     */
    @Override
    protected boolean onUp(Controller controller) {
        cancel();
        if (!mRepeating) {
            int keyCode = mKeyboard.getKey(mAreas);
            controller.fire(new InputAction(keyCode));
        }
        System.out.println(mAreas);
        mAreas.clear();
        mRepeating = false;
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
        // or the outside
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
