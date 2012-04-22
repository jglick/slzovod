package com.myopenid.typrase.slzovod;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.Timer;
import java.util.TimerTask;
class MainView extends View {
    private final MainActivity activity;
    private Renderer rend;
    private TimerTask task;
    MainView(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }
    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFocusable(true);
    }
    @Override protected void onWindowVisibilityChanged(int visibility) {
        if (visibility != View.VISIBLE) {
            task.cancel(); // XXX restart later
        }
    }
    @Override protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        Log.v("slzovod", "size: " + width + "x" + height);
        final Universe u = init(width, height);
        activity.universe = u;
        rend = new Renderer(u.space);
        task = new TimerTask() {
            @Override public void run() {
                u.step();
                postInvalidate();
            }
        };
        new Timer("Slzovod").scheduleAtFixedRate(task, 0, 100);
    }
    private Universe init(float width, float height) {
        Universe u = new Universe(width, height);
        u.populate(15);
        return u;
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Universe u = activity.universe;
        rend.render(canvas, u.oko);
        for (Kruh k : u.kruhy) {
            rend.render(canvas, k);
        }
        for (Slza s : u.slzy) {
            rend.render(canvas, s);
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
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            rend.tx += event.getX() * 3;
            rend.ty += event.getY() * 3;
            invalidate();
        }
        return true;
    }
}
