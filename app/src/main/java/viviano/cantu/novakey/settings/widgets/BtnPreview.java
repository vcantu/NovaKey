package viviano.cantu.novakey.settings.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import viviano.cantu.novakey.KeyLayout;
import viviano.cantu.novakey.utils.Util;
import viviano.cantu.novakey.btns.Btn;
import viviano.cantu.novakey.btns.BtnTheme;
import viviano.cantu.novakey.settings.Settings;

/**
 * Created by Viviano on 6/25/2015.
 */
public class BtnPreview extends View implements View.OnTouchListener {

    private Paint p;
    //dimensions
    public float radius;
    protected float viewWidth, viewHeight, centerX, centerY, smallRadius;

    private ArrayList<Btn> btns = new ArrayList<Btn>();
    private int movingBtn = -1;
    private boolean btnAdded = false;

    private BtnTheme btnTheme;

    public BtnPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//allows svgs
        setOnTouchListener(this);

        p = new Paint();
        setViewDimen();

        btnTheme = new BtnTheme(BtnTheme.BACK_OUTLINE, Settings.theme.buttonColor(),
                Settings.theme.buttonColor());
        for (Btn b : Settings.btns) {
            btns.add(b);
        }
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setViewDimen();
    }
    private void setViewDimen() {
        viewWidth = getWidth();
        viewHeight = getHeight();
        radius = Math.min(viewHeight - getPaddingTop() - getPaddingBottom(),
                viewWidth - getPaddingRight() - getPaddingLeft());
        radius /= 2;
        centerX = viewWidth / 2;
        centerY = getPaddingTop() + radius;
        smallRadius = radius / 3;
    }


    @Override
    public void onDraw(Canvas canvas) {
        setViewDimen();
        Settings.theme.drawBackground(0, 0, viewWidth, viewHeight, centerX, centerY,
                radius, smallRadius, canvas);
        Settings.theme.drawBoard(centerX, centerY, radius, smallRadius, canvas);
        //draw main keys
        Settings.theme.drawKeys(centerX, centerY, radius, smallRadius, KeyLayout.get("English"),
                false, canvas);

        p.setColor(Settings.theme.buttonColor());
        for (int i=0; i<btns.size(); i++) {
            float sw = p.getStrokeWidth();
            if (i==movingBtn)
                p.setStrokeWidth(4);
            btns.get(i).draw(centerX, centerY, radius, btnTheme, canvas);
            p.setStrokeWidth(sw);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!btnAdded)
                    movingBtn = onBtn(e.getX(), e.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (movingBtn != -1) {
                    setBtn(movingBtn, Util.angle(centerX, centerY, e.getX(), e.getY()),
                            getDistance(centerX, centerY, e.getX(), e.getY()));
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                movingBtn = -1;
                btnAdded = false;
                invalidate();
                break;
        }
        return true;
    }

    public void addBtn(int shape) {
        btnAdded = true;
        btns.add(new Btn(Util.angle(centerX, centerY, viewWidth / 2, viewHeight),
                getDistance(centerX, centerY, viewWidth / 2, viewHeight) / radius, shape));
        movingBtn = btns.size()-1;
        invalidate();
    }

    public int onBtn(float x, float y) {
        for (int i=0; i<btns.size(); i++) {
            if (btns.get(i).onBtn(x, y, centerX, centerY, radius))
                return i;
        }
        return -1;
    }

    //basic controller for all btns
    private void setBtn(int index, double angle, float dist) {
        Btn b = btns.get(index);
        if ((b.shape&Btn.SHAPE)!=Btn.ARC)
            b.dist = dist / radius;
        else
            b.dist = 1.16667f;
        b.angle = angle;
    }

    private boolean checkCollision(double a1, float d1, float r1, double a2, float d2, float r2) {
        return getDistance(centerX + (float)Math.cos(a1) * d1, centerY + (float)Math.sin(a1) * d1,
                centerX + (float)Math.cos(a2) * d2, centerY + (float)Math.sin(a2) * d2)
                < r1 + r2;
    }

    public float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
}
