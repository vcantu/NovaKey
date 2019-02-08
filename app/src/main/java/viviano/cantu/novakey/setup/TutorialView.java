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

package viviano.cantu.novakey.setup;

import android.graphics.Paint;

public class TutorialView {

    private Paint p = new Paint();
    private int backgroundColor = 0xFF626262, lineColor = 0xFFF0F0F0, doneColor = 0xFF909090;

    private String[][] instructions = new String[][]{
            new String[]{"Lets begin.", "For the main key in each area", "just tap the letter.", "Type: \"cage\""},
            new String[]{"Nice Job!", "Now for the other keys", "swipe over the line closest", "to the letter.", "Type: \"you\""},
            new String[]{"You're doing great!", "Now to space, swipe from", "left to right", "over the small circle.", "Type: \"hi there\""},
            new String[]{"You're awesome!", "Swipe up to shift,", "you can cycle between", "shifted, caps locked and unshifted", "Type: \"I AM COOL\""},
            new String[]{"You are cool! Swipe down", "to enter over the circle", "Type: \"NovaKey", "is", "great\""},
            new String[]{"You're getting good at this.", "Now lets learn to delete", "swipe left over the circle.", "Delete the text1"},
            new String[]{"To delete faster swipe left", "then(without letting go) rotate", "around the circle you can rotate.", "Delete the text1"},
            new String[]{"You're a pro!", "Now just rotate around", "the circle to move the cursor.", "Fix the text1 to say: \"Apples\""},
            new String[]{"Fantastic! While moving the", "cursor quickly go in and", "out of the circle to select", "do it again to switch sides"},
            new String[]{"We cant forget other symbols", "click on the #! to switch keyboard,", "shift while on there to access", "more symbols. Type 123"},
            new String[]{"You can use your clipboard", "while moving the cursor", "hold the center down", "and release on what you want to do"},
            new String[]{"Almost done. Hold down", "any key for the special", "characters rotate to select"},
            new String[]{"Finally, You can hold", "down with two fingers to", "move and resize the keyboard"},
            new String[]{"Congratulations! You finished!", "Keep typing,", "practice makes perfect"},
    };

}