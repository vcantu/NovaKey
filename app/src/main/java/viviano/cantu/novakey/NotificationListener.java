package viviano.cantu.novakey;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Viviano on 7/3/2015.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService{
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();context = getApplicationContext();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("Notification", sbn.toString());
        String pack = sbn.getPackageName();

        String ticker = "null";
        try {
            ticker = sbn.getNotification().tickerText.toString();
        } catch (Exception e) {}
        String title = "null", text = "null", big_text = "null", info_text = "null",
                sub_text = "null", summary_text = "null", text_lines = "null";
        try {
            Bundle extras = sbn.getNotification().extras;
            title = extras.getString(Notification.EXTRA_TITLE);
            text = extras.getCharSequence(Notification.EXTRA_TEXT).toString();
//            big_text = extras.getCharSequence(Notification.EXTRA_BI).toString(); dont exist
            info_text = extras.getCharSequence(Notification.EXTRA_INFO_TEXT).toString();
            sub_text = extras.getCharSequence(Notification.EXTRA_SUB_TEXT).toString();
            summary_text = extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT).toString();
            text_lines = extras.getCharSequence(Notification.EXTRA_TEXT_LINES).toString();
        } catch (Exception e) {}

//        Log.i("Package", pack);
//        Log.i("Ticker",ticker);
//        Log.i("Title",title);
//        Log.i("Text",text);
//        Log.i("Info Text",info_text);
//        Log.i("Sub Text",sub_text);
//        Log.i("Summary Text",summary_text);
//        Log.i("Text Lines",text_lines);


//        Intent msgrcv = new Intent("Msg");
//        msgrcv.putExtra("package", pack);
//        msgrcv.putExtra("ticker", ticker);
//        msgrcv.putExtra("title", title);
//        msgrcv.putExtra("text", text);
//
//        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");
    }
}
