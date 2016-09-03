package viviano.cantu.novakey.model.loaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.view.themes.BaseMasterTheme;
import viviano.cantu.novakey.view.themes.MasterTheme;
import viviano.cantu.novakey.view.themes.board.BaseTheme;
import viviano.cantu.novakey.view.themes.board.BoardTheme;
import viviano.cantu.novakey.view.themes.board.DonutTheme;
import viviano.cantu.novakey.view.themes.board.IconTheme;
import viviano.cantu.novakey.view.themes.board.MaterialTheme;
import viviano.cantu.novakey.view.themes.board.MulticolorDonutTheme;
import viviano.cantu.novakey.view.themes.board.MulticolorTheme;
import viviano.cantu.novakey.view.themes.board.SeparateSectionsTheme;

/**
 * Created by Viviano on 2/4/2016.
 */
public class ThemeFactory {

    public static Boards BOARDS = new Boards();

    /*
        Creates a JSON string of the theme following the format:
        {
            "master" {
                "colors" : [ xxx, xxx, xxx ]
                "is3D" : true/false
                "board" : {
                    "class" : xxx
                }
                "background" : {
                    "class" : xxx
                }
                "button" : {
                    "class" : xxx
                }
            }
        }
     */
    public static MasterTheme themeFromString(String str) {
        if (str.equals(Settings.DEFAULT)) {
            return new BaseMasterTheme();
        }
        MasterTheme theme = new BaseMasterTheme();
        try {
            JSONObject obj = new JSONObject(str);
            obj.getJSONObject("master");
            JSONArray colors = obj.getJSONArray("colors");
            theme.setColors(
                    colors.getInt(0),
                    colors.getInt(1),
                    colors.getInt(2))
                    .set3D(obj.getBoolean("is3D"));
            //board
            JSONObject board = obj.getJSONObject("board");
            theme.setBoardTheme(BOARDS.getValue(board.getInt("class")));
            //background
            JSONObject background = obj.getJSONObject("background");

            //button
            JSONObject button = obj.getJSONObject("button");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return theme;
    }

    /*
        Creates a JSON string of the theme following the format:
        {
            "master" {
                "colors" : [ xxx, xxx, xxx ]
                "is3D" : true/false
                "board" : {
                    "class" : xxx
                }
                "background" : {
                    "class" : xxx
                }
                "button" : {
                    "class" : xxx
                }
            }
        }
     */
    public static String stringFromTheme(MasterTheme theme) {
        try {
            //master
            JSONObject master = new JSONObject("master");
            JSONArray colors = new JSONArray()
                    .put(theme.getPrimaryColor())
                    .put(theme.getPrimaryColor())
                    .put(theme.getContrastColor());
            master.put("colors", colors);
            master.put("is3D", theme.is3D());
            //board
            JSONObject board = new JSONObject("board");
            board.put("class", BOARDS.getKey(theme.getBoardTheme()));
            //background
            JSONObject background = new JSONObject("background");

            //button
            JSONObject button = new JSONObject("button");

            //add all
            master.put("board", board);
            master.put("background", background);
            master.put("button", button);

            return master.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Settings.DEFAULT;
    }

    /*
     * Builds a theme from the sharedPref string
     *
     * theme will be a String that can be translated into a BoardTheme, with colors and other data
     * it will have the following format:
     *
     * t = the number theme id
     *
     * numbers represent the color so that:
     * c1 = primaryColor, c2 = accentColor, c3 = contrastColor
     *
     * 'A' or 'X' if theme has auto color enabled
     *
     * '3d' or 'X' if the theme has 3d enabled
     *
     * format:
     *     t,c1,c2,c3,A,3d
     *
     */
    public static MasterTheme themeFromLegacyString(String str) {
        if (str.equals(Settings.DEFAULT)) {
            return new BaseMasterTheme();
        }
        String[] params = str.split(",");

        MasterTheme theme = new BaseMasterTheme();
        theme.setColors(Integer.valueOf(params[1]),
                Integer.valueOf(params[2]),
                Integer.valueOf(params[3]));
        //don't care about auto color
        theme.set3D(params[5].equalsIgnoreCase("3d"));
        theme.setBoardTheme(BOARDS.getValue(Integer.parseInt(params[0])));
        return theme;
    }

    public static int getBoardNum(BoardTheme theme) {
        return BOARDS.getKey(theme);
    }

    public static BoardTheme getBoard(int num) {
        return BOARDS.getValue(num);
    }

    public static class Boards extends InstanceList<BoardTheme> {

        /**
         * Called when constructing, use this to build the
         *
         * @param map
         */
        @Override
        protected void build(Map<Integer, Class> map) {
            map.put(0, BaseTheme.class);
            map.put(1, MasterTheme.class);
            map.put(2, SeparateSectionsTheme.class);
            map.put(3, DonutTheme.class);
            map.put(4, MulticolorDonutTheme.class);
            map.put(5, MulticolorTheme.class);
            map.put(6, IconTheme.class);
        }
    }


}
