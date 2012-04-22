package com.myopenid.typrase.slzovod;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
class Renderer {
    private static final int R_SUCHÝ = 204;
    private static final int G_SUCHÝ = 140;
    private static final int B_SUCHÝ = 0;
    private static final int R_VLHKÝ = 96;
    private static final int G_VLHKÝ = 0;
    private static final int B_VLHKÝ = 0;
    private static final float[] WRAPS = {-1, 0, -1};
    final float w, h;
    private final Paint p;
    float tx, ty;
    Renderer(float w, float h) {
        this.w = w;
        this.h = h;
        p = new Paint();
        p.setAntiAlias(true);
    }
    void render(Canvas canvas, Oko oko) {
        p.setARGB(255, 255, 255, 255);
        oval(canvas, oko.x, oko.y, 20, 10);
        p.setARGB(255, 0, 0, 255);
        oval(canvas, oko.x, oko.y, 8, 8);
        p.setARGB(255, 0, 0, 0);
        oval(canvas, oko.x, oko.y, 4, 4);
    }
    void render(Canvas canvas, Kruh kruh) {
        p.setARGB(255, R_SUCHÝ + (int) ((R_VLHKÝ - R_SUCHÝ) * kruh.vlhkost),
                       G_SUCHÝ + (int) ((G_VLHKÝ - G_SUCHÝ) * kruh.vlhkost),
                       B_SUCHÝ + (int) ((B_VLHKÝ - B_SUCHÝ) * kruh.vlhkost));
        oval(canvas, kruh.x, kruh.y, kruh.r, kruh.r);
    }
    private void oval(Canvas canvas, float x, float y, float rx, float ry) {
        float ax = (float) Math.IEEEremainder(x + tx, w);
        if (ax < 0) {
            ax += w;
        }
        float ay = (float) Math.IEEEremainder(y + ty, h);
        if (ay < 0) {
            ay += h;
        }
         // XXX may be faster to precalculate which are definitely invisible
        for (float wx : WRAPS) {
            float cx = ax + w * wx;
            for (float wy : WRAPS) {
                float cy = ay + h * wy;
                // XXX if cx > w || cy > h, nothing is drawn, even though cx - rx < w && cy - ry < h; maybe use drawArc?
                canvas.drawOval(new RectF(cx - rx, cy - ry, cx + rx, cy + ry), p);
            }
        }
    }
}
