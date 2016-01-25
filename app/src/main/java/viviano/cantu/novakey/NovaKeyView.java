package viviano.cantu.novakey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.view.View;

import java.util.ArrayList;

import viviano.cantu.novakey.animations.animators.Animator;
import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.themes.Theme;
import viviano.cantu.novakey.utils.Util;

public class NovaKeyView extends View {
    //Theme
    public Theme theme;
	//Dimensions
	public NovaKeyDimen dimens;

	//animations
	private Paint p;
	private ArrayList<Drawer> drawers;
	private ArrayList<Animator> mAnimators;
	
	//candidates
	public boolean showCandidates = false;
	private float candidateHeight;

	private final Character[] returnAfterSpace = new Character[]
			{ '.', ',', ';', '&', '!', '?' };
	public boolean shouldReturnAfterSpace(Character c) {
		for (Character C  : returnAfterSpace) {
			if (C == c)
			    return true;
		}
		return false;
	}

	public NovaKeyView(Context context) {
		super(context);
		p = new Paint();
		drawers = new ArrayList<>();
        mAnimators = new ArrayList<>();

        theme = Settings.theme;
        updateDimens();
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		updateDimens();
        setMeasuredDimension((int)dimens.w, (int)dimens.h);
//		if (!context.undocked)
//			context.setInputView(this);
    }

	//update all
	public void updateDimens() {
		if (dimens == null)
			dimens = new NovaKeyDimen();
		dimens.w = getResources().getDisplayMetrics().widthPixels;
		updateRadius();
		updateCoords();
		dimens.h = (int)(Settings.sharedPref.getFloat("y" + (Controller.landscape ? "_land" : ""), dimens.r) + dimens.r);
		updateKeyLayout();
	}

	public void updateKeyLayout() {
		if (Controller.currKeyboard != null) {
			dimens.kl = Controller.currKeyboard;
			dimens.kl.updateCoords(dimens.x, dimens.y, dimens.r, dimens.sr);
		}
	}

	public void updateRadius() {
		setRadii(Settings.sharedPref.getFloat("size" + (Controller.landscape ? "_land" : ""), getResources().getDimension(R.dimen.default_radius)));
    }

	public void updateCoords() {
		setCoords(Settings.sharedPref.getFloat("x" + (Controller.landscape ? "_land" : ""), dimens.w / 2),
				dimens.r);
	}

    private void setRadii(float r) {
		dimens.r = r;
		dimens.sr = r / 3;
    }

