package viviano.cantu.novakey.controller.touch;

import java.util.Objects;

/**
 * TODO: add velocityX & velocityY to crossEvent
 * Created by Viviano on 6/13/2016.
 */
public class CrossEvent {

    public final int newArea, prevArea;

    public CrossEvent(int newArea, int prevArea) {
        this.newArea = newArea;
        this.prevArea = prevArea;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CrossEvent))
            return false;
        CrossEvent that = (CrossEvent)o;
        return this.newArea == that.newArea &&
                this.prevArea == that.prevArea;
    }


    @Override
    public int hashCode() {
        return newArea * 31 + prevArea;
    }
}
