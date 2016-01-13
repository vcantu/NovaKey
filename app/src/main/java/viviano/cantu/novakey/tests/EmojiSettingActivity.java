package viviano.cantu.novakey.tests;

import android.app.Activity;
import android.os.Bundle;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.emoji.Emoji;
import viviano.cantu.novakey.emoji.HexDisplayView;
import viviano.cantu.novakey.emoji.ThrowAwayView;

public class EmojiSettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Emoji.load(getResources());
        setContentView(R.layout.activity_emoji_setting);

        final HexDisplayView hex = (HexDisplayView)findViewById(R.id.hexView);

        ThrowAwayView tav = (ThrowAwayView)findViewById(R.id.throaway);
        tav.setListener(new ThrowAwayView.onClickListener() {
            @Override
            public void onClik(Emoji e) {
                Emoji.emojis.get(200).addNeighbor(e);
                hex.invalidate();
            }
        });
    }
}
