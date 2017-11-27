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