	private void setCoords(float x, float y) {
		dimens.x = x;
		dimens.y = y;
	}

	
	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null)
            super.onDraw(canvas);

		theme.drawBackground(0, 0, dimens.w, dimens.h, dimens.x, dimens.y,
				dimens.r, dimens.sr, canvas);

        theme.drawBoard(dimens.x, dimens.y, dimens.r, dimens.sr, canvas);
		theme.drawButtons(dimens.x, dimens.y, dimens.r, canvas);

        switch (Controller.state & NovaKey.STATE_MASK) {
            case NovaKey.ON_KEYS:
            default:
                if (!((Controller.state & NovaKey.KEYS_MASK) == NovaKey.DEFAULT_KEYS &&
                        Settings.hideLetters || (Settings.hidePassword && Controller.onPassword)))
                    theme.drawKeys(dimens.x, dimens.y, dimens.r, dimens.sr, Controller.currKeyboard,
                            Controller.hasState(NovaKey.DEFAULT_KEYS)
                                    && Controller.hasState(NovaKey.CAPSED_LOCKED), canvas);
                break;
            case NovaKey.ROTATING:
                switch (Controller.state & NovaKey.ROTATING_MASK) {
                    case NovaKey.MOVING_CURSOR:
                        theme.drawCursorIcons(Controller.state, dimens.x, dimens.y, canvas);
                        break;
                    case NovaKey.INFINITE_MENU:
						theme.drawInfiniteMenu(Controller.infiniteMenu, dimens.x, dimens.y, dimens.r, dimens.sr, canvas);
                        break;
                }
                break;
            case NovaKey.ON_MENU:
                theme.drawOnUpMenu(Controller.onUpMenu, dimens.x, dimens.y, dimens.r, dimens.sr, canvas);
                break;
        }

		//draws all drawers
		for (int i=0; i<drawers.size(); i++) {
			drawers.get(i).onDraw(canvas);
		}
	}

	public void animate(Animator animator) {
        mAnimators.add(animator);
		animator.start(this);
	}

	public void clearDrawers() {
		drawers.clear();
		invalidate();
	}

    public void cancelAnimators() {
        clearDrawers();
        for (int i = 0; i < mAnimators.size(); i++) {
            mAnimators.get(i).cancel();
        }
        mAnimators.clear();
    }
	
	private void drawCandidates(float x, float y, Paint p, Canvas canvas) {
		String[] DELETE = new String[] { "candidates", "candidate", "candid" };
		
		float startAngle = (float)(-Math.PI * 2 / 5 * 2);
		for (int j=0; j<3; j++) {
			startAngle += Math.PI * 2 / 5;
			String s = DELETE[j];
			float textStartAngle = startAngle - p.measureText(s) / dimens.r / 2;
			
			int length = 0;
			for (int i=0; i<s.length(); i++) {
				length += p.measureText(s, i, i+1) / 2;
				
				float angle = textStartAngle + length / dimens.r;
				canvas.rotate((float) Math.toDegrees(angle), x, y);
				//p.setTextSize(textSize * (float)Math.cos(angle));
				canvas.drawText(s.substring(i, i + 1), x - p.measureText(s.substring(i, i + 1)) / 2, y - dimens.r, p);
				canvas.rotate((float) Math.toDegrees(-angle), x, y);
				
				length += p.measureText(s, i, i+1) / 2;
			}
		}
		p.setTextSize(/* textSize*/ 100);
	}
	
	/*
	 * will return the keyCode for the actions done
	 * or Keyboard.KEYCODE_CANCEL if invalid
	 */
	public int getKey(ArrayList<Integer> areasCrossed) {
		if (Controller.currKeyboard == null || areasCrossed.size() <= 0)
			return Keyboard.KEYCODE_CANCEL;
		//regular areas
		//gets first and last of list
		int firstArea = areasCrossed.get(0);
		//Inside circle
		if (firstArea >= 0) {
			int key = getGesture(areasCrossed);
			if (key != Keyboard.KEYCODE_CANCEL) {
				return key;
			}
			Location l = getLoc(areasCrossed);
			try { //makes sure is l checks out
				return Controller.currKeyboard.getCharKey(l.x, l.y);
			} catch (Exception e) {}		
		}
		return Keyboard.KEYCODE_CANCEL;
	}
	
	public int getGesture(ArrayList<Integer> areasCrossed) {
		int size = areasCrossed.size();
		if (size < 3 || size > 5)
			return Keyboard.KEYCODE_CANCEL;
		
		int first = areasCrossed.get(0), last = areasCrossed.get(size-1);
		boolean hasZero = areasCrossed.contains(0),
                hasThree = areasCrossed.contains(3);
		if (first == 2 && (hasZero || hasThree) && (last == 4 || last == 5))//swipe right
			return ' ';//SPACE
		if (first == 4 && (hasZero || hasThree) && last == 2)//swipe left
			return Keyboard.KEYCODE_DELETE;
		if ((first == 1 || first == 5) && hasZero && last == 3)//swipe down
			return '\n'; //ENTER
		if (first == 3 && hasZero && (last == 1 || last == 5))//swipe up
			return Keyboard.KEYCODE_SHIFT;
		return Keyboard.KEYCODE_CANCEL;
	}
	
	public Location getLoc(ArrayList<Integer> areasCrossed) {
		if (areasCrossed.size() <= 0)
			return null;
		//regular areas
		//gets first and last of list
		int firstArea = areasCrossed.get(0);
		int lastArea = areasCrossed.get(areasCrossed.size() > 1 ? areasCrossed.size() - 1 : 0);//sets to last or first if there is only one value
		int secondArea = areasCrossed.get(areasCrossed.size() > 1 ? 1 : 0);//sets to second value or first if there is only one value
		
		//Inside circle
		if (firstArea >= 0) {
			//loops twice checks first and last area first, then checks first and second area
			int check = lastArea;
			for (int i=0; i<2; i++) {
				if (firstArea == 0 && check >= 0)//center
					return new Location(0, check);
				else {
					if (firstArea == check)
						return new Location(firstArea, 0);
					else if (check == firstArea+1 || (firstArea == 5 && check == 1))
						return new Location(firstArea, 1);
					else if (check == 0)
						return new Location(firstArea, 2);
					else if (check == firstArea-1 || (firstArea == 1 && check == 5))
						return new Location(firstArea, 3);
				}
				check = secondArea;
			}
		}
		return null;
	}
	
	/*
	 * will return 0 if inside inner circle
	 * will return [1,5] depending on which area
	 * will return Keyboard.KEYCODE_CANCEL if not valid
	 */
	public int getArea(float x, float y) {
		if (Util.distance(dimens.x, dimens.y, x, y) <= dimens.sr) //inner circle
			return 0;
		else if (Util.distance(dimens.x, dimens.y, x, y) <= dimens.r)
			return getSector(x, y);
		return Keyboard.KEYCODE_CANCEL;
	}

	/*
	 * Will return a number [1, 5]
	 * representing which sector, the x and y is in
	 * returns Keyboard.KEYCODE_CANCEL if invalid
	 */
	public int getSectorFromCenter(float x, float y, float centX, float centY) {
		x -= centX;
		y = centY - y;
		double angle = Util.getAngle(x, y);
		angle = (angle < Math.PI / 2 ? Math.PI * 2 + angle : angle);//sets angle to [90, 450]
		for (int i=0; i < 5; i++) {
			double angle1 = (i * 2 * Math.PI) / 5  + Math.PI / 2;
			double angle2 = ((i+1) * 2 * Math.PI) / 5  + Math.PI / 2;
			if (angle >= angle1 && angle < angle2)
				return i+1;
		}
		return Keyboard.KEYCODE_CANCEL;
	}
	
	/*
	 * Will return a number [1, 5]
	 * representing which sector, the x and y is in
	 * returns Keyboard.KEYCODE_CANCEL if invalid
	 * use this with raw coords
	 */
	public int getSector(float x, float y) {
		return getSectorFromCenter(x, y, dimens.x, dimens.y);
	}

    public boolean onBtn(float x, float y, Btn btn) {
        return btn != null && btn.onBtn(x, y, dimens.x, dimens.y, dimens.r);
    }

    //if area is -1 returns between 5 & 1
    public float getAreaX(int area) {
        if (area > 0) {
            double a = (area-1) * (Math.PI * 2 / 5) + Math.PI / 2 + (Math.PI / 5);
            return (float)(dimens.x + Math.cos(a) * (dimens.sr + (dimens.r - dimens.sr) / 2));
        }
        return dimens.x;
    }

    //if area is -1 returns between 5 & 1
    public float getAreaY(int area) {
        if (area > 0) {
            double a = (area-1) * (Math.PI * 2 / 5) + Math.PI / 2 + (Math.PI / 5);
            return (float)(dimens.y - Math.sin(a) * (dimens.sr + (dimens.r - dimens.sr) / 2));
        }
        if (area == 0)
            return dimens.y;
        return dimens.y - (dimens.sr + (dimens.r - dimens.sr) / 2);
    }

	public void addDrawer(Drawer drawer) {
		drawers.add(drawer);
	}

	public void removeDrawer(Drawer drawer) {
		drawers.remove(drawer);
	}

	public interface Drawer {
		void onDraw(Canvas canvas);
	}
}







