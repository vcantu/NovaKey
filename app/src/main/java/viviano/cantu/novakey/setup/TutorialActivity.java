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
import viviano.cantu.novakey.IconView;
import viviano.cantu.novakey.Location;
import viviano.cantu.novakey.drawing.Icons;
import viviano.cantu.novakey.R;
import viviano.cantu.novakey.animations.animators.FocusAnimator;
import viviano.cantu.novakey.animations.animators.TeachAnimator;

public class TutorialActivity extends Activity {

    private EditText mEditText;
    private TextInputLayout mTaskText;
    private IconView mClearIC, mHintIC;
    private TaskView mTaskView;
    private ArrayList<Task> mTasks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.tutorial_layout);
        //forces keyboard to start
		final InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        mTaskView = (TaskView)findViewById(R.id.taskView);
        mTaskView.setOnIndexChangeListener(new TaskView.OnIndexChangeListener() {
            @Override
            public void onNewIndex(int index, int prev) {
                Controller.cancelAnimators();
                if (mTasks != null) {
                    //update task
                    mTaskText.setHint(mTasks.get(index).hintText());

                    //update hint icon
                    mHintIC.setVisibility(mTasks.get(index).hasHint()
                            ? View.VISIBLE : View.INVISIBLE);

                    //initialize depending on task
                    mTasks.get(prev).onEnd();
                    mEditText.setText("");
                    mTasks.get(index).onStart(mEditText);
                }
                if (index > prev)//moved forward
                    mTaskView.disableNext();
            }
        });
        mTaskView.setOnFinishListener(new TaskView.OnFinishListener() {
            @Override
            public void onFinish() {
                finish();
            }
        });

        mEditText = (EditText)findViewById(R.id.editText);
        mEditText.getBackground().setColorFilter(0xFFF0F0F0, PorterDuff.Mode.SRC_ATOP);

        mTaskText = (TextInputLayout)findViewById(R.id.editTextLayout);

        mHintIC = (IconView)findViewById(R.id.hintView);
        mHintIC.setIcon(Icons.get("help"));
        mHintIC.setSize(.6f);

        mClearIC = (IconView)findViewById(R.id.clearView);
        mClearIC.setIcon(Icons.get("clear"));
        mClearIC.setSize(.4f);
        mClearIC.setClickListener(new IconView.OnClickListener() {
            @Override
            public void onClick() {
                mEditText.getText().clear();
            }
        });


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                    mClearIC.setVisibility(View.INVISIBLE);
                else
                    mClearIC.setVisibility(View.VISIBLE);

                if (mTaskView.isComplete(s.toString()))
                    mTaskView.enableNext();
            }
        });

        mHintIC.setClickListener(new IconView.OnClickListener() {
            @Override
            public void onClick() {
                Controller.cancelAnimators();
                if (mTasks != null)
                    mTasks.get(mTaskView.getIndex()).onTeach();
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
        mTasks = new ArrayList<>();
        mTasks.add(new Task("Press Next to begin!", "") {
            @Override
            void onTeach() {}

            @Override
            void onStart(EditText text) {
                text.setText("");
            }

            @Override
            boolean isComplete(String currText) {
                return true;
            }

            @Override
            boolean hasHint() {
                return false;
            }
        });
        final Location[] tappers = new Location[] {
                new Location(0, 0), new Location(1, 0), new Location(2, 0),
                new Location(3, 0), new Location(4, 0), new Location(5, 0) };
        mTasks.add(new Task("For the main letters just tap the area.", "Type \"call\"") {
            @Override
            void onTeach() {
                Controller.animate(new TeachAnimator("call"));
            }

            @Override
            void onStart(EditText text) {
                text.setText("");
                Controller.animate(new FocusAnimator(tappers).addDelay(400));
            }

            @Override
            boolean isComplete(String currText) {
                return currText.equals("call");
            }
        });

        final Location[] swipers = new Location[] {
                new Location(0, 1), new Location(0, 2), new Location(0, 3), new Location(0, 4),
                new Location(0, 5),
                new Location(1, 1), new Location(1, 2), new Location(1, 3), new Location(1, 4),
                new Location(2, 1), new Location(2, 2), new Location(2, 3), new Location(2, 4),
                new Location(3, 1), new Location(3, 2), new Location(3, 3), new Location(3, 4),
                new Location(4, 1), new Location(4, 2), new Location(4, 3), new Location(4, 4),
                new Location(5, 1), new Location(5, 2), new Location(5, 3), new Location(5, 4),
        };
        mTasks.add(new Task("Nice Job!\nFor the remaining keys swipe over the line closest to it.",
                "Type \"novakey\"") {
            @Override
            void onTeach() {
                Controller.animate(new TeachAnimator("novakey"));
            }

            @Override
            void onStart(EditText text) {
                text.setText("");
                Controller.animate(new FocusAnimator(swipers).addDelay(400));
            }

            @Override
            boolean isComplete(String currText) {
                return currText.equals("novakey");
            }


        });

        mTasks.add(new Task("You're doing great! Now to space, swipe from left to right over the small circle.",
                "Type \"hi there\"") {
            @Override
            void onTeach() {
                Controller.animate(new TeachAnimator("hi there"));
            }

            @Override
            void onStart(EditText text) {
                Controller.animate(new TeachAnimator(" ").addDelay(400));
            }

            @Override
            boolean isComplete(String currText) {
                return currText.equals("hi there");
            }
        });
        mTasks.add(new Task("Oh no! A wild smiley has appeared...use delete to get rid of it!",
                "Delete the text") {
            @Override
            void onTeach() {
                Controller.animate(new TeachAnimator("⌫⌫⌫"));
            }

            @Override
            void onStart(EditText text) {
                text.setText(">:)");
                text.setSelection(3);
                Controller.animate(new TeachAnimator("⌫").addDelay(400));
            }

            @Override
            boolean isComplete(String currText) {
                return currText.equals("");
            }
        });
        mTasks.add(new Task("You're awesome! Swipe up to shift, shift twice to lock the shift.",
                "Type \"CAPS ARE FUN\"") {
            @Override
            void onTeach() {
                Controller.animate(new TeachAnimator("▲▲CAPS ARE FUN"));
            }

            @Override
            void onStart(EditText text) {
                Controller.animate(new TeachAnimator("▲▲▲").addDelay(400));
            }

            @Override
            boolean isComplete(String currText) {
                return currText.equals("CAPS ARE FUN");
            }
        });
        mTasks.add(new Task("You're a pro! Swipe down to enter over the circle.", "Type on multiple lines") {
            @Override
            void onTeach() {
                Controller.animate(new TeachAnimator("\n"));
            }

            @Override
            void onStart(EditText text) {
                Controller.animate(new TeachAnimator("\n").addDelay(400));
            }

            @Override
            boolean isComplete(String currText) {
                return currText.contains("\n");
            }
        });
//        mTasks.add(new Task("You can move the cursor! Just rotate around the circle.",
//                "Fix the text to say: \"Apples\"") {
//            @Override
//            void onTeach() {
//                Controller.animate(new TeachAnimator("⑷⑸⑴⑵p").addDelay(400));
//            }
//            @Override
//            void onStart(EditText mEditText) {
//                mEditText.setText("Aples");
//                mEditText.setSelection(5);
//                Controller.animate(new TeachAnimator("⑷⑸⑴⑵").addDelay(400));
//            }
//            @Override
//            boolean isComplete(String currText) {
//                return currText.equals("Apples");
//            }
//        });
//        mTasks.add(new Task("You can move the cursor! Just rotate around the circle.",
//                "Fix the text to say: \"Apples\"") {
//            @Override
//            void onTeach() {
//                Controller.animate(new TeachAnimator("⑷⑸⑴⑵p").addDelay(400));
//            }
//            @Override
//            void onStart(EditText mEditText) {
//                mEditText.setText("Aples");
//                mEditText.setSelection(5);
//                Controller.animate(new TeachAnimator("⑷⑸⑴⑵").addDelay(400));
//            }
//            @Override
//             boolean isComplete(String currText) {
//                return currText.equals("Apples");
//            }
        mTasks.add(new Task("Great! You are done! Keep typing, practice makes perfect.",
                "") {
            @Override
            void onTeach() {
            }

            @Override
            void onStart(EditText text) {
            }

            @Override
            boolean isComplete(String currText) {
                return true;
            }

            @Override
            boolean hasHint() {
                return false;
            }
        });
//        });
        mTaskView.setTasks(mTasks);
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
