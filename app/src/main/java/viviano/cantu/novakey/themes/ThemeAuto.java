package viviano.cantu.novakey.themes;

import android.graphics.Canvas;

import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.settings.Settings;

/**
 * Created by Viviano on 6/8/2015.
 */
public class ThemeAuto extends BaseTheme {

    public String currPack;
    private BaseTheme theme;

    public ThemeAuto() {
        theme = BaseTheme.fromString(Settings.DEFAULT);
    }
    public ThemeAuto(BaseTheme defaultTheme) {
        theme = defaultTheme;
    }


    public void setCurrent(String pk) {
        currPack = pk;
        //set theme from here
        BaseTheme t = AppTheme.fromPk(pk);
        if (t != null)
            theme = t;

//        else if (pk.equals("com.google.android.apps.maps")) {
//        }
//        else if (pk.equals("com.google.android.apps.docs.editors.docs")) {
//            theme = new ThemeDonut();
//            theme.setColor(0xffffffff, 0xff4285F4, 0xffffffff);
//        }
//        else if (pk.equals("com.google.android.apps.docs.editors.sheets")) {
//            theme = new ThemeDonut();
//            theme.setColor(0xffffffff, 0xff0F9D58, 0xffffffff);
//        }
//        else if (pk.equals("com.google.android.apps.docs.editors.slides")) {
//            theme = new ThemeDonut();
//            theme.setColor(0xffffffff, 0xffF4B400, 0xffffffff);
//        }
//        else if (pk.equals("com.google.android.apps.docs")) {
//            theme = new ThemeMulticolorDonut();
//            theme.setColor(0xFFffffff, 0xff757575, 0xFFffffff);
//            ((ThemeMulticolorDonut)theme).setColors(new int[]{
//                    0xff0F9D58, 0xff0F9D58, 0xff4285F4, 0xffF4B400, 0xffF4B400
//            });
//        }
    }

    @Override
    public void drawBackground(float l, float t, float rt, float b,
                               float x, float y, float r, float sr, Canvas canvas) {
        theme.drawBackground(l, t, rt, b, x, y, r, sr, canvas);
    }
    @Override
    public void drawBoard(float x, float y, float r, float sr, Canvas canvas) {
        theme.drawBoard(x, y, r, sr, canvas);
    }
    @Override
    public void drawButtons(float x, float y, float r, Canvas canvas) {
        theme.drawButtons(x, y, r, canvas);
    }
    @Override
    public void drawKeys(float x, float y, float r, float sr, KeyLayout keyboard, boolean capsed,
                         Canvas canvas) {
        theme.drawKeys(x, y, r, sr, keyboard, capsed, canvas);
    }

    @Override
    public int primaryColor() { return theme.primaryColor(); }
    @Override
    public int secondaryColor() { return theme.secondaryColor(); }
    @Override
    public int ternaryColor() { return theme.ternaryColor(); }
    @Override
    public int textColor() { return theme.textColor(); }
    @Override
    public int[] textColors() { return theme.textColors(); }
    @Override
    public int buttonColor() { return theme.buttonColor(); }

}

