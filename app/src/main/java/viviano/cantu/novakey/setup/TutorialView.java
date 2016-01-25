package viviano.cantu.novakey.setup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class TutorialView extends View {
	
	private Paint p = new Paint();
	private int backgroundColor = 0xFF626262, lineColor = 0xFFF0F0F0, doneColor = 0xFF909090;
	private float screenWidth, screenHeight;
	private int deviceDensity;
	
	private int progress = 0;
	private boolean stepDone = false;
	
	private String[][] instructions = new String[][] {
			new String[] { "Lets begin.", "For the main key in each area", "just tap the letter.", "Type: \"cage\"" },
			new String[] { "Nice Job!", "Now for the other keys", "swipe over the line closest", "to the letter.", "Type: \"you\"" },
			new String[] { "You're doing great!", "Now to space, swipe from", "left to right", "over the small circle.", "Type: \"hi there\"" },
			new String[] { "You're awesome!", "Swipe up to shift,", "you can cycle between", "shifted, caps locked and unshifted", "Type: \"I AM COOL\"" },
			new String[] { "You are cool! Swipe down", "to enter over the circle", "Type: \"NovaKey", "is", "great\"" }, 
			new String[] { "You're getting good at this.", "Now lets learn to delete", "swipe left over the circle.", "Delete the text" },
			new String[] { "To delete faster swipe left", "then(without letting go) rotate", "around the circle you can rotate.", "Delete the text" },
			new String[] { "You're a pro!", "Now just rotate around", "the circle to move the cursor.", "Fix the text to say: \"Apples\"" },
			new String[] { "Fantastic! While moving the", "cursor quickly go in and", "out of the circle to select", "do it again to switch sides" },
			new String[] { "We cant forget other symbols", "click on the #! to switch keyboard,", "shift while on there to access", "more symbols. Type 123" },
			new String[] { "You can use your clipboard", "while moving the cursor", "hold the center down", "and release on what you want to do" },
			new String[] { "Almost done. Hold down", "any key for the special", "characters rotate to select", "Type: \"ni�o\"" },
			new String[] { "Congratulations! You finished!", "Keep typing,", "practice makes perfect"},
	};
	
	private Context context;
	
	public TutorialView(Context context) {
		super(context);
		this.context = context;
		final TutorialActivity parent = (TutorialActivity)context;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		deviceDensity = metrics.densityDpi;
			
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					int dp = 50 * (deviceDensity / 160);
					float y = event.getY(), x = event.getX();
					if (y < dp * 2) {
						if (x < screenWidth / 2 && progress != 0) { // back pressed
							progress--;
						}
						else if (stepDone && x > screenWidth / 2 && progress < instructions.length - 1){ // next pressed
							if (progress < 12)
								progress++;
						}
						stepDone = false;
						parent.text.setText("");
						if (progress == 5) {
							parent.text.setText(">:)");
							parent.text.setSelection(3);
							stepDone = false;
		            	}
						else if (progress == 6) {
							parent.text.setText("DELETE ME PLEASE\nPLEASE\nPLEASE");
							parent.text.setSelection("DELETE ME PLEASE\nPLEASE\nPLEASE".length());
							stepDone = false;
						}
						else if (progress == 7) {
							parent.text.setText("Aples");
							parent.text.setSelection("Aples".length());
						}
						else if (progress == 8) {
							parent.text.setText("Hi I am a text box :), I like you.");
							parent.text.setSelection("Hi I am a text box :), I like you.".length() / 2);
							stepDone = true;
						}
						else if (progress == 10) {
							parent.text.setText("Select me and copy me:)");
							parent.text.setSelection("Select me and copy me:)".length() / 2);
							stepDone = true;
						}
						invalidate();
					}
				break;
				}
				return false;
			}
		});
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(backgroundColor);
		
		p.setStrokeWidth(4);
		p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

		int dp = 50 * (deviceDensity / 160);
		p.setColor(lineColor);
		p.setTextSize(dp / 2.3f);
		drawMultilineText(instructions[progress], screenWidth / 2, dp * 2, p, canvas);
		
		p.setTextSize(dp / 2);
		if (progress != 0)
			drawText("Back", dp, dp / 2, p, canvas);
		if (stepDone)
			p.setColor(lineColor);
		else
			p.setColor(doneColor);
		if (progress < 11)
			drawText("Next", screenWidth - dp, dp / 2, p, canvas);
		else if (progress < 12)
			drawText("Done", screenWidth - dp, dp / 2, p, canvas);
	}
	
	//Draws text centered
	private void drawText(String s, float x, float y, Paint p, Canvas canvas) {
		canvas.drawText(s, x - p.measureText(s) / 2, y - (p.ascent() + p.descent()) / 2, p);
	}
	
	private void drawMultilineText(String[] text, float x, float y, Paint p, Canvas canvas) {
		for (int i=0; i<text.length; i++) {
			int dp = 35 * (deviceDensity / 160);
			drawText(text[i], x, y + dp * i, p, canvas);
		}
	}
	
	public EditText getTextBox() {
		final EditText text = new EditText(context);
        text.setActivated(true);
        text.setGravity(Gravity.CENTER);
        text.setY(screenHeight / 2 - 25 * (deviceDensity / 160));
        text.setHeight(50 * (deviceDensity / 160));
        text.setWidth((int) screenWidth);
        text.setTextColor(lineColor);
        text.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
            	if (progress == 0) {
	            	if (!stepDone && !s.toString().equals("cage".substring(0, s.length()))) {
	            		text.setText("cage".subSequence(0, s.length()-1));
	            		text.setSelection(s.length()-1);
	            	}
	            	if (s.toString().equals("cage")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 1) {
	            	if (!stepDone && !s.toString().equals("you".substring(0, s.length()))) {
	            		text.setText("you".subSequence(0, s.length()-1));
	            		text.setSelection(s.length()-1);
	            	}
	            	if (s.toString().equals("you")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 2) {
	            	if (!stepDone && !s.toString().equals("hi there".substring(0, s.length()))) {
	            		text.setText("hi there".subSequence(0, s.length()-1));
	            		text.setSelection(s.length()-1);
	            	}
	            	if (s.toString().equals("hi there")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 3) {
	            	if (!stepDone && !s.toString().equals("I AM COOL".substring(0, s.length()))) {
	            		text.setText("I AM COOL".subSequence(0, s.length()-1));
	            		text.setSelection(s.length()-1);
	            	}
	            	if (s.toString().equals("I AM COOL")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 4) {
	            	if (!stepDone && !s.toString().equals("NovaKey\nis\ngreat".substring(0, s.length()))) {
	            		text.setText("NovaKey\nis\ngreat".subSequence(0, s.length()-1));
	            		text.setSelection(s.length()-1);
	            	}
	            	if (s.toString().equals("NovaKey\nis\ngreat")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 5 || progress == 6) {
	            	if (s.length()==0) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 7) {
	            	if (s.toString().equals("Apples")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 9) {
	            	if (!stepDone && !s.toString().equals("123".substring(0, s.length()))) {
	            		text.setText("123".subSequence(0, s.length()-1));
	            		text.setSelection(s.length()-1);
	            	}
	            	if (s.toString().equals("123")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            	else if (progress == 11) {
	            	if (!stepDone && !s.toString().equals("ni�o".substring(0, s.length()))) {
	            		text.setText("ni�o".subSequence(0, s.length()-1));
	            		text.setSelection(s.length()-1);
	            	}
	            	if (s.toString().equals("ni�o")) {
	            		stepDone = true;
	            		invalidate();
	            	}
            	}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        }); 
        return text;
	}
}
