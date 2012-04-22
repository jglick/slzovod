package com.myopenid.typrase.slzovod;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
class MainView extends View {
    private final MainActivity activity;
    private Renderer rend;
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
        Universe u = init(width, height);
        activity.universe = u;
        rend = new Renderer(u.width, u.height);
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
            rend.tx += event.getX();
            rend.ty += event.getY();
            Log.v("slzovod", "scroll: " + rend.tx + "x" + rend.ty);
            invalidate();
        }
        return true;
    }
}
