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

package viviano.cantu.novakey.model.loaders;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.elements.buttons.Button;
import viviano.cantu.novakey.elements.buttons.ButtonData;
import viviano.cantu.novakey.elements.buttons.ButtonToggleModeChange;
import viviano.cantu.novakey.elements.buttons.PunctuationButton;
import viviano.cantu.novakey.view.drawing.shapes.Circle;
import viviano.cantu.novakey.view.posns.DeltaRadiusPosn;

/**
 * Created by viviano on 11/26/2017.
 */

public class ElementsLoader implements Loader<List<Element>> {


    @Override
    public List<Element> load() {
        //TODO: get button size from DPI and preferences
        int size = 150;
        List<Element> buttons = new ArrayList<>();
        Button b1 = new ButtonToggleModeChange(
                new ButtonData()
                        .setPosn(new DeltaRadiusPosn(size / 2, Math.PI * 5 / 4))
                        .setSize(size)
                        .setShape(new Circle()));
        buttons.add(b1);
        Button b2 = new PunctuationButton(
                new ButtonData()
                        .setPosn(new DeltaRadiusPosn(size / 2, Math.PI * 7 / 4))
                        .setSize(size)
                        .setShape(new Circle()));
        buttons.add(b2);
        return buttons;
    }

    @Override
    public void save(List<Element> elements) {

    }
}
