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

package viviano.cantu.novakey.animations;

import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 6/20/2016.
 */
public interface Animation {

    /**
     * Should start the animation
     * <p>
     * Initialize the necessary data here
     */
    void start(Model model);

    /**
     * Cancels the animation
     */
    void cancel();

    /**
     * Set the start delay of this animation
     *
     * @param delay start delay in milliseconds
     * @return this animation
     */
    Animation setDelay(long delay);

    /**
     * @param listener set this animation's on end listener
     * @return this Animation
     */
    Animation setOnEndListener(OnEndListener listener);

    interface OnEndListener {
        /**
         * Called when this animation ends
         */
        void onEnd();
    }

    /**
     * @param listener set this animation's on end listener
     * @return this Animation
     */
    Animation setOnUpdateListener(OnUpdateListener listener);

    interface OnUpdateListener {
        /**
         * Called when this animation updates
         */
        void onUpdate();
    }
}
