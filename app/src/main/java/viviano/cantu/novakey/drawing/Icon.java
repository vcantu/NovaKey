package viviano.cantu.novakey.drawing;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import viviano.cantu.novakey.R;

/**
 * Created by Viviano on 6/8/2015.
 */
public class Icon {

    public static Bitmap cursors, cursorLeft, cursorRight, cancel, accept, refresh;
    private static ArrayList<Drawable> icons;

    public static void load(Context context) {
        Resources res = context.getResources();
        icons = new ArrayList<>();

        cursors = BitmapFactory.decodeResource(res, R.drawable.ic_cursors);
        cursorLeft = BitmapFactory.decodeResource(res, R.drawable.ic_cursor_left);
        cursorRight = BitmapFactory.decodeResource(res, R.drawable.ic_cursor_right);

        //Edit View
        cancel = BitmapFactory.decodeResource(res, R.drawable.ic_action_cancel);
        accept = BitmapFactory.decodeResource(res, R.drawable.ic_action_accept);
        refresh = BitmapFactory.decodeResource(res, R.drawable.ic_action_refresh);

        setSVGs(res);
        setMaterialIcons(res);
    }

    private static void setSVGs(Resources res) {
        AssetManager am = res.getAssets();
        try {
            String[] paths = am.list("svgs/icons");
            icons = new ArrayList<>();
            for (String s : paths) {
                //ic_*****.svg
                icons.add(new SVGIcon(s.substring(3, s.length() - 4), am.open("svgs/icons/" + s)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setMaterialIcons(Resources res) {
        InputStream is = res.openRawResource(R.raw.codepoints);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder sb = new StringBuilder("");
                String[] params = line.split(" ");
                icons.add(new MaterialIcon(params[0], sb.appendCodePoint(Integer.parseInt(params[1], 16)).toString()));
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading TXT file: " + ex);
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

    public static Drawable get(String name) {
        for (Drawable d : icons) {
            if (d.equals(name))
                return d;
        }
        return null;
    }

    public interface Drawable {
        void draw(float x, float y, float size, Paint p, Canvas canvas);
    }
}