package viviano.cantu.novakey;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import java.util.Locale;

import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.settings.Colors;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.themes.AppTheme;
import viviano.cantu.novakey.utils.Util;

public class NovaKey extends InputMethodService {
    //Statics
	public static String MY_PREFERENCES = "MyPreferences";

	public static int CB_COPY = 1, CB_SELECT_ALL = 2, CB_PASTE = 3, CB_DESELECT_ALL = 4, CB_CUT = 5;
    //flags are allocated like so:
    /*
        F -> Current state (on keys, rotating, on menu)
        F0 -> Current keyboard
        F00 -> current shift state
        F000 and greater, MetaData
     */
    public final static int
    STATE_MASK = 0xF,
    ON_KEYS = 0x1, KEYS_MASK = 0xF0,
        DEFAULT_KEYS = 0x10, PUNCTUATION = 0x20, SYMBOLS = 0x30, //TODO: add other languages
        SHIFT_MASK = 0xF00,
            LOWERCASE = 0x100, UPPERCASE = 0x200, CAPSED_LOCKED = 0x300,
    ROTATING = 0x2, ROTATING_MASK = 0xF000,
        DELETING = 0x1000, MOVING_CURSOR = 0x2000, INFINITE_MENU = 0x3000,
        CURSOR_MASK = 0xF0000,
                CURSOR_LEFT = 0x10000, CURSOR_RIGHT = 0x20000, CURSOR_BOTH = 0x30000,
    ON_MENU = 0x3;

	// Predictions
	private boolean predicting;
	private StringBuilder composing = new StringBuilder();

    //Services
	// haptic feedback
	private Vibrator vibrator;// Not Used Yet
	// copypaste
	private ClipboardManager clipboard;

	// Shift
	private boolean shouldReturn = false; // if the keyboard should return after a space
    //composing
    private int composingIndex = 0;

    //Closing Characters
    private int[] openers = new int[] { '¿', '¡', '⌊', '⌈' },
                  closers = new int[] { '?', '!', '⌋', '⌉' };
    private int getIndex( int c) {
        for (int i=0; i<openers.length; i++) {
            if (openers[i] == c)
                return i;
        }
        return -1;
    }
    private boolean isOpener(int c) {
        for (int i : openers) {
            if (i == c)
                return true;
        }
        return false;
    }

	// floating window
	public boolean undocked = false;
	private WindowManager wm;

