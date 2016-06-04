package viviano.cantu.novakey;

import android.inputmethodservice.Keyboard;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.view.NovaKeyView;

/**
 * Created by Viviano on 7/10/2015.
 */
public class NovaKeyListener implements View.OnTouchListener {

    private ArrayList<Integer> areasCrossed;
    // rotating
    private int prevArea = -1, prevSector = -1;

    private EventListener sendEvent;

    private boolean isClick = false;

    private float currX, currY;
    private int currArea, currSector;
    private Btn currBtn = null;

    public NovaKeyListener(EventListener eventListener) {
        sendEvent = eventListener;
        createTimers();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        NovaKeyView view = (NovaKeyView)v;
        currX = event.getX(0);
        currY = event.getY(0);
        currArea = view.getArea(currX, currY);
        currSector = view.getSector(currX, currY);

        sendEvent.onRawAction(event.getAction(), currX, currY);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //TODO: maybe dont add area when area is invalid!!!!!!!!!!!!!!!!!!
            case MotionEvent.ACTION_DOWN:
                startClickTimer();
                currBtn = checkBtn(view, currX, currY);
                if (currBtn != null) {
                    currBtn.onDown();
                    btnPress.begin();
                }
                areasCrossed = new ArrayList<>();
                if (currArea != Keyboard.KEYCODE_CANCEL) {
                    areasCrossed.add(currArea);
                    singlePress.begin();
                    sendEvent.onDownArea(currArea);
                    prevArea = currArea;
                    prevSector = currSector;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                currBtn = checkBtn(view, currX, currY);
                //DETECT AREA
                if (areasCrossed.size() > 0) {
                    if (currArea != prevArea && currArea != Keyboard.KEYCODE_CANCEL) {
                        areasCrossed.add(currArea);
                        singlePress.reset();

                        sendEvent.onNewArea(currArea, areasCrossed);
                        prevArea = currArea;
                    }
                }
                else if (currArea != Keyboard.KEYCODE_CANCEL) {
                    areasCrossed.add(currArea);
                    singlePress.reset();

                    sendEvent.onNewArea(currArea, areasCrossed);
                    prevArea = currArea;
                    prevSector = currSector;
                }
                //DETECT ROTATION
                if (currSector != prevSector) {
                    boolean clockwise = !((prevSector < currSector
                            && !(prevSector == 1 && currSector == 5))
                            || (prevSector == 5 && currSector == 1));
                    sendEvent.onRotate(clockwise, currSector, currArea == 0);
                    prevSector = currSector;
                }
                break;
            case MotionEvent.ACTION_UP:
                sendEvent.onUp(currArea, areasCrossed);
                prevArea = -1;
                prevSector = -1;
                singlePress.cancel();
                btnPress.cancel();
                doublePress.cancel();
                clickTimer.cancel();
                if (currBtn != null)
                    currBtn.onUp();
                if (isClick && view.onBtn(currX, currY, currBtn))
                    sendEvent.onBtnClick(currBtn);
                isClick = false;
                break;
            //for multitouch
            case MotionEvent.ACTION_POINTER_DOWN:
                singlePress.cancel();
                btnPress.cancel();
                doublePress.begin();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                doublePress.cancel();
                break;
        }
        return true;
    }

    private Btn checkBtn(NovaKeyView view, float x, float y) {
        for (Btn b : Settings.btns) {
            if (view.onBtn(x, y, b)) {
                return b;
            }
        }
        return null;
    }

    //Timers
    private class CustomTimer {
        CountDownTimer timer;
        TimerEvent event;
        long time;
        CustomTimer(long milliseconds, final TimerEvent event) {
            this.event = event;
            time = milliseconds;
        }
        void begin() {
            timer = new CountDownTimer(time, time) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                    event.onFinish();
                }
            }.start();
        }
        void cancel() {
            if (timer != null)
                timer.cancel();
        }
        void reset() {
            cancel();
            begin();
        }
    }
    private interface TimerEvent {
        void onFinish();
    }
    private CustomTimer singlePress, doublePress, btnPress, clickTimer;

    public void createTimers() {
        singlePress = new CustomTimer(Settings.longPressTime, new TimerEvent() {
            @Override
            public void onFinish() {
                sendEvent.onLongPress(currArea, areasCrossed, currX, currY);
            }
        });
        doublePress = new CustomTimer(1000, new TimerEvent() {
            @Override
            public void onFinish() {
                sendEvent.onTwoFingerLongPress();
            }
        });
        btnPress = new CustomTimer(Settings.longPressTime, new TimerEvent() {
            @Override
            public void onFinish() {
                if (currBtn != null)
                    sendEvent.onBtnLongPress(currBtn, currX, currY);
            }
        });
        clickTimer = new CustomTimer(400, new TimerEvent() {
            @Override
            public void onFinish() {
                isClick = false;
            }
        });
    }
    private void startClickTimer() {
        isClick = true;
        clickTimer.begin();
    }

    public interface EventListener {
        void onRawAction(int action, float x, float y);
        void onDownArea(int area);
        void onNewArea(int currArea, ArrayList<Integer> areasCrossed);
        void onRotate(boolean clockwise, int currSector, boolean inCenter);
        void onUp(int lastArea, ArrayList<Integer> areasCrossed);
        void onLongPress(int currArea, ArrayList<Integer> areasCrossed, float currX, float currY);
        void onTwoFingerLongPress();
        //Btns
        void onBtnClick(Btn btn);
        void onBtnLongPress(Btn btn, float currX, float currY);
    }
}
