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
    public final int[] colors;
    public final Theme theme;

    public AppTheme(String[] params) {
        name = params[0];
        pk = params[1];
        colors = new int[] { Util.webToColor(params[2]), Util.webToColor(params[3]), Util.webToColor(params[4]) };
        int id = 1;
        //TODO: get theme ID if not there then it's 1
        theme = Theme.getTheme(id);
        theme.setColor(colors[0], colors[1], colors[2]);
        //TODO: set other stuff it theme ID is something else
    }


    private static ArrayList<AppTheme> themes;

    /**
     * Function that creates a theme from the given package name
     * @param pk package name
     * @return A Theme colored according to the app
     */
    public static Theme fromPk(String pk) {
        for (AppTheme t : themes) {
            if (t.pk.equals(pk))
                return t.theme;
        }
        return null;
    }

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
