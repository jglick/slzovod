package com.myopenid.typrase.slzovod;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
public class MainActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainView(this));
    }
    private class MainView extends View {
        MainView(Context context) {
            super(context);
        }
        @Override protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            setFocusable(true);
        }
        @Override protected void onSizeChanged(int width, int height, int oldw, int oldh) {
            super.onSizeChanged(width, height, oldw, oldh);
            Log.v("slzovod", "onSizeChanged");
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setARGB(255, 255, 0, 0);
            paint.setAntiAlias(true);
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 5, paint);
        }
        @Override public boolean onTouchEvent(MotionEvent me) {
            if (me.getAction() != MotionEvent.ACTION_DOWN) {
                return false;
            }
            invalidate();
            return true;
        }
        @Override public boolean onTrackballEvent(MotionEvent event) {
            return true;
        }
    }
}
