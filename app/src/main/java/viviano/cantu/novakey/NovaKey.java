package viviano.cantu.novakey;

import android.content.ClipboardManager;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.inputmethodservice.InputMethodService;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.view.drawing.Font;
import viviano.cantu.novakey.view.drawing.Icons;
import viviano.cantu.novakey.model.elements.menus.InfiniteMenu;
import viviano.cantu.novakey.settings.Colors;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.themes.AppTheme;

public class NovaKey extends InputMethodService {

    //Statics
	public static String MY_PREFERENCES = "MyPreferences";
	public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    //Services
	private Vibrator vibrator;
	private ClipboardManager clipboard;
	private WindowManager windowManager;
    private List<View> mWindows;

    private Controller mController;

	/**
	 * Called when the keyboard is enabled
	 */
	@Override
	public void onCreate() {
		getApplicationContext().setTheme(R.style.AppTheme);
		super.onCreate();

        //Phone Services
		clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(() -> {
            try {
                Clipboard.add(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
            } catch (NullPointerException e) {}
        });
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindows = new ArrayList<>();

        //create colors
        Colors.initialize();
		//load app themes
		AppTheme.load(this, getResources());
        //create fonts
        Font.create(this);
        //load icons
        Icons.load(this);
		//load emojis
		//Emoji.load(this);
        //Create Hidden Keys
        InfiniteMenu.setHiddenKeys(getResources().getStringArray(R.array.hidden_keys));
        //Create Clipboard Menu
        Clipboard.createMenu();
        //Initialize settings
        Settings.setPrefs(PreferenceManager.getDefaultSharedPreferences(this));
        Settings.update();

        //Controller creation
        mController = new Controller(this);

        //Shared Preferences for setup activity //TODO: change this
		Editor temp = getApplicationContext().getSharedPreferences(NovaKey.MY_PREFERENCES, MODE_PRIVATE).edit();
		temp.putBoolean("has_setup", true);
		temp.commit();

	}

	@Override
	public View onCreateInputView() {
		AppTheme.load(this, getResources());

		return mController.getView();
	}

    public void addWindow(View view, boolean fullscreen) {
		WindowManager.LayoutParams params= new WindowManager.LayoutParams(
                fullscreen ? WindowManager.LayoutParams.MATCH_PARENT :
                        WindowManager.LayoutParams.WRAP_CONTENT,
                fullscreen ? WindowManager.LayoutParams.MATCH_PARENT :
                        WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        mWindows.add(view);
        windowManager.addView(view, params);
    }

    public void clearWindows() {
        for (int i=0; i<mWindows.size(); i++) {
            windowManager.removeView(mWindows.remove(i));
        }
    }


	@Override
	public void onBindInput() {
		//du stuff maybe?
	}


	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        mController.getModel().onStart(info, getCurrentInputConnection());//updates editor info
	}

	@Override
	public void setCandidatesViewShown(boolean status) {
        //TODO: this is how it was
//		if (InputView == null)
//			return;
//		InputView.showCandidates = status;
//		InputView.resizeView();
//		setInputView(InputView);
	}

	//TODO: move to input state
	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd,
			int newSelStart, int newSelEnd, int candidatesStart,
			int candidatesEnd) {
		mController.getModel().getInputState().updateSelection(newSelStart, newSelEnd);
	}

//TODO: this code is for undocking
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (undocked) {
//            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH)
//                close();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    //TODO: SHOULD UNDOCKED BE VIEW
//    private void open() {
//        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        windowManager.addView(Controller.view(), params);
//    }
//
//	private void close() {
//		try { windowManager.removeView(Controller.view()); }
//		catch (Exception e) {}
//	}

	/**
	 * Makes it never go on Fullscreen Mode
	 *
	 * @return false always
	 */
	@Override
	public boolean onEvaluateFullscreenMode() {
		return false;
	}

	/**
	 * Commits the composing text
	 */
	@Override
	public void onFinishInput() {
		//TODO: finish input
        super.onFinishInput();
	}

	public ClipboardManager getClipboard() {
		return clipboard;
	}

	//-----------------------------------------helper methods---------------------------------------//

	//TODO: move to inputState
    public int getCurrentCapsMode(EditorInfo editorInfo) {
        InputConnection ic = getCurrentInputConnection();
        //if null caps if needed only
        if (ic == null) {
            return (editorInfo.inputType & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT &&
                   (editorInfo.inputType & 0x4000) == 0 ? 0 : 1;
        }
		else
            return getCurrentInputConnection().getCursorCapsMode(editorInfo.inputType);
    }

    //TODO: make action
    public void vibrate(long milliseconds) {
        if (Settings.vibrate)
            vibrator.vibrate(milliseconds);
    }
}