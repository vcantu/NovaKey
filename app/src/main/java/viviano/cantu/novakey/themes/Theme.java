package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;

import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.Font;
import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.btns.BtnTheme;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.menus.OnUpMenu;
import viviano.cantu.novakey.settings.Settings;
import viviano.cantu.novakey.settings.widgets.pickers.PickerItem;

/**
 * Created by Viviano on 6/9/2015.
 */
public class Theme implements PickerItem {

    public Paint pBG, pB, pT;
    //must be used by calling the method
    private int primaryColor, secondaryColor, ternaryColor;
    private BtnTheme btnTheme;
    protected int[] textColors = new int[6];

    public Theme() {
        pBG = new Paint();
        pB = new Paint();
        pT = new Paint();

        pB.setFlags(Paint.ANTI_ALIAS_FLAG);//smooth edges and Never changes
        pT.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    //TO draw background
    public void drawBackground(float l, float t, float rt, float b,
                               float x, float y, float r, float sr, Canvas canvas) {
        pBG.setStyle(Paint.Style.FILL);
        pBG.setColor(primaryColor());
        canvas.drawRect(l, t, rt, b, pBG);
    }

    //To draw keyboard
    public void drawBoard(float x, float y, float r, float sr, Canvas canvas) {
        //prevents double drawing when alpha is lower than 255
        //TODO: make not draw back when minimalist and not undocked
        if (Color.alpha(primaryColor()) == 255)
            drawBoardBack(x, y, r, sr, canvas);

        pB.setShadowLayer(2, 0, 2, 0xFF808080);
        drawLines(x, y, r, sr, canvas);
        pB.clearShadowLayer();
    }

    //Override to change drawing
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        //draw background flat color
        pB.setColor(primaryColor());
        pB.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, r, pB);//main circle
    }
    //override to change drawing
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        //draw lines and circle
        pB.setColor(secondaryColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        Draw.shadedLines(x, y, r, sr, secondaryColor(), primaryColor(), pB, canvas);
    }
    private void drawLines(float x, float y, float r, float sr, Canvas canvas) {
        drawLines(x, y, r, sr, 1 / 72f, canvas);
    }

    public void drawButtons(float x, float y, float r, Canvas canvas) {
        for (int i=0; i<Settings.btns.size(); i++) {
            Settings.btns.get(i).draw(x, y, r, btnTheme, canvas);
        }
    }

    //To draw keys
    public void drawKeys(float x, float y, float r, float sr, KeyLayout keyboard, boolean capsed,
                         Canvas canvas) {
        pT.setStyle(Paint.Style.FILL);
        pT.setFlags(Paint.ANTI_ALIAS_FLAG);
        pT.setColor(ternaryColor());
        pT.setTextSize(r / (16 / 3));
        //TODO: different sizes
        if (capsed) {
            pT.setTypeface(Font.SANS_SERIF_CONDENSED);
        }
        else {
            pT.setTypeface(Font.SANS_SERIF_LIGHT);
        }
        keyboard.drawKeys(textColors, pT, canvas);
    }

    public void drawInfiniteMenu(InfiniteMenu infiniteMenu, float x, float y, float r, float sr, Canvas canvas) {
        infiniteMenu.draw(x, y, r, sr, textColor(), pT, canvas);
        if (textColor() != textColors()[0]) {
            Path p = new Path();
            p.addCircle(x, y, sr + 2, Path.Direction.CW);
            canvas.clipPath(p);
            infiniteMenu.draw(x, y, r, sr, textColors()[0], pT, canvas);
            canvas.restore();
        }
    }

    public void drawOnUpMenu(OnUpMenu onUpMenu, float x, float y, float r, float sr, Canvas canvas) {
        onUpMenu.draw(x, y, r, sr, this, canvas);
    }

    public void drawCursorIcons(int state, float x, float y, Canvas canvas) {
        pB.setColorFilter(new LightingColorFilter(textColors()[0], 0));

        Draw.bitmap(Icon.cursors, x, y, 1, pB, canvas);
        if ((state & NovaKey.CURSOR_RIGHT) == NovaKey.CURSOR_RIGHT)
            Draw.bitmap(Icon.cursorLeft, x, y, 1, pB, canvas);
        if ((state & NovaKey.CURSOR_LEFT) == NovaKey.CURSOR_LEFT)
            Draw.bitmap(Icon.cursorRight, x, y, 1, pB, canvas);

        pB.setColorFilter(null);
    }

    public void setColor(int prim, int sec, int ter) {
        primaryColor = prim;
        secondaryColor = sec;
        ternaryColor = ter;
        setTextColors();
        if (btnTheme == null)
            btnTheme = new BtnTheme(BtnTheme.NO_BACK, primaryColor(), buttonColor());
    }
    public void setBtnTheme(BtnTheme t) {
        btnTheme = t;
    }

    //since different areas may have different text color for contrast
    public void setTextColors() {
        for (int i=0; i<textColors.length; i++) {
            textColors[i] = textColor();
        }
    }

    public int primaryColor() { return primaryColor; }
    public int secondaryColor() { return secondaryColor; }
    public int ternaryColor() { return ternaryColor; }
    public int buttonColor() { return ternaryColor(); }
    public int textColor() { return ternaryColor(); }
    public int[] textColors() { return textColors; }

    /**
     * Draw method for the picker item
     *
     * @param x center x position
     * @param y center y position
     * @param dimen dimension equivalent to the maximum height
     * @param selected whether it is selected
     * @param index sub index of picker item
     * @param p paint used
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(float x, float y, float dimen, boolean selected, int index, Paint p, Canvas canvas) {
        float r = dimen / 2 * .8f;
        float sr = (dimen / 2 * .8f) / 3;
        setColor(0xFF616161, 0xFFF0F0F0, 0xFFF0F0F0);
        drawBoardBack(x, y, r, sr, canvas);
        drawLines(x, y, r, sr, 1 / 30f, canvas);

        if (selected) {
            p.setStyle(Paint.Style.STROKE);
            p.setColor(0xFF58ACFA);
            p.setStrokeWidth(15);
            canvas.drawCircle(x, y, r, p);
            p.setStrokeWidth(0);
            p.setStyle(Paint.Style.FILL);
        }
    }

    //static methods
    public static Theme getTheme(int i) {
        switch (i) {
            case 0:
            default:
                return new Theme();
            case 1:
                return new ThemeMaterial();
            case 2:
                return new ThemeSeparate();
            case 3:
                return new ThemeDonut();
            case 4:
                return new ThemeMulticolorDonut();
            case 5:
                return new ThemeMulticolor();
            case 6:
                return new ThemeAuto();
        }
    }

    public static int getIndex(Theme theme) {
        if (theme instanceof ThemeMaterial)
            return 1;
        if (theme instanceof ThemeSeparate)
            return 2;
        if (theme instanceof ThemeDonut)
            return 3;
        if (theme instanceof ThemeMulticolorDonut)
            return 4;
        if (theme instanceof ThemeMulticolor)
            return 5;
        if (theme instanceof ThemeAuto)
            return 6;
        return 0;
    }

    public static int COUNT = 7;

    public static Theme fromString(String s) {
        if (s.equals(Settings.DEFAULT)) {
            Theme res = new Theme();
            res.setColor(0xFF616161, 0xFFF5F5F5, 0xFFF5F5F5);
            return res;
        }
        String[] params = s.split(",");
        Theme res = getTheme(Integer.valueOf(params[0]));
        res.setColor(Integer.valueOf(params[1]),
                     Integer.valueOf(params[2]),
                     Integer.valueOf(params[3]));
        if (params.length >= 5 && params[4].equalsIgnoreCase("A")) {
            return new ThemeAuto(res);}
        return res;
    }
}
