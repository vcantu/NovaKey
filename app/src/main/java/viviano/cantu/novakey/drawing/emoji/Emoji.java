package viviano.cantu.novakey.drawing.emoji;

import android.content.res.Resources;
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

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.drawing.Draw;
import viviano.cantu.novakey.Font;
import viviano.cantu.novakey.drawing.Icons;

/**
 * Created by Viviano on 11/16/2015.
 */
public class Emoji implements Icons.Drawable {

    public static ArrayList<Emoji> emojis;

    public static void load(Resources res) {
        emojis = new ArrayList<>();
        try {
            InputStream is = res.openRawResource(R.raw.emoji);
            byte[] b = new byte[is.available()];
            is.read(b);
            JSONObject jObject = new JSONObject(new String(b));
            JSONArray arr = jObject.getJSONArray("emojis");

            for (int i=0; i<arr.length(); i++) {
                //Get code
                JSONObject curr = arr.getJSONObject(i);
                StringBuilder sb = new StringBuilder("");
                String code = curr.getString("code");
                String[] S = code.split("-");
                for (String s : S) {
                    sb.appendCodePoint(Integer.parseInt(s, 16));
                }
                //Get name
                String name = "";
                try {
                    name = curr.getString("name");
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
                emojis.add(new Emoji(name, sb.toString(), i));
            }
        } catch (JSONException e) {
            Log.e("Exception", e.toString());
        } catch (IOException e){
            Log.e("Exception", e.toString());
        }
    }

    public static Emoji find(String value) {
        for (Emoji e : emojis) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    //---------------------------------------------------------------------------------------------
    private String name, value;
    private Emoji[] neighbors = new Emoji[6];//All null to start with
    private int id;//index in array

    public Emoji(String name, String value, int id) {
        this.name = name;
        this.value = value;
        this.id = id;
    }

    public int id() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setNeighbors(Emoji[] neighbors) {
        this.neighbors = neighbors;
    }

    //   0  5
    // 1      4
    //   2  3
    public void setNeighbor(int index, Emoji neighbor) {
        this.neighbors[index] = neighbor;
        this.neighbors[(index + 3) % 6] = this;
    }

    public void addNeighbor(Emoji neighbor) {
        for (int i=0; i<6; i++) {
            if (this.neighbors[i] == null) {
                this.neighbors[i] = neighbor;
                return;
            }
        }
        this.neighbors = new Emoji[6];
        this.neighbors[0] = neighbor;
    }

    public Emoji getNeighbor(int index) {
        return neighbors[index];
    }

    @Override
    public void draw(float x, float y, float size, Paint p, Canvas canvas) {
        Typeface tempTTF = p.getTypeface();
        float tempSize = p.getTextSize();

        p.setTypeface(Font.EMOJI);
        p.setTextSize(size);
        Draw.text(value, x, y, p, canvas);

        p.setTypeface(tempTTF);
        p.setTextSize(tempSize);
    }
}
