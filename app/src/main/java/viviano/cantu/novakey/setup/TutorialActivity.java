package viviano.cantu.novakey.setup;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import viviano.cantu.novakey.Controller;
import viviano.cantu.novakey.drawing.Icon;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.animations.animators.FocusAnimator;
import viviano.cantu.novakey.animations.animators.TeachAnimator;

public class TutorialActivity extends Activity {

    private EditText text;
    private TextInputLayout textHint;
    private IconView clearView, hintView;
    private TaskView taskView;
    private ArrayList<Task> tasks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.tutorial_layout);
        //forces keyboard to start
		final InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        taskView = (TaskView)findViewById(R.id.taskView);
        taskView.setListener(new TaskView.OnIndexChangeListener() {
            @Override
            public void onNewIndex(int index, int prev) {
                textHint.setHint(tasks.get(index).hintText());
                if (Controller.view() != null)
                    Controller.clearDrawers();
                if (tasks != null) {
                    tasks.get(prev).onEnd();
                    tasks.get(index).onStart(text);
                }
            }
        });

        text = (EditText)findViewById(R.id.editText);
        text.getBackground().setColorFilter(0xFFF0F0F0, PorterDuff.Mode.SRC_ATOP);

        textHint = (TextInputLayout)findViewById(R.id.editTextLayout);

        hintView = (IconView)findViewById(R.id.hintView);
        hintView.setIcon(Icon.get("help"));
        hintView.setSize(.6f);

        clearView = (IconView)findViewById(R.id.clearView);
        clearView.setIcon(Icon.get("clear"));
        clearView.setSize(.4f);
        clearView.setClickListener(new IconView.OnClickListener() {
            @Override
            public void onClick() {
                text.getText().clear();
            }
        });


        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                    clearView.setVisibility(View.INVISIBLE);
                else
                    clearView.setVisibility(View.VISIBLE);
            }
        });

        hintView.setClickListener(new IconView.OnClickListener() {
            @Override
            public void onClick() {
                if (tasks != null)
                    tasks.get(taskView.getIndex()).onTeach();
            }
        });
        setInstructions();
	}

    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    private void setInstructions() {
        tasks = new ArrayList<>();
        tasks.add(new Task("Press Next to begin!", "") {
            @Override
            public void onTeach() {
            }
            @Override
            public void onStart(EditText text) {
                text.setText("");
            }
            @Override
            public boolean isComplete(String currText) {
                return true;
            }
        });
        tasks.add(new Task("For the main letters just tap the area.", "Type \"call\"") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("call"));
            }
            @Override
            public void onStart(EditText text) {
                text.setText("");
                Controller.animate(new FocusAnimator("aeiclh").addDelay(400));
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.equals("call");
            }
            @Override
            public void onEnd() {
                Controller.animate(new FocusAnimator("aeiclh", true));
            }
        });
        tasks.add(new Task("Nice Job!\nFor the remaining keys swipe over the line closest to it.",
                "Type \"novakey\"") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("novakey"));
            }
            @Override
            public void onStart(EditText text) {
                text.setText("");
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.equals("novakey");
            }
        });
        tasks.add(new Task("You're doing great! Now to space, swipe from left to right over the small circle.",
                "Type \"hi there\"") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("hi there"));
            }
            @Override
            public void onStart(EditText text) {
                text.setText("");
                Controller.animate(new TeachAnimator(" ").addDelay(400));
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.equals("hi there");
            }
        });
        tasks.add(new Task("Oh no! A wild smiley has appeared...use delete to get rid of it!",
                "Delete the text") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("⌫⌫⌫"));
            }
            @Override
            public void onStart(EditText text) {
                text.setText(">:)");
                text.setSelection(3);
                Controller.animate(new TeachAnimator("⌫").addDelay(400));
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.equals("");
            }
        });
        tasks.add(new Task("You're awesome! Swipe up to shift, shift twice to lock the shift.",
                "Type \"CAPS ARE FUN\"") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("▲▲CAPS ARE FUN"));
            }
            @Override
            public void onStart(EditText text) {
                text.setText("");
                Controller.animate(new TeachAnimator("▲▲▲").addDelay(400));
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.equals("CAPS ARE FUN");
            }
        });
        tasks.add(new Task("You're a pro! Swipe down to enter over the circle.", "Type on multiple lines") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("\n"));
            }
            @Override
            public void onStart(EditText text) {
                text.setText("");
                Controller.animate(new TeachAnimator("\n").addDelay(400));
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.contains("\n");
            }
        });
        tasks.add(new Task("You can move the cursor! Just rotate around the circle.",
                "Fix the text to say: \"Apples\"") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("⑷⑸⑴⑵p").addDelay(400));
            }
            @Override
            public void onStart(EditText text) {
                text.setText("Aples");
                text.setSelection(5);
                Controller.animate(new TeachAnimator("⑷⑸⑴⑵").addDelay(400));
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.equals("Apples");
            }
        });
        tasks.add(new Task("You can move the cursor! Just rotate around the circle.",
                "Fix the text to say: \"Apples\"") {
            @Override
            public void onTeach() {
                Controller.animate(new TeachAnimator("⑷⑸⑴⑵p").addDelay(400));
            }
            @Override
            public void onStart(EditText text) {
                text.setText("Aples");
                text.setSelection(5);
                Controller.animate(new TeachAnimator("⑷⑸⑴⑵").addDelay(400));
            }
            @Override
            public boolean isComplete(String currText) {
                return currText.equals("Apples");
            }
        });
        taskView.setTasks(tasks);
        String[] tasks = new String[] {
                "You're a pro!", "Now just rotate around", "the circle to move the cursor.", "Fix the text1 to say: \"Apples\"" ,
                "Fantastic! While moving the\ncursor quickly go in and\nout of the circle to select\ndo it again to switch sides",
                "We cant forget other symbols\nclick on the #! to switch keyboard,\nshift while on there to access\nmore symbols. Type 123",
                "You can use your clipboard\nwhile moving the cursor\nhold the center down\nand release on what you want to do",
                "Almost done. Hold down\nany key for the special\ncharacters rotate to select",
                "Finally, You can hold\ndown with two fingers to\nmove and resize the keyboard",
                "Congratulations! You finished!\nKeep typing,\npractice makes perfect" };
    }
}
