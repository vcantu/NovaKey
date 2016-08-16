package viviano.cantu.novakey.controller.touch;

/**
 * Interface for any class which deals managing
 * and dispatching handle events to TouchHandlers
 *
 * Created by Viviano on 6/17/2016.
 */
public interface HandlerManager {

    /**
     * Push a handler to take priority over the
     * other handlers
     *
     * @param handler handler to take priority
     */
    void setHandler(TouchHandler handler);
}
