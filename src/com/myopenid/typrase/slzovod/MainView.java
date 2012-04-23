package com.myopenid.typrase.slzovod;
import android.graphics.Canvas;
import android.graphics.Picture;
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
    private static final boolean USE_STATIS = false;
    private Picture stasis;
    private float stasisKey;
    MainView(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }
    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFocusable(true);
    }
    @Override protected void onWindowVisibilityChanged(int visibility) {
        // XXX this should rather be done in MainActivity.onPause/onResume
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
        stasis = null;
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
        if (USE_STATIS) {
            float key = stasisKey(u);
            if (stasis == null || key != stasisKey) {
                Log.v("Slzovod", "redrawing stasis");
                stasis = new Picture();
                renderStasis(u, stasis.beginRecording(canvas.getWidth(), canvas.getHeight()));
                stasisKey = key;
            }
            stasis.draw(canvas);
        } else {
            renderStasis(u, canvas);
        }
        for (Slza s : u.slzy) {
            rend.render(canvas, s, u.oko);
        }
        if (u.warp != null) {
            rend.renderWarp(canvas, u.warp);
        }
    }
    private void renderStasis(Universe u, Canvas canvas) {
        rend.render(canvas, u.oko);
        for (Kruh k : u.kruhy) {
            rend.render(canvas, k);
        }
    }
    private float stasisKey(Universe u) {
        float key = 0;
        for (Kruh k : u.kruhy) {
            key = key * 1.0023456789f + k.vlhkost;
        }
        //Log.v("Slzovod", "statisKey=" + key);
        return key;
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
            stasis = null;
        }
        return true;
    }
}
