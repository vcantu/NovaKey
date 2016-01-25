package viviano.cantu.novakey.setup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class TutorialActivity extends Activity {
	
	public EditText text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar

        //FrameLayout layout = new FrameLayout(this);
        
		RelativeLayout layout = new RelativeLayout(this);
        
        TutorialView view = new TutorialView(this);
        
        layout.addView(view);
        text = view.getTextBox();
        layout.addView(text);

        setContentView(layout);
        
		InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}
}
