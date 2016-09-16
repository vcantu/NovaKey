package viviano.cantu.novakey.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.model.elements.Element;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.model.elements.buttons.Button;
import viviano.cantu.novakey.model.elements.buttons.ButtonData;
import viviano.cantu.novakey.model.elements.buttons.ButtonToggleModeChange;
import viviano.cantu.novakey.model.elements.buttons.PunctuationButton;
import viviano.cantu.novakey.model.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.loaders.Loader;
import viviano.cantu.novakey.model.loaders.ThemeLoader;
import viviano.cantu.novakey.view.drawing.shapes.Circle;
import viviano.cantu.novakey.view.posns.DeltaRadiusPosn;
import viviano.cantu.novakey.view.themes.MasterTheme;

/**
 * Created by Viviano on 6/7/2016.
 *
 * Model that saves and reads all of it's data from a persistent source
 * like shared preferences or a file.
 *
 * This is typically altered by the user, knowingly, as a setting.
 * And it is typically read by the MainModel upon it's creation
 * generate it's data according to the user's preferences.
 *
 * Only views which change settings and other models should have access
 * to this model
 */
public class TrueModel implements DrawModel {

    //Shared preferences
    private final SharedPreferences mSharedPref;
    private final Context mContext;

    //Loaders
    private final Loader<List<Element>> mElementLoader;
    private final Loader<MasterTheme> mThemeLoader;

    //Keyboards
    private final Keyboards mKeyboards;

    public TrueModel(Context context) {
        this.mContext = context;
        this.mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        //loaders
        mThemeLoader = new ThemeLoader(mSharedPref);
        mElementLoader = null;//TODO: dis shite

        //Keyboards
        mKeyboards = new Keyboards(context);
    }

    private boolean isLandscape() {
        return mContext.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    public Keyboards getKeyboards() {
        return mKeyboards;
    }

    /**
     * @return the width of the board
     */
    @Override
    public int getWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * @param width width to set
     */
    @Override
    public void setWidth(int width) {
        //TODO: for now it's based on the width of the screen
    }

    /**
     * @return the height of the board
     */
    @Override
    public int getHeight() {
        float r = getRadius();
        return (int)(mSharedPref.getFloat("y" + (isLandscape() ? "_land" : ""), r)
                + r + getPadding());
    }

    /**
     * @param height height to set
     */
    @Override
    public void setHeight(int height) {
        //TODO: for now it's based on the radius and padding
    }

    /**
     * @return the center X position of the board
     */
    @Override
    public float getX() {
        return mSharedPref.getFloat("x" + (isLandscape() ? "_land" : ""),
                getWidth() / 2);
    }

    /**
     * @param x x coord to set
     */
    @Override
    public void setX(float x) {
        mSharedPref.edit()
                .putFloat("x" + (isLandscape() ? "_land" : ""), x)
                .commit();
    }

    /**
     * @return the center Y position of the board
     */
    @Override
    public float getY() {
        return getRadius() + getPadding();
    }

    /**
     * @param y y coord to set
     */
    @Override
    public void setY(float y) {
        mSharedPref.edit()
                .putFloat("y" + (isLandscape() ? "_land" : ""), y)
                .commit();
    }

    /**
     * @return the radius of the board
     */
    @Override
    public float getRadius() {
        return this.mSharedPref.getFloat("size" + (isLandscape() ? "_land" : ""),
                mContext.getResources().getDimension(R.dimen.default_radius));
    }

    /**
     * @param radius radius to set
     */
    @Override
    public void setRadius(float radius) {
        mSharedPref.edit()
                .putFloat("size" + (isLandscape() ? "_land" : ""), radius)
                .commit();
    }

    /**
     * @return the small radius of the board
     */
    @Override
    public float getSmallRadius() {
        return getRadius() / mSharedPref.getFloat("smallRadius", 3);
    }

    /**
     * @param smallRadius radius to set
     */
    @Override
    public void setSmallRadius(float smallRadius) {
        mSharedPref.edit()
                .putFloat("smallRadius", smallRadius)
                .commit();
    }

    /**
     * @return the top vertical padding
     */
    @Override
    public float getPadding() {
        return mSharedPref.getFloat("padd" + (isLandscape() ? "_land" : ""),
                mContext.getResources().getDimension(R.dimen.default_padding));
    }

    /**
     * @param padding padding to set
     */
    @Override
    public void setPadding(float padding) {
        mSharedPref.edit()
                .putFloat("padd" + (isLandscape() ? "_land" : ""), padding)
                .commit();
    }

    /**
     * @return this model's theme
     */
    @Override
    public MasterTheme getTheme() {
        return mThemeLoader.load();
    }

    /**
     * @param theme theme to set
     */
    @Override
    public void setTheme(MasterTheme theme) {
        mThemeLoader.save(theme);
    }

    /**
     * @return a list of the buttons saved by user preferences
     */
    public List<Element> getButtons() {
        List<Element> buttons = new ArrayList<>();
        Button b1 = new ButtonToggleModeChange(
                new ButtonData()
                        .setPosn(new DeltaRadiusPosn(75, Math.PI * 5 / 4))
                        .setSize(150)
                        .setShape(new Circle()));
        buttons.add(b1);
        Button b2 = new PunctuationButton(
                new ButtonData()
                        .setPosn(new DeltaRadiusPosn(75, Math.PI * 7 / 4))
                        .setSize(150)
                        .setShape(new Circle()));
        buttons.add(b2);
        return buttons;
    }
}
