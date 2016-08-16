package viviano.cantu.novakey.controller.actions;

import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Created by Viviano on 6/26/2016.
 */
public class SelectionActions {

    /**
     * Moves the selection from it's current place. If the desired
     * ending position is outsied the limit it will move the selection to
     * the limit.
     */
    public static class Move implements Action<Void> {

        private final int mStart, mEnd;
        /**
         * Negative parameters move left, positive move right, and 0 doesn't move
         */
        public Move(int start, int end) {
            mStart = start;
            mEnd = end;
        }

        /**
         * Called when the action is triggered
         * Actual logic for the action goes here
         *
         * @param ime
         * @param control
         * @param model
         */
        @Override
        public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
            try {
                ExtractedText et = ime.getCurrentInputConnection()
                        .getExtractedText(new ExtractedTextRequest(), 0);
                int s = et.selectionStart + mStart, e = et.selectionEnd + mEnd;
                s = s < 0 ? 0 : s;
                e = e < 0 ? 0 : e;
                if (s <= e)
                    control.fire(new Set(s, e));
                else {
                    control.fire(new Set(e, s));
                }
            } catch (Exception e) {
            }
            return null;
        }
    }

    /**
     * Sets the selection from it's current place. If the desired
     * ending position is outsied the limit it will move the selection to
     * the limit.
     */
    public static class Set implements Action<Void> {

        private final int mStart, mEnd;
        /**
         * Parameter's are absolute position
         */
        public Set(int start, int end) {
            mStart = start;
            mEnd = end;
        }

        /**
         * Called when the action is triggered
         * Actual logic for the action goes here
         *
         * @param ime
         * @param control
         * @param model
         */
        @Override
        public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
            InputConnection ic = ime.getCurrentInputConnection();
            if (ic == null)
                return null;
            ime.commitComposing();
            //must set selection start first, just in case because of weird bug when user edits cursor it
            // 			can select more to the right but nor the left
            ic.setSelection(mStart, mStart);
            ic.setSelection(mStart, mEnd);
            return null;
        }
    }
}
