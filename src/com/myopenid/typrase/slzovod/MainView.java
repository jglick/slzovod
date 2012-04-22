package com.myopenid.typrase.slzovod;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
class MainView extends View {
    private final MainActivity activity;
    private static final int R_SUCHÝ = 204;
    private static final int G_SUCHÝ = 140;
    private static final int B_SUCHÝ = 0;
    private static final int R_VLHKÝ = 96;
    private static final int G_VLHKÝ = 0;
    private static final int B_VLHKÝ = 0;
    MainView(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }
    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFocusable(true);
    }
    @Override protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        Log.v("slzovod", "size: " + width + "x" + height);
        activity.universe = init(width, height);
    }
    private Universe init(float width, float height) {
        Universe u = new Universe(width, height);
        u.populate(15);
        return u;
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Universe u = activity.universe;
        // XXX paint toroidal
        paint.setARGB(255, 255, 255, 255);
        canvas.drawOval(new RectF(u.oko.x - 20, u.oko.y - 10, u.oko.x + 20, u.oko.y + 10), paint);
        paint.setARGB(255, 0, 0, 255);
        canvas.drawCircle(u.oko.x, u.oko.y, 8, paint);
        paint.setARGB(255, 0, 0, 0);
        canvas.drawCircle(u.oko.x, u.oko.y, 4, paint);
        for (Kruh k : u.kruhy) {
            paint.setARGB(255, R_SUCHÝ + (int) ((R_VLHKÝ - R_SUCHÝ) * k.vlhkost),
                               G_SUCHÝ + (int) ((G_VLHKÝ - G_SUCHÝ) * k.vlhkost),
                               B_SUCHÝ + (int) ((B_VLHKÝ - B_SUCHÝ) * k.vlhkost));
            canvas.drawCircle(k.x, k.y, k.r, paint);
        }
    }
    @Override public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        // XXX warp
        invalidate();
        return true;
    }
    @Override public boolean onTrackballEvent(MotionEvent event) {
        // XXX scroll torus
        return true;
    }
}
