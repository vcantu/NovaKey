package viviano.cantu.novakey;


import android.content.Context;
import android.graphics.Typeface;

public class Font {

    public static Typeface
                    SANS_SERIF_LIGHT,
                    SANS_SERIF_CONDENSED,
                    EMOJI,
                    EMOJI_REGULAR,
                    MATERIAL_ICONS;


    public static void create(Context context) {
        SANS_SERIF_LIGHT = Typeface.create("sans-serif-light", Typeface.NORMAL);
        SANS_SERIF_CONDENSED = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        //EMOJI = Typeface.createFromAsset(context.getAssets(), "fonts/NotoColorEmoji.ttf");
        EMOJI_REGULAR = Typeface.createFromAsset(context.getAssets(), "fonts/NotoEmoji-Regular.ttf");
        MATERIAL_ICONS = Typeface.createFromAsset(context.getAssets(), "fonts/MaterialIcons-Regular.ttf");
    }
}
