package viviano.cantu.novakey;

import android.inputmethodservice.Keyboard;
import android.text.InputType;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;

import viviano.cantu.novakey.animations.animators.Animator;
import viviano.cantu.novakey.animations.animators.CharAnimator;
import viviano.cantu.novakey.animations.animators.CharGrow;
import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.emoji.Emoji;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.menus.OnUpMenu;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.themes.ThemeAuto;

/**
 * Created by Viviano on 7/10/2015.
 */
public class Controller implements NovaKeyListener.EventListener {

    public static int INPUT_UPDATE_SHIFT = 1, INPUT_FIX_WORD = 2, INPUT_AUTO_CORRECT = 4,
                        INPUT_FULL_CORRECT = 6, INPUT_IGNORE_SETTINGS = 8;

    private static NovaKey main;
    private static NovaKeyView view;
    private static Controller eventListener;

    //State
    public static int state, inputType;
    public static boolean onPassword = false;
    public static boolean landscape = false;

    public static KeyLayout currKeyboard;

    //menus
    public static InfiniteMenu infiniteMenu;
    public static OnUpMenu onUpMenu;

     public static void initialize(NovaKey main,  NovaKeyView view) {
         if (Controller.main == null)
            Controller.main = main;
         if (Controller.view == null) {
             Controller.view = view;
             Controller.eventListener = new Controller();
             Controller.view.setOnTouchListener(new NovaKeyListener(eventListener));
         }
    }
    public static NovaKeyView view() {
        return view;
    }
    public static void destroy() {
        main = null; view = null;
    }
    public static void addState(int flag) {
        int replace_mask = 0;
        for (int i=0; i<8; i++) {
            int testMask = 0xF * (int)Math.pow(0x10, i);
            if ((flag & testMask) == 0)
                replace_mask |= testMask;
        }
        state = (state & replace_mask)|flag;
    }
    public static void removeState(int mask) {
        int inverse = 0;
        for (int i=0; i<8; i++) {
            int curr = 0xF * (int)Math.pow(0x10, i);
            if ((mask & curr) == 0)
                inverse |= curr;
        }
        state &= inverse;
    }
    public static boolean hasState(int state) {
        int mask = 0;
        for (int i=0; i<8; i++) {
            int testMask = 0xF * (int)Math.pow(0x10, i);
            if ((state & testMask) != 0)
                mask |= testMask;
        }
        return (Controller.state & mask) == state;
    }

