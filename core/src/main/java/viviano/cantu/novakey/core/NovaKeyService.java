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

package viviano.cantu.novakey.core;

import android.content.ClipboardManager;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

public abstract class NovaKeyService extends InputMethodService {

    /**
     * @return this device's clipboard manager
     */
    public abstract ClipboardManager getClipboard();


    /**
     * Vibrates the device given an amount of milliseconds
     *
     * @param milliseconds amount to activate vibrator for
     */
    public abstract void vibrate(long milliseconds);


    /**
     * inputs the given text as is
     *
     * @param text         text to input
     * @param newCursorPos were the cursor should end
     */
    public abstract void inputText(String text, int newCursorPos);


    /**
     * This method is expensive, avoid it
     *
     * @return extracted text object, which receives updates
     */
    public abstract ExtractedText getExtractedText();


    /**
     * Get text that's selected
     *
     * @return text between the selection start and end
     */
    public abstract String getSelectedText();


    /**
     * Gets the current capitalization mode at the current cursor position
     *
     * @return 1 for caps 0 for not caps
     */
    public abstract int getCurrentCapsMode();


    /**
     * move the selection from it's current position
     *
     * @param deltaStart difference of start cursor
     * @param deltaEnd   difference of end cursor
     */
    public abstract void moveSelection(int deltaStart, int deltaEnd);


    /**
     * move the selection to the absolute position
     *
     * @param start start of cursor
     * @param end   end of cursor
     */
    public abstract void setSelection(int start, int end);


    /**
     * Commits the current composing text with the best possible correction
     */
    public abstract void commitCorrection();


    /**
     * Commits the given text replacing the current composing text
     *
     * @param text text to commit
     */
    public abstract void commitReplacementText(String text);


    /**
     * Commits the current composing text to the editor without corrections
     */
    public abstract void commitComposingText();
}
