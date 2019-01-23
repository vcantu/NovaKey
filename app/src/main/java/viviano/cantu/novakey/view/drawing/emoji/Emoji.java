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

package viviano.cantu.novakey.view.drawing.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.view.drawing.Draw;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.drawing.Font;

/**
 * Created by Viviano on 11/16/2015.
 */
public class Emoji implements Drawable {

    public static List<Emoji> emojis;

    public static void load(Context context) {
        Resources res = context.getResources();
        emojis = new ArrayList<>();
        try {
            InputStream is = res.openRawResource(R.raw.emoji);
            byte[] b = new byte[is.available()];
            is.read(b);
            JSONObject jObject = new JSONObject(new String(b));
            JSONArray arr = jObject.getJSONArray("emojis");

            for (int i=0; i<arr.length(); i++) {
                JSONObject curr = arr.getJSONObject(i);

                if (curr.getBoolean("has_img_google")) {
                    //Get code
                    StringBuilder sb = new StringBuilder("");
                    String code = curr.getString("unified");
                    String[] S = code.split("-");
                    for (String s : S) {
                        sb.appendCodePoint(Integer.parseInt(s, 16));
                    }
                    //Get name
                    String name = "";
                    name = curr.getString("name");
                    //Get bmp
                    String image = curr.getString("image");
                    Bitmap bmp = BitmapFactory.decodeResource(res, res.getIdentifier(
                            "e_" + image.replace('-', '_').substring(0, image.length() - 4),
                                    "drawable", context.getPackageName()));

                    emojis.add( new Emoji(name, sb.toString(), bmp));
                }
            }
        } catch (JSONException e) {
            Log.e("Exception", e.toString());
        } catch (IOException e){
            Log.e("Exception", e.toString());
        }
    }

//    public static Emoji find(String value) {
//        if (emojis.containsKey(value))
//            return emojis.get(value);
//        return null;
//    }
//
//    public static Emoji findByName(String name) {
//        for (Map.Entry<String, Emoji> e : emojis.entrySet()) {
//            if (e.getValue().getName().equals(name)) {
//                return e.getValue();
//            }
//        }
//        return null;
//    }

    //---------------------------------------------------------------------------------------------
    private final String name, value;
    private final Bitmap bmp;

    public Emoji(String name, String value, Bitmap bmp) {
        this.name = name;
        this.value = value;
        this.bmp = bmp;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        canvas.drawBitmap(bmp, //Bitmap.createScaledBitmap(bmp, (int)size, (int)size, false),
                x - size/2, y - size/2, p);
    }

    private void drawFromTTF(float x, float y, float size, Paint p, Canvas canvas) {
        Typeface tempTTF = p.getTypeface();
        float tempSize = p.getTextSize();

        p.setTypeface(Font.EMOJI);
        p.setTextSize(size);
        Draw.text(value, x, y, p, canvas);

        p.setTypeface(tempTTF);
        p.setTextSize(tempSize);
    }



}
