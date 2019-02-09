/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.core.utils.drawing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import viviano.cantu.novakey.core.R;
import viviano.cantu.novakey.core.utils.drawing.drawables.BMPDrawable;
import viviano.cantu.novakey.core.utils.drawing.drawables.Drawable;
import viviano.cantu.novakey.core.utils.drawing.drawables.FontIcon;

/**
 * Created by Viviano on 6/8/2015.
 */
public class Icons {

    public static BMPDrawable cursors, cursorLeft, cursorRight;//TODO: add to list
    public static Bitmap cancel, accept, refresh;
    private static ArrayList<Drawable> icons;


    public static void load(Context context) {
        Resources res = context.getResources();
        icons = new ArrayList<>();

        cursors = new BMPDrawable(res, R.drawable.ic_cursors);
        cursorLeft = new BMPDrawable(res, R.drawable.ic_cursor_left);
        cursorRight = new BMPDrawable(res, R.drawable.ic_cursor_right);

        //Edit View
        cancel = BitmapFactory.decodeResource(res, R.drawable.ic_action_cancel);
        accept = BitmapFactory.decodeResource(res, R.drawable.ic_action_accept);
        refresh = BitmapFactory.decodeResource(res, R.drawable.ic_action_refresh);

        setMaterialIcons(res);
        setCustomIcons(res);
    }


    /**
     * gets the material icons from the material icons font and the codepoints.txt
     *
     * @param res resources used to load icons
     */
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
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading TXT file: " + ex);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
    }


    /**
     * Gets the custom icons from the custom icon font and the codepoints_custom txt
     *
     * @param res resources used to load icons
     */
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
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading TXT file: " + ex);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
    }


    /**
     * gets an icon from the global list of icons
     *
     * @param name name of the icon
     * @return the drawable
     */
    public static Drawable get(String name) {
        for (Drawable d : icons) {
            if (d.equals(name))
                return d;
        }
        return null;
    }
}