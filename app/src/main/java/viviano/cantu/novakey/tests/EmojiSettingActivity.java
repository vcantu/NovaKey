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

package viviano.cantu.novakey.tests;

import android.app.Activity;
import android.os.Bundle;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.view.drawing.drawables.Drawable;
import viviano.cantu.novakey.view.drawing.emoji.Emoji;
import viviano.cantu.novakey.view.drawing.emoji.ThrowAwayView;
import viviano.cantu.novakey.HexGridView;

public class EmojiSettingActivity extends Activity {

    int x = 0, y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Emoji.load(this);
        setContentView(R.layout.activity_emoji_setting);


        final Drawable[][] grid = new Drawable[10][10];

        final HexGridView hex = findViewById(R.id.hex);
        hex.setGrid(grid);

        ThrowAwayView tav = findViewById(R.id.throaway);
        tav.setListener(e -> {
            grid[x][y] = e;
            hex.setGrid(grid);
            hex.invalidate();

            x++;
            if (x >= 10) {
                x = 0;
                y++;
            }
            if (y >= 10)
                y = 0;
        });
    }
}
