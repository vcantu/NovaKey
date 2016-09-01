package viviano.cantu.novakey.controller.touch;

import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.SetEditingAction;

/**
 * Created by Viviano on 7/10/2015.
 */
public class NovaKeyListener implements View.OnTouchListener, HandlerManager {

    private final Controller mController;
    private TouchHandler mHandler;//current handler
    private CustomTimer mDoublePress;
    private MotionEvent mLastEvent;

    public NovaKeyListener(Controller controller) {
        mController = controller;
        mDoublePress = new CustomTimer(1000, () ->
                mController.fire(new SetEditingAction(true)));
    }

    /**
     * Push a handler to take priority over the
     * other handlers
     *
     * @param handler handler to take priority
     */
    @Override
    public void setHandler(TouchHandler handler) {
        mHandler = handler;
        if (mLastEvent != null)
            mHandler.handle(mLastEvent, mController, this);
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mLastEvent = event;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //for multitouch
            case MotionEvent.ACTION_POINTER_DOWN:
                mDoublePress.begin();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mDoublePress.cancel();
                break;
            case MotionEvent.ACTION_UP:
                mLastEvent = null;//avoids actions happening when switching but not touching
                mDoublePress.cancel();
                break;
        }

        //if has a handler handle event
        if (mHandler != null) {
            boolean result = mHandler.handle(event, mController, this);
            if (result)//if event returns true handler is done
                mHandler = null;
        } else {
            for (Element e : mController.getModel().getElements()) {
                if (e.getTouchHandler().handle(event, mController, this)) {
                    mHandler = e.getTouchHandler();
                    break;
                }
            }
        }
        return true;//take in all events
    }

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
}
