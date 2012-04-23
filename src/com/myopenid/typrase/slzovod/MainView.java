package com.myopenid.typrase.slzovod;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.Timer;
import java.util.TimerTask;
class MainView extends View {
    private static final int FPS = 30;
    private static final int KRUH_COUNT = 15;
    private static final float SCROLL_SPEED = 3;
    private final MainActivity activity;
    private Renderer rend;
    private final Timer timer = new Timer("Slzovod");
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
        if (activity.universe != null) {
            if (visibility == View.VISIBLE) {
                activity.universe.resume();
                scheduleTask();
            } else {
                activity.universe.pause();
                task.cancel();
            }
        }
    }
    private void scheduleTask() {
        task = new TimerTask() {
            @Override public void run() {
                activity.universe.step();
                postInvalidate();
            }
        };
        timer.schedule(task, 0, 1000 / FPS);
    }
    @Override protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        Log.v("slzovod", "size: " + width + "x" + height);
        final Universe u = init(width, height);
        activity.universe = u;
        rend = new Renderer(u.space);
        scheduleTask();
    }
    private Universe init(float width, float height) {
        Universe u = new Universe(width, height);
        u.populate(KRUH_COUNT);
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
            rend.render(canvas, s, u.oko);
        }
        if (u.warp != null) {
            rend.renderWarp(canvas, u.warp);
        }
    }
    @Override public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN || me.getAction() == MotionEvent.ACTION_MOVE) {
            activity.universe.warp = new PointF(me.getX() - rend.tx, me.getY() - rend.ty);
            return true;
        } else if (me.getAction() == MotionEvent.ACTION_UP) {
            activity.universe.warp = null;
            return true;
        }
        return false;
    }
    @Override public boolean onTrackballEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            rend.tx += event.getX() * SCROLL_SPEED;
            rend.ty += event.getY() * SCROLL_SPEED;
        }
        return true;
    }
}
