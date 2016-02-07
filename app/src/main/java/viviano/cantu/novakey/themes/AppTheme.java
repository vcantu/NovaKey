package viviano.cantu.novakey.themes;


import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 8/18/2015.
 */
public class AppTheme {

    public final String name, pk;
    public final int color1, color2, color3;

    public AppTheme(String[] params) {
        name = params[0];
        pk = params[1];
        color1 = Util.webToColor(params[2]);
        color2 = Util.webToColor(params[3]);
        color3 = Util.webToColor(params[4]);
    }

    private static ArrayList<AppTheme> themes;
    /**
     * Function that creates a theme from the given package name
     * @param pk package name
     * @return A BaseTheme colored according to the app
     */
    public static AppTheme fromPk(String pk) {
        for (AppTheme t : themes) {
            if (t.pk.equals(pk))
                return t;
        }
        return null;
    }

    /**
     * Will load from csv file in raw
     *
     * @param res resources to load from
     */
    public static void load(Resources res) {
        themes = new ArrayList<>();

        InputStream is = res.openRawResource(R.raw.app_colors);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] params = line.split(",");
                themes.add(new AppTheme(params));
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
    }
}