	//When keyboard is created in settings
	@Override
	public void onCreate() {
		super.onCreate();
        //Services
		clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Clipboard.add(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
            }
        });
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        //create keyboards
        KeyLayout.CreateKeyboards(getResources());
        //create colors
        Colors.initialize();
		//load app themes
		AppTheme.load(getResources());
        //create fonts
        Font.create(this);
        //load icons
        Icon.load(this);
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

        initializeController();
	}

	@Override
	public View onCreateInputView() {
		//TODO: remove this if it lags
		AppTheme.load(getResources());

        initializeController();
        //stops it from crashing when the orientation changes
        if (Controller.view() != null) {
            ViewGroup parent = (ViewGroup) Controller.view().getParent();
            if (parent != null) {
                parent.removeView(Controller.view());
            }
        }

		return Controller.view();
	}

	@Override
	public void onBindInput() {
		//du stuff maybe?
	}


	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        initializeController();
		Controller.landscape =
				getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        Controller.onInputStart(info, restarting);
		// Reset State
		composing.setLength(0);

		predicting = true;
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
		if (ic == null || Controller.onPassword)// or keyInserted?
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (undocked) {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH)
                close();
        }
        return super.onKeyDown(keyCode, event);
    }

    //TODO: SHOULD UNDOCKED BE VIEW
    private void open() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        wm.addView(Controller.view(), params);
    }

	private void close() {
		try { wm.removeView(Controller.view()); }
		catch (Exception e) {}
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		Controller.landscape =
				getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		super.onConfigurationChanged(config);
	}

	@Override
	public boolean onEvaluateFullscreenMode() {
		return false;
	}

	@Override
	public void onFinishInput() {
		commitComposing();
        super.onFinishInput();
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Controller.destroy();
    }

    private void initializeController() {
		Controller.initialize(this, new NovaKeyView(this));
    }

	//-----------------------------------------helper methods---------------------------------------//

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

	/*
    Deletes the character right before, and returns the character it just deleted,
    If there is nothing to delete it will return 0;
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

		if (composing.length() > 0 && composingIndex >= composing.length()) {
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
		Controller.updateShift(getCurrentInputEditorInfo());
		return result;
	}

	public void handleEnter() {
		InputConnection ic = getCurrentInputConnection();
		if (ic == null)
			return;
		//TODO: not everything should enter
		EditorInfo ei = getCurrentInputEditorInfo();		
		if ((ei.inputType & EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE) != 0)
			handleCharacter('\n');
		else
			ic.performEditorAction(getCurrentInputEditorInfo().imeOptions & EditorInfo.IME_MASK_ACTION);
		// requirements
		shouldReturn = false;
		Controller.updateShift(getCurrentInputEditorInfo());
	}

    public void handleText(String text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null)
            return;

        ic.finishComposingText();
        composing.setLength(0);
        ic.commitText(text, 0);
    }

	public void handleCharacter(int keyCode) {
		InputConnection ic = getCurrentInputConnection();
		if (ic == null)
			return;
		// if charachter
		if (keyCode != ' ') {
			// if predicting and is a letter add to composing
			if (predicting && !Controller.onPassword && (Character.isLetter(keyCode) || Util.isNumber(keyCode))) {
				//if cusor at the end of composing
				if (composingIndex >= composing.length()) {
					composing.append((char) keyCode);
					ic.setComposingText(composing, 1);
					composingIndex = composing.length();
				}
				//if cursor is not at the end of composing
				else {
					commitComposing();
					ic.commitText(Character.toString((char) keyCode), 1);
					//then the updateSelection() will update composing and composingIndex
				}
			}// if the character is a quote then handle it
			else if (!Controller.onPassword && Settings.quickInsert && isOpener(keyCode)) {
				commitComposing();
                int i = ic.getExtractedText(new ExtractedTextRequest(), 0).selectionStart + 1;
				ic.commitText(String.valueOf((char)keyCode) +
							  String.valueOf((char)closers[getIndex(keyCode)]), 1);
				ic.setSelection(i, i);
			}
			// else just send character
			else {
				commitComposing();
				ic.commitText(Character.toString((char) keyCode), 1);
			}
			// handle returning to letters with special characters
            //TODO: this is dumb should be in controller
			shouldReturn = Controller.view().shouldReturnAfterSpace((char) keyCode); //set should return after space
			//these characters return right away
			if (keyCode == '\'' || keyCode == '"' || keyCode == '¿' || keyCode == '¡')
                Controller.setKeys(DEFAULT_KEYS);
		}// if space
		else {
			commitComposing();
			ic.commitText(" ", 0);
			//ic.commitText("\uD83D\uDE00", 0);
			if (shouldReturn)
                Controller.setKeys(DEFAULT_KEYS);
			shouldReturn = false;
		}
		Controller.updateShift(getCurrentInputEditorInfo());
	}

    public void setSelection(int selectionStart, int selectionEnd) {
        commitComposing();
		//must set selection start first, just in case because of weird bug when user edits cursor it
		// 			can select more to the right but nor the left
		getCurrentInputConnection().setSelection(selectionStart, selectionStart);
        getCurrentInputConnection().setSelection(selectionStart, selectionEnd);
    }
    public void moveSelection(int dSelectionStart, int dSelectionEnd) {
		try {
			ExtractedText et = getCurrentInputConnection()
					.getExtractedText(new ExtractedTextRequest(), 0);
			int s = et.selectionStart + dSelectionStart, e = et.selectionEnd + dSelectionEnd;
			s = s < 0 ? 0 : s;
			e = e < 0 ? 0 : e;
			if (s <= e)
				setSelection(s, e);
			else {
				Controller.addState(ROTATING | MOVING_CURSOR | CURSOR_LEFT);
				setSelection(e, s);
			}
		} catch (Exception e) {
		}
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
                    handleText(text);
            }
			// select all
			else if (action == CB_SELECT_ALL) {
                int end = eText.text.length();
				setSelection(0, end);
            }
			// deselect all
			else if (action == CB_DESELECT_ALL) {
                int i = (Controller.state & CURSOR_LEFT) == CURSOR_LEFT ?
                        eText.selectionEnd : eText.selectionStart;
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
		h.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, length).show();
			}
		});
	}

    //vibrates if settings allow it
    public void vibrate(long milliseconds) {
        if (Settings.vibrate)
            vibrator.vibrate(milliseconds);
    }

	private void commitComposing() {
		InputConnection ic = getCurrentInputConnection();
		if (ic == null)
			return;

        //AutoCorrect
		if (Settings.autoCorrect && !Controller.onPassword) {
			int i = Util.isContraction(composing, getResources());
			if (i != -1) {
				String s = getResources().getStringArray(R.array.contractions)[i];
				if (Controller.hasState(CAPSED_LOCKED))
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