package viviano.cantu.novakey.drawing;

import android.content.Context;
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

import viviano.cantu.novakey.Font;
import viviano.cantu.novakey.R;

/**
 * Created by Viviano on 6/8/2015.
 */
public class Icons {

    public static Bitmap cursors, cursorLeft, cursorRight, cancel, accept, refresh, border_center;
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

        border_center = BitmapFactory.decodeResource(res, R.drawable.ic_border_vertical_white_36dp);

        setMaterialIcons(res);
        setCustomIcons(res);
    }

    private static void setMaterialIcons(Resources res) {
        InputStream is = res.openRawResource(R.raw.codepoints);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder sb = new StringBuilder("");
                String[] params = line.split(" ");
                icons.add(new FontIcon(params[0],
                        sb.appendCodePoint(Integer.parseInt(params[1], 16)).toString(),
                        Font.MATERIAL_ICONS));
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

    private static void setCustomIcons(Resources res) {
        InputStream is = res.openRawResource(R.raw.codepoints_custom);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder sb = new StringBuilder("");
                String[] params = line.split(" ");
                icons.add(new FontIcon(params[0],
                        sb.appendCodePoint(Integer.parseInt(params[1], 16)).toString(),
                        Font.CUSTOM_ICONS));
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

    /**
     * wrapper for Icon.Drawable.draw that null checks
     */
    public static void draw(Drawable ic, float x, float y, float size, Paint p, Canvas canvas) {
        if (ic != null && p != null && canvas != null)
            ic.draw(x, y, size, p, canvas);
    }

    public static void draw(String name, float x, float y, float size, Paint p, Canvas canvas) {
        draw(get(name), x, y, size, p, canvas);
    }

    public interface Drawable {
        /**
         * DO NOT CALL THIS call Icons.draw() instead as it does null checks before
         * drawing
         *
         * @param x x position
         * @param y y position
         * @param size size of icon
         * @param p paint to use
         * @param canvas canvas to draw on
         */
        void draw(float x, float y, float size, Paint p, Canvas canvas);
    }
}