package viviano.cantu.novakey.view;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.view.View;

import java.util.ArrayList;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.Location;
import viviano.cantu.novakey.animations.animators.Animator;
import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.model.DrawModel;
import viviano.cantu.novakey.model.NovaKeyModel;
import viviano.cantu.novakey.model.ShiftState;
import viviano.cantu.novakey.model.StateModel;
import viviano.cantu.novakey.model.ViewModel;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.themes.Theme;
import viviano.cantu.novakey.utils.Util;

public class NovaKeyView extends View {

	private StateModel mModel;
	private ViewModel mViewModel;
	//animations
	private ArrayList<Drawer> drawers;
	private ArrayList<Animator> mAnimators;

	public NovaKeyView(Context context) {
		super(context);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		drawers = new ArrayList<>();
        mAnimators = new ArrayList<>();
	}

	public void setModel(NovaKeyModel model) {
		mModel = model;
		mViewModel = new ViewModel((DrawModel) mModel);//ONLY CAST HERE
	}


	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mViewModel.getWidth(), mViewModel.getHeight());
    }

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null)
            super.onDraw(canvas);

		Theme mTheme = mViewModel.getTheme();
		float w = mViewModel.getWidth(), h = mViewModel.getHeight();
		float x = mViewModel.getX(), y = mViewModel.getY();
		float r = mViewModel.getRadius(), sr = mViewModel.getSmallRadius();

		mTheme.drawBackground(0, 0, w, h, x, y,
				r, sr, canvas);

        mTheme.drawBoard(x, y, r, sr, canvas);
		//TODO: mTheme.drawButtons(dimens, canvas);

        switch (mModel.getUserState()) {
			case TYPING:
            default:
                if (!(mModel.getKeyboardCode() > 0  && //on an alphabet
                        Settings.hideLetters || (Settings.hidePassword && Controller.onPassword)))
                    mTheme.drawKeys(x, y, r, sr, mModel.getKeyboard(),
                            mModel.getShiftState() == ShiftState.CAPS_LOCKED, canvas);
                break;
			case SELECTING:
				mTheme.drawCursorIcon(mModel.getCursorMode(), x, y, sr, canvas);
				break;
			case ON_INFINITE_MENU:
				mTheme.drawInfiniteMenu(mModel.getInfiniteMenu(), x, y,
						r, sr, canvas);
				break;
			case ON_UP_MENU:
                mTheme.drawOnUpMenu(mModel.getOnUpMenu(), x, y, r, sr,
						canvas);
                break;
        }

		//draws all drawers
		for (int i=0; i<drawers.size(); i++) {
			drawers.get(i).onDraw(canvas);
		}
	}

	public void animate(Animator animator) {
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

	/*
	 * will return the keyCode for the actions done
	 * or Keyboard.KEYCODE_CANCEL if invalid
	 */
	public int getKey(ArrayList<Integer> areasCrossed) {
		if (areasCrossed.size() <= 0)
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
				return mModel.getKeyboard().getLowercase(l.x, l.y);
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
                    else if (check == -1 && areasCrossed.size() == 2)
                        return new Location(firstArea, 4);
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
		if (Util.distance(mViewModel.getX(), mViewModel.getY(), x, y) <= mViewModel.getSmallRadius()) //inner circle
			return 0;
		else if (Util.distance(mViewModel.getX(), mViewModel.getY(), x, y) <= mViewModel.getRadius())
			return getSector(x, y);
		return Keyboard.KEYCODE_CANCEL;//outside area
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
		return getSectorFromCenter(x, y, mViewModel.getX(), mViewModel.getY());
	}

    public boolean onBtn(float x, float y, Btn btn) {
        return btn != null && btn.onBtn(x, y,
				mViewModel.getX(), mViewModel.getY(), mViewModel.getRadius());
    }

    //if area is -1 returns between 5 & 1
    public float getAreaX(int area) {
        if (area > 0) {
            double a = (area-1) * (Math.PI * 2 / 5) + Math.PI / 2 + (Math.PI / 5);
            return (float)(mViewModel.getX() + Math.cos(a) *
					(mViewModel.getSmallRadius() +
							(mViewModel.getRadius() - mViewModel.getSmallRadius()) / 2));
        }
        return mViewModel.getX();
    }

    //if area is -1 returns between 5 & 1
    public float getAreaY(int area) {
        if (area > 0) {
            double a = (area-1) * (Math.PI * 2 / 5) + Math.PI / 2 + (Math.PI / 5);
            return (float)(mViewModel.getY() - Math.sin(a) *
					(mViewModel.getSmallRadius() +
							(mViewModel.getRadius() - mViewModel.getSmallRadius()) / 2));
        }
        if (area == 0)
            return mViewModel.getY();
        return mViewModel.getY() - (mViewModel.getSmallRadius() +
				(mViewModel.getRadius() - mViewModel.getSmallRadius()) / 2);
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







