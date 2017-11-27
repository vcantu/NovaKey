package viviano.cantu.novakey.utils;

import android.os.CountDownTimer;

/**
 * Created by viviano on 11/26/2017.
 */

public class CustomTimer {

    private CountDownTimer mTimer;
    private Runnable mEvent;
    private long mTime;

    public CustomTimer(long milliseconds, final Runnable event) {
        mEvent = event;
        mTime = milliseconds;
    }

    public void begin() {
        mTimer = new CountDownTimer(mTime, mTime) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                mEvent.run();
            }
        }.start();
    }

    public void cancel() {
        if (mTimer != null)
            mTimer.cancel();
    }

    public void reset() {
        cancel();
        begin();
    }
}
