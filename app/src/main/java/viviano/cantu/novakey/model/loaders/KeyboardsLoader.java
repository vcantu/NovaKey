package viviano.cantu.novakey.model.loaders;

import android.content.Context;

import viviano.cantu.novakey.elements.keyboards.Keyboards;

/**
 * Created by viviano on 11/26/2017.
 */

public class KeyboardsLoader implements Loader<Keyboards> {

    private final Context mContext;

    public KeyboardsLoader(Context context) {
        mContext = context;
    }

    @Override
    public Keyboards load() {
        return new Keyboards(mContext);
    }

    @Override
    public void save(Keyboards keyboards) {
        //TODO
    }
}