    //--------------------Main Lifecycle---------------------------------------------
    public static void onInputStart(EditorInfo info, boolean restarting) {
        //Update settings for theme and buttons
        Settings.update();
        view.theme = Settings.theme;
        if (view.theme instanceof ThemeAuto) {
            setAutoTheme(info.packageName);
        }

        //TODO: When not copying packages
        //main.copy(info.packageName);

        inputType = info.inputType;
        state = NovaKey.ON_KEYS|NovaKey.LOWERCASE;//will change depending
        setKeys(NovaKey.DEFAULT_KEYS);//set keys is used so that currKeyboard is not null
        onPassword = false;

        int var = inputType & InputType.TYPE_MASK_VARIATION,
                flags = inputType & InputType.TYPE_MASK_FLAGS;

        switch (info.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_TEXT:
                addState(NovaKey.DEFAULT_KEYS);
                if (var == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                    var == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                    var == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                    onPassword = true;
                break;
            case InputType.TYPE_CLASS_NUMBER:
                addState(NovaKey.PUNCTUATION);
                if (var == InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    onPassword = true;
                break;
            case InputType.TYPE_CLASS_DATETIME:
                addState(NovaKey.PUNCTUATION);
                break;
            case InputType.TYPE_CLASS_PHONE:
                addState(NovaKey.PUNCTUATION);
                break;
        }
        updateShift(info);
        view.updateDimens();
        view.clearDrawers();//drawers are never permanent
        view.invalidate();
        //start animation
        if (!restarting) {
            view.animate(new CharGrow(CharAnimator.UP).addDelay(100));
        }
    }

    public static void setAutoTheme(String pk) {
//        try {
//            PackageManager pm = main.getPackageManager();
//            ActivityManager am = (ActivityManager) main.getSystemService(Context.ACTIVITY_SERVICE);
//            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//            //ComponentName cn = am.getAppTasks().get(0).getTaskInfo().topActivity;
//
//            final Resources res = pm.getResourcesForApplication(pk);
//            final Resources.Theme theme = res.newTheme();
//            theme.applyStyle(pm.getActivityInfo(cn, 0).theme, false);
//
//            final TypedArray a = theme.obtainStyledAttributes(new int[]{
//                    res.getIdentifier("colorPrimary", "attr", pk),
//                    res.getIdentifier("color", "attr", pk),
//                    res.getIdentifier("colorAccent", "attr", pk),
//                    res.getIdentifier("colorControlHighLight", "attr", pk),
//                    res.getIdentifier("navigationColor", "attr", pk),
//                    res.getIdentifier("colorPrimaryDark", "attr", pk),
//
//                    R.attr.colorPrimary,
//                    R.attr.color,
//                    R.attr.colorAccent,
//                    R.attr.colorButtonNormal,
//                    R.attr.colorControlActivated,
//                    R.attr.colorControlHighlight,
//                    R.attr.colorControlNormal,
//                    R.attr.colorPrimaryDark,
//                    R.attr.colorSwitchThumbNormal,
//                    0x01010452//navigationbarColor
//            });
//
//            try {
//                Print.ln(pk);
//                for (int i = 0; i < a.length(); i++) {
//                    try {
//                        int c = a.getColor(i, Color.WHITE);
//                        Print.hex(c);
//                    } catch (Resources.NotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } finally {
//                a.recycle();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        ((ThemeAuto) view.theme).setCurrent(pk);
    }

    public static void setKeys(int keys) {
        addState(keys);
        switch (keys) {
            case NovaKey.DEFAULT_KEYS:
            default:
                //TODO: for now default keys are english
                currKeyboard = KeyLayout.get("English");
                break;
            case NovaKey.PUNCTUATION:
                currKeyboard = KeyLayout.get("Punctuation");
                break;
            case NovaKey.SYMBOLS:
                currKeyboard = KeyLayout.get("Symbols");
        }
        Settings.update();//recreates buttons
        view.updateKeyLayout();
    }

//    public static int INPUT_UPDATE_SHIFT = 1, INPUT_FIX_WORD = 2, INPUT_AUTO_CORRECT = 4,
//            INPUT_FULL_CORRECT = 6, INPUT_IGNORE_SETTINGS = 8;
    public static void input(Object o, int flags) {//TODO: flags
        if (o instanceof Character) {
            main.handleCharacter((int)(Character)o);
        }
        else if (o instanceof String) {
            main.handleText((String)o);
        }
        else if (o instanceof Emoji) {
            String res = ((Emoji) o).getValue();
            main.handleText(((Emoji) o).getValue());
        }
        //TODO: see if other types of objects like images can be inputed
        //if ((flags & INPUT_UPDATE_SHIFT) == INPUT_UPDATE_SHIFT)
        updateShift(main.getCurrentInputEditorInfo());
    }

    public static void toggleShift() {
        switch (state & NovaKey.KEYS_MASK) {
            default:
            case NovaKey.DEFAULT_KEYS:
                switch (state & NovaKey.SHIFT_MASK) {
                    case NovaKey.LOWERCASE:
                        addState(NovaKey.UPPERCASE);
                        currKeyboard.setShifted(true);
                        break;
                    case NovaKey.UPPERCASE:
                        addState(NovaKey.CAPSED_LOCKED);
                        break;
                    case NovaKey.CAPSED_LOCKED:
                        addState(NovaKey.LOWERCASE);
                        currKeyboard.setShifted(false);
                        break;
                }
                break;
            case NovaKey.PUNCTUATION:
                setKeys(NovaKey.SYMBOLS);
                break;
            case NovaKey.SYMBOLS:
                setKeys(NovaKey.PUNCTUATION);
                break;
        }
    }

    public static void updateShift(EditorInfo editorInfo) {
        if (hasState(NovaKey.DEFAULT_KEYS)) {
            switch (state & NovaKey.SHIFT_MASK) {
                case NovaKey.LOWERCASE:
                case NovaKey.UPPERCASE:
                    if (main.getCurrentCapsMode(editorInfo) != 0) {
                        currKeyboard.setShifted(true);
                        addState(NovaKey.UPPERCASE);
                    } else {
                        currKeyboard.setShifted(false);
                        eventListener.repeatingChar = Character.toLowerCase(eventListener.repeatingChar);
                        addState(NovaKey.LOWERCASE);
                    }
                    break;
                case NovaKey.CAPSED_LOCKED:
                    currKeyboard.setShifted(true);
                    break;
            }
        }
    }

    public static void setEditing(boolean editing) {
        if (editing) {
            NovaKeyEditView editView = new NovaKeyEditView(main);
            editView.theme = Controller.view().theme;
            main.setInputView(editView);
        }
        else {
            main.setInputView(view);
        }
    }

    public static void startInfiniteMenu(InfiniteMenu im, float x, float y) {
        if (im == null)
            return;
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        infiniteMenu = im;
        infiniteMenu.updateCoords(x, y);
        addState(NovaKey.ROTATING | NovaKey.INFINITE_MENU);
        view.invalidate();
    }

    //---------------------------------Relay Methods----------------------------
    public static void performClipboardAction(int action) {
        main.handleClipboardAction(action);
    }
    public static void vibrate(long milliseconds) { main.vibrate(milliseconds); }
    public static void animate(Animator animator) {
        if (view != null)
            view.animate(animator);
    }
    public static void clearDrawers() { view.clearDrawers(); }


///--------------------------------------------------------TOUCH CONTROLLER---------------------------------------------------------------
    public Controller() {}
    //deleting
    private ArrayList<Character> charsDeleted;
    private boolean deleteDone = false;
    //repeating
    private char repeatingChar;
    private int area1, area2;
    private boolean repeating, shouldFix, repeatingDone;

    @Override
    public void onRawAction(int action, float x, float y) {
        if (action == MotionEvent.ACTION_MOVE) {
            if (hasState(NovaKey.ROTATING|NovaKey.INFINITE_MENU) && infiniteMenu != null) {
                infiniteMenu.updateCoords(x, y);
                view.invalidate();
            }
            else if (hasState(NovaKey.ON_MENU) && onUpMenu != null) {
                onUpMenu.updateCoords(x, y);
                view.invalidate();
            }
        }
    }

    @Override
    public void onDownArea(int area) {
        //Controller.animate(new HintAnimator(area)); TODO:
        view.invalidate();
    }

    @Override
    public void onNewArea(int currArea, ArrayList<Integer> areasCrossed) {
        switch(state & NovaKey.STATE_MASK) {
            case NovaKey.ON_KEYS:
                main.vibrate(50);//TODO: use settings.vibrateLevel
                //Set Deleting
                if (view.getKey(areasCrossed) == Keyboard.KEYCODE_DELETE) {
                    addState(NovaKey.ROTATING | NovaKey.DELETING);
                    char c = main.handleDelete();
                    if (c != 0) {
                        charsDeleted = new ArrayList<Character>();
                        charsDeleted.add(c);
                    }
                    deleteDone = true;
                }
                else if (areasCrossed.size() == 3) {
                    //Set Repeating
                    if (areasCrossed.get(0) == areasCrossed.get(2)) {
                        ArrayList<Integer> sub = new ArrayList<Integer>(areasCrossed);
                        sub.remove(2);//remove last
                        repeatingChar = (char)view.getKey(sub);
                        repeating = true;
                        repeatingDone = true;
                        main.handleCharacter(repeatingChar);
                        //FOR OPENERS
                        if (Settings.quickClose && isOpener(repeatingChar)) {
                            main.handleCharacter(getCloser(repeatingChar));
                            main.moveSelection(-1, -1);
                            shouldFix = true;
                        }
                        else
                            main.handleCharacter(repeatingChar);

                        area1 = areasCrossed.get(0);
                        area2 = areasCrossed.get(1);
                    }
                    //Set Moving Cursor
                    else if (isRotating(areasCrossed))
                        addState(NovaKey.ROTATING | NovaKey.MOVING_CURSOR | NovaKey.CURSOR_BOTH);
                }
                //If Repeating
                else if (repeating) {
                    if (currArea == area1 || currArea == area2) {
                        if (Settings.quickClose && shouldFix) {
                            //delete char after first
                            main.getCurrentInputConnection().deleteSurroundingText(0, 1);
                            main.handleCharacter(repeatingChar);
                            main.handleCharacter(repeatingChar);
                        }
                        else
                            main.handleCharacter(repeatingChar);
                    }
                    else
                        repeating = false;
                    shouldFix = false;
                }
                break;
            case NovaKey.ROTATING:
                if (hasState(NovaKey.MOVING_CURSOR) && currArea == 0) {
                    //TODO: make this a timer to reduce human error
                    if (hasState(NovaKey.CURSOR_LEFT) || hasState(NovaKey.CURSOR_BOTH))
                        addState(NovaKey.ROTATING | NovaKey.MOVING_CURSOR | NovaKey.CURSOR_RIGHT);
                    else if (hasState(NovaKey.CURSOR_RIGHT))
                        addState(NovaKey.ROTATING | NovaKey.MOVING_CURSOR | NovaKey.CURSOR_LEFT);
                }
                break;
        }
        view.invalidate();
    }

    @Override
    public void onRotate(boolean clockwise, int currSector, boolean inCenter) {
        if ((state & NovaKey.STATE_MASK) == NovaKey.ROTATING) {
            switch (state & NovaKey.ROTATING_MASK) {
                case NovaKey.DELETING:
                    if (!deleteDone) {
                        if (!clockwise) {
                            char c = main.handleDelete();
                            if (c != 0)
                                charsDeleted.add(c);
                        } else if (charsDeleted.size() > 0)
                            main.handleCharacter(charsDeleted.remove(charsDeleted.size() - 1));
                    }
                    else
                        deleteDone = false;
                    break;
                case NovaKey.MOVING_CURSOR:
                    if (!inCenter) {
                        //These two checks should not use hasState!!!
                        int ds = 0, de = 0;
                        if ((state & NovaKey.CURSOR_LEFT) == NovaKey.CURSOR_LEFT)
                            ds = clockwise ? 1 : -1;
                        if ((state & NovaKey.CURSOR_RIGHT) == NovaKey.CURSOR_RIGHT)
                            de = clockwise ? 1 : -1;
                        main.moveSelection(ds, de);
                    }
                    break;
                case NovaKey.INFINITE_MENU:
                    if (infiniteMenu != null) {
                        if (clockwise)
                            infiniteMenu.down();
                        else
                            infiniteMenu.up();
                    }
                    break;
            }
        }
    }

    @Override
    public void onUp(int lastArea, ArrayList<Integer> areasCrossed) {
        int keyCode = view.getKey(areasCrossed);
        //animate(new ResetCharAnimator()); TODO:
        if (hasState(NovaKey.ON_KEYS) && !repeatingDone && keyCode != Keyboard.KEYCODE_CANCEL){
            if (keyCode != '\n' && keyCode >=0)
                main.handleCharacter(keyCode);
            else {
                if (keyCode == '\n')
                    main.handleEnter();
                else if (keyCode == Keyboard.KEYCODE_SHIFT)
                    toggleShift();
            }
        }
        else if (hasState(NovaKey.ROTATING|NovaKey.INFINITE_MENU) && infiniteMenu != null) {
            infiniteMenu.performSelection();
            infiniteMenu.reset();
            infiniteMenu = null;
        }
        else if (hasState(NovaKey.ON_MENU) && onUpMenu != null) {
            onUpMenu.action.onUp(lastArea);
            onUpMenu = null;
        }
        //reset state
        repeating = false;
        repeatingDone = false;//This method is used to stop inserting characters when it shouldn't
        addState(NovaKey.ON_KEYS);
        removeState(NovaKey.ROTATING_MASK | NovaKey.CURSOR_MASK);//TODO: may remove on case by case basis
        view.invalidate();
    }

    @Override
    public void onLongPress(int currArea, ArrayList<Integer> areasCrossed, float x, float y) {
        switch (state & NovaKey.STATE_MASK) {
            case NovaKey.ON_KEYS:
                if (areasCrossed.size() <= 2) {
                    //TODO: perform double vibrate or no vibrate when there is no infinite menu
                    char c = (char)view.getKey(areasCrossed);
                    for (InfiniteMenu im : InfiniteMenu.HIDDEN_KEYS) {
                        if (im.matches(c)) {
                            startInfiniteMenu(im, x, y);
                        }
                    }
                }
                break;
            case NovaKey.ROTATING:
                if (currArea == 0 && (state & NovaKey.ROTATING_MASK)==NovaKey.MOVING_CURSOR) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    onUpMenu = Clipboard.MENU;
                    onUpMenu.updateCoords(x, y);
                    addState(NovaKey.ON_MENU);
                    view.invalidate();
                }
                break;
            case NovaKey.ON_MENU:
                if (onUpMenu != null)
                    onUpMenu.action.onLongPress(currArea);
                break;
        }
    }

    @Override
    public void onTwoFingerLongPress() {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        setEditing(true);
    }

    @Override
    public void onBtnClick(Btn btn) {
        if (btn instanceof Btn.Clickable)
            ((Btn.Clickable) btn).onClicked();
    }

    @Override
    public void onBtnLongPress(final Btn btn, float x, float y) {
        if (btn instanceof Btn.LongPressable) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            ((Btn.LongPressable) btn).onLongPress();
        }
        if (btn instanceof Btn.InfiniteMenuable) {
            startInfiniteMenu(new InfiniteMenu(((Btn.InfiniteMenuable) btn).getInfiniteMenu(),
                    new InfiniteMenu.Action() {
                        @Override
                        public void onSelected(Object selected) {
                            ((Btn.InfiniteMenuable) btn).onItemSelected(selected);
                        }
                    }), x, y);
        }
    }

    //----------------------------------------HELPER METHODS-----------------------------
    //Closing Characters
    private static int[] openers = new int[] { '(', '[', '{', '<', '>', '|', '\"' },// the | is used for absolute
                         closers = new int[] { ')', ']', '}', '>', '<', '|', '\"' };//  value so its in
    private static int getCloser(int c) {
        for (int i=0; i<openers.length; i++) {
            if (openers[i] == c)
                return closers[i];
        }
        return -1;
    }
    private static boolean isOpener(int c) {
        for (int i : openers) {
            if (i == c)
                return true;
        }
        return false;
    }
    private static boolean isRotating(ArrayList<Integer> areasCrossed) {
        if (areasCrossed.size() == 3) {
            int one = areasCrossed.get(0);
            int two = areasCrossed.get(1);
            int three = areasCrossed.get(2);
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