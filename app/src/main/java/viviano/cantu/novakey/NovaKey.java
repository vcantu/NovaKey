package viviano.cantu.novakey;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.SelectionActions;
import viviano.cantu.novakey.controller.actions.typing.InputAction;
import viviano.cantu.novakey.controller.actions.typing.UpdateShiftAction;
import viviano.cantu.novakey.view.drawing.Font;
import viviano.cantu.novakey.view.drawing.Icons;
import viviano.cantu.novakey.elements.menus.InfiniteMenu;
import viviano.cantu.novakey.model.states.ShiftState;
import viviano.cantu.novakey.model.keyboards.KeyLayout;
import viviano.cantu.novakey.settings.Colors;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.themes.AppTheme;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.view.INovaKeyView;
import viviano.cantu.novakey.view.NovaKeyView;

public class NovaKey extends InputMethodService {

    //Statics
	public static String MY_PREFERENCES = "MyPreferences";
	public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

	public static int CB_COPY = 1, CB_SELECT_ALL = 2, CB_PASTE = 3, CB_DESELECT_ALL = 4, CB_CUT = 5;

	public StringBuilder composing = new StringBuilder();//TODO: move fields to controller
    public int composingIndex = 0;

    //Services
	private Vibrator vibrator;
	private ClipboardManager clipboard;


	private boolean shouldReturn = false; // if the keyboard should return after a space


	private WindowManager windowManager;
    private List<View> mWindows;

    private Controller mController;

    public void createController() {
        mController = new Controller(this);
    }

