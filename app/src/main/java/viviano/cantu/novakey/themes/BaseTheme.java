package viviano.cantu.novakey.themes;

import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;

import viviano.cantu.novakey.NovaKeyDimen;
import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.Font;
import viviano.cantu.novakey.drawing.Icons;
import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.btns.BtnTheme;
import viviano.cantu.novakey.menus.InfiniteMenu;
import viviano.cantu.novakey.menus.OnUpMenu;
import viviano.cantu.novakey.settings.Settings;

/**
 * Created by Viviano on 6/9/2015.
 */
public class BaseTheme implements Theme {

    /**
     * @return Its name identifier, if it inherits from another theme
     * it must include its parents name in the format ParentName.ThisName
     */
    @Override
    public String themeName() {
        return "Theme";
    }

    /**
     * @return an integer ID unique to this theme
     */
    @Override
    public int themeID() {
        return 0;
    }

    public final Paint pBG, pB, pT;
    //INVARIANT only its corresponding methods can reference or set them
    private int mPrimaryColor, mAccentColor, mContrastColor;

    private BtnTheme btnTheme;
    protected int[] textColors = new int[6];

    private boolean mIsAuto = false, mIs3D = false;

    public BaseTheme() {
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
        drawBoardBack(x, y, r, sr, canvas);
        drawLines(x, y, r, sr, canvas);
    }

    //Override to change drawing
    public void drawBoardBack(float x, float y, float r, float sr, Canvas canvas) {
        //Does nothing
    }

    //override to change drawing
    public void drawLines(float x, float y, float r, float sr, float w, Canvas canvas) {
        if (is3D())
            pB.setShadowLayer(r / 72f / 2, 0, r / 72f, 0x80000000);
        //draw lines and circle
        pB.setColor(accentColor());
        pB.setStyle(Paint.Style.STROKE);
        pB.setStrokeWidth(r * w);
        //draw circles & lines
        canvas.drawCircle(x, y, sr, pB);
        pB.clearShadowLayer();

        Draw.shadedLines(x, y, r, sr, accentColor(), pB, canvas);

        if (is3D())
            Draw.shadedLines(x, y + r / 72f, r, sr, 0x80000000, pB, canvas);
    }

    private void drawLines(float x, float y, float r, float sr, Canvas canvas) {
        drawLines(x, y, r, sr, 1 / 72f, canvas);
    }

    /**
     * Make the buttons draw themselves with the given parameters
     *
     * @param dimens dimensions of the keyboard
     * @param canvas canvas to draw on
     */
    public void drawButtons(NovaKeyDimen dimens, Canvas canvas) {
        for (int i = 0; i < Settings.btns.size(); i++) {
            Settings.btns.get(i).draw(dimens, btnTheme, canvas);
        }
    }

    //To draw keys
    public void drawKeys(float x, float y, float r, float sr, KeyLayout keyboard, boolean capsed,
                         Canvas canvas) {
        if (is3D())
            pT.setShadowLayer(r / 72f / 2, 0, r / 72f / 2, 0x80000000);
        pT.setStyle(Paint.Style.FILL);
        pT.setFlags(Paint.ANTI_ALIAS_FLAG);
        pT.setColor(textColor());
        pT.setTextSize(r / (16 / 3));
        //TODO: different sizes
        if (capsed) {
            pT.setTypeface(Font.SANS_SERIF_CONDENSED);
        } else {
            pT.setTypeface(Font.SANS_SERIF_LIGHT);
        }
        keyboard.drawKeys(textColors, pT, canvas);
        pT.clearShadowLayer();
    }

    /**
     * Draw cursor icons depending on the state of the cursor
     *
     * @param state  state of the cursor
     * @param x      center X position
     * @param y      center Y position
     * @param sr     small radius of keyboard
     * @param canvas canvas to draw on
     */
    @Override
    public void drawCursorIcon(int state, float x, float y, float sr, Canvas canvas) {
        pB.setColorFilter(new LightingColorFilter(textColors()[0], 0));

        Draw.bitmap(Icons.cursors, x, y, 1, pB, canvas);
        if ((state & NovaKey.CURSOR_RIGHT) == NovaKey.CURSOR_RIGHT)
            Draw.bitmap(Icons.cursorLeft, x, y, 1, pB, canvas);
        if ((state & NovaKey.CURSOR_LEFT) == NovaKey.CURSOR_LEFT)
            Draw.bitmap(Icons.cursorRight, x, y, 1, pB, canvas);

        pB.setColorFilter(null);
    }


