package viviano.cantu.novakey;


import android.content.Context;
import android.graphics.Typeface;

public class Font {

    //Icons typefaces
    public static Typeface EMOJI, EMOJI_REGULAR, MATERIAL_ICONS, CUSTOM_ICONS;
    public static Typeface
                    SANS_SERIF_LIGHT,
                    SANS_SERIF_CONDENSED;


    public static void create(Context context) {
        //Defaul type faces
        SANS_SERIF_LIGHT = Typeface.create("sans-serif-light", Typeface.NORMAL);
        //SANS_SERIF_LIGHT = Typeface.createFromAsset(context.getAssets(), "SF_Burlington_Script.ttf");
        SANS_SERIF_CONDENSED = Typeface.create("sans-serif-condensed", Typeface.NORMAL);

        // Icons Type faces
        try {
            EMOJI = Typeface.createFromAsset(context.getAssets(), "NotoColorEmoji.ttf");
        } catch (Exception e) {}
        EMOJI_REGULAR = Typeface.createFromAsset(context.getAssets(), "NotoEmoji-Regular.ttf");
        MATERIAL_ICONS = Typeface.createFromAsset(context.getAssets(), "MaterialIcons-Regular.ttf");
        CUSTOM_ICONS = Typeface.createFromAsset(context.getAssets(), "CustomIcons.ttf");
    }
}