	/**
	 * Called when the keyboard is enabled
	 */
	@Override
	public void onCreate() {
		getApplicationContext().setTheme(R.style.AppTheme);
		super.onCreate();
        //Services
		clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(() -> {
            try {
                Clipboard.add(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
            } catch (NullPointerException e) {}
        });
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mWindows = new ArrayList<>();

        //create keyboards
        KeyLayout.CreateKeyboards(getResources());
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
        Settings.setSharedPref(PreferenceManager.getDefaultSharedPreferences(this));

        //Shared Preferences for setup activity //TODO: change this
		Editor temp = getApplicationContext().getSharedPreferences(NovaKey.MY_PREFERENCES, MODE_PRIVATE).edit();
		temp.putBoolean("has_setup", true);
		temp.commit();

	}

	@Override
	public View onCreateInputView() {
		AppTheme.load(this, getResources());
        createController();//creates view if it has not been created
//        //stops it from crashing when the orientation changes
//        if (Controller.view() != null) {
//            ViewGroup parent = (ViewGroup) Controller.view().getParent();
//            if (parent != null) {
//                parent.removeView(Controller.view());
//            }
//        }

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
        createController();
        mController.getTrueModel().updateInputState(info);//updates editor info
		// Reset State
		composing.setLength(0);
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

	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd,
			int newSelStart, int newSelEnd, int candidatesStart,
			int candidatesEnd) {
		InputConnection ic = getCurrentInputConnection();
		if (ic == null || mController.getTrueModel()
                .getInputState().onPassword())// or keyInserted?
			return;
		//set composing region
		if (newSelStart == newSelEnd && oldSelEnd != newSelStart) {
			try {
				String text = ic.getExtractedText(new ExtractedTextRequest(), 0).text.toString();
				int s, e;
				for (s=newSelStart-1; s>=0; s--) {
					char c;
					try { c = text.charAt(s); }
					catch (Exception x) { s++; break;}
					if (!Character.isLetter(c) && !Util.isNumber(c) && c != '\'')
					{ s++; break; }
				}
				for (e=newSelStart; e<text.length(); e++) {
					char c = text.charAt(e);
					if (!Character.isLetter(c) && !Util.isNumber(c) && c != '\'')
						break;
				}
				try {
					if (s < 0)
						s=0;
					ic.setComposingRegion(s, e);
					composing.replace(0, composing.length(), text.substring(s, e));
				}
				catch (Exception x) { composing.setLength(0); }//TODO: did this
				composingIndex = newSelStart - s;
				}
			catch (Exception e) { composing.setLength(0); }//TODO: did this too
		}
		else if (newSelStart != newSelEnd) {
			ic.finishComposingText();
			composing.setLength(0);
		}
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
		commitComposing();
        super.onFinishInput();
	}

	//-----------------------------------------helper methods---------------------------------------//

    /**
     * @return the text inside the current selection or an empty string if none
     */
    public String getSelectedText() {
        try {
            return getCurrentInputConnection().getSelectedText(0).toString();
        } catch (Exception e) {
            //if input connection or selected text is null
            return "";
        }
    }

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

	/**
	 * Deletes the character right before, and returns the character it just deleted,
	 * If there is nothing to delete it will return 0;
 	*/
	public char handleDelete() {
		// add deleted character to temporary memory so it can be added
		InputConnection ic = getCurrentInputConnection();
        if (ic == null)
            return (char)0;

		char result = 0;
		CharSequence c = ic.getTextBeforeCursor(1, 0);
		if (c != null && c.length() > 0) {
			result = c.charAt(0);
		}

        //if composing isnt blank &
		if (composing.length() > 0 && composingIndex >= composing.length() && false) {
			if (composing.length() > 1) {
				composing.deleteCharAt(composing.length() - 1);
				ic.setComposingText(composing, 1);
			} else {
				composing.setLength(0);
				ic.commitText("", 0);
			}
			//TODO: update candidates
		} else {
			ic.finishComposingText();
			composing.setLength(0);
			ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
		}
		mController.fire(new UpdateShiftAction());
		return result;
	}

    public void handleClipboardAction(int action) {
        try {//try statement because clip could be empty or selection could be empty
            InputConnection ic = getCurrentInputConnection();
            ExtractedText eText = ic.getExtractedText(new ExtractedTextRequest(), 0);
			// copy/cut
            if (action == CB_COPY || action == CB_CUT) {
                String text = (String) ic.getSelectedText(0);
                if (copy(text)) {
                    // cut
                    if (action == CB_CUT) {
                        ic.finishComposingText();
                        composing.setLength(0);
                        ic.commitText("", 0);
                    }
                }
				showToast("Text Copied", Toast.LENGTH_SHORT);
			}
			// paste
			else if (action == CB_PASTE) {
                String text = clipboard.getPrimaryClip()
                        .getItemAt(clipboard.getPrimaryClip().getItemCount()-1)
                        .getText().toString();
                if (text != null)
                    mController.fire(new InputAction(text));
            }
			// select all
			else if (action == CB_SELECT_ALL) {
                int end = eText.text.length();
                mController.fire(new SelectionActions.Set(0, end));
            }
			// deselect all
			else if (action == CB_DESELECT_ALL) {
                int i = mController.getTrueModel().getCursorMode() <= 0
                        ? eText.selectionEnd : eText.selectionStart;
                    ic.setSelection(i, i);
            }
        } catch (Exception e) {}
    }

    //Returns true if copy was successful
    public boolean copy(String text) {
        if (text.length() > 0) {
            ClipData cd = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(cd);
            return true;
        }
        return false;
    }

	public void showToast(final String message, final int length) {
		final Context context = this;
		Handler h = new Handler(this.getMainLooper());
		h.post(() -> Toast.makeText(context, message, length).show());
	}

    //vibrates if settings allow it
    public void vibrate(long milliseconds) {
        if (Settings.vibrate)
            vibrator.vibrate(milliseconds);
    }

	public void commitComposing() {
		InputConnection ic = getCurrentInputConnection();
		if (ic == null)
			return;

        //AutoCorrect
		if (Settings.autoCorrect && !mController.getTrueModel().getInputState().onPassword()) {
			int i = Util.isContraction(composing, getResources());
			if (i != -1) {
				String s = getResources().getStringArray(R.array.contractions)[i];
				if (mController.getTrueModel().getShiftState() == ShiftState.CAPS_LOCKED)
					s = s.toUpperCase(Locale.US);//TODO: other languages
				else if (Character.isUpperCase(composing.charAt(0)))
					s = Util.capsFirst(s);
				composing.replace(0, composing.length(), s);
				ic.setComposingText(composing, 1);
			}
		}
		ic.finishComposingText();
		composing.setLength(0);
	}
}