    public void drawInfiniteMenu(InfiniteMenu infiniteMenu, float x, float y, float r, float sr,
                                 Canvas canvas) {
        if (is3D())
            pT.setShadowLayer(r / 72f / 2, 0, r / 72f / 2, 0x80000000);
        infiniteMenu.draw(x, y, r, sr, textColor(), pT, canvas);
        if (textColor() != textColors()[0]) {
            try {
                canvas.save();
                Path p = new Path();
                p.addCircle(x, y, sr + 2, Path.Direction.CW);
                canvas.clipPath(p);
                infiniteMenu.draw(x, y, r, sr, textColors()[0], pT, canvas);
                canvas.restore();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        pT.clearShadowLayer();
    }

    public void drawOnUpMenu(OnUpMenu onUpMenu, float x, float y, float r, float sr, Canvas canvas) {
        if (is3D())
            pT.setShadowLayer(r / 72f / 2, 0, r / 72f / 2, 0x80000000);
        onUpMenu.draw(x, y, r, sr, this, canvas);

        pT.clearShadowLayer();
    }

    public void setBtnTheme(BtnTheme t) {
        btnTheme = t;
    }

    //since different areas may have different text color for contrast
    public void setTextColors() {
        for (int i = 0; i < textColors.length; i++) {
            textColors[i] = textColor();
        }
    }

    /**
     * used to set the three colors of the theme
     *
     * @param primary  primary color
     * @param accent   accent color
     * @param contrast contrast color
     */
    public void setColors(int primary, int accent, int contrast) {
        mPrimaryColor = primary;
        mAccentColor = accent;
        mContrastColor = contrast;
        //sets text colors depending on each area
        setTextColors();
        btnTheme = new BtnTheme();
        btnTheme.setColors(primaryColor(), contrastColor());
        btnTheme.setIs3D(is3D());
    }

    /**
     * @return the primary color of the theme, usually the background color
     */
    @Override
    public int primaryColor() {
        return mPrimaryColor;
    }

    /**
     * @return the accent color of the theme, usually for lines
     */
    @Override
    public int accentColor() {
        return mAccentColor;
    }

    /**
     * @return the contrast color of the theme, usually for keys & icons
     */
    @Override
    public int contrastColor() {
        return mContrastColor;
    }

    /**
     * Set this theme's current package to have it auto color itself
     *
     * @param pack package to obtain color from
     */
    @Override
    public final void setPackage(String pack) {
        AppTheme app = AppTheme.fromPk(pack);
        if (app == null) {
            //App not supported
            //maybe ask user to support this app
            //TODO: AppTheme.promptUser()
            return;
        }
        setAutoColors(app.color1, app.color2, app.color3);
    }

    /**
     * Override to customize how the auto colors are distributed
     *
     * @param primary primary color
     * @param accent accent color
     * @param contrast contrast color
     */
    protected void setAutoColors(int primary, int accent, int contrast) {
        setColors(primary, accent, contrast);
    }

    /**
     * sets whether theme should use shadows to appear 3D
     *
     * @param is3D true if theme should appear 3D
     */
    @Override
    public void set3D(boolean is3D) {
        mIs3D = is3D;
    }

    /**
     * @returns whether this theme has 3d mode set
     */
    @Override
    public boolean is3D() {
        return mIs3D;
    }

    /**
     * Draw method for the picker item
     *
     * @param x        center x position
     * @param y        center y position
     * @param dimen    dimension equivalent to the maximum height
     * @param selected whether it is selected
     * @param index    sub index of picker item
     * @param p        paint used
     * @param canvas   canvas to draw on
     */
    @Override
    public void draw(float x, float y, float dimen, boolean selected, int index, Paint p, Canvas canvas) {
        float r = dimen / 2 * .8f;
        float sr = (dimen / 2 * .8f) / 3;
        setColors(0xFFF0F0F0, 0xFF616161, 0xFF616161);
        drawBoardBack(x, y, r, sr, canvas);
        drawLines(x, y, r, sr, 1 / 30f, canvas);

        if (selected) {
            p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);
            p.setColor(0xFF58ACFA);
            p.setStrokeWidth(dimen * .1f);
            canvas.drawCircle(x, y, r, p);
            p.setStrokeWidth(0);
            p.setStyle(Paint.Style.FILL);
        }
    }


    //deal wit dis later
    public int buttonColor() {
        return contrastColor();
    }

    public int textColor() {
        return contrastColor();
    }

    public int[] textColors() {
        return textColors;
    }
}
