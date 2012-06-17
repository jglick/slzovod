package com.myopenid.typrase.slzovod;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
class Renderer {
    private static final int R_SUCHÝ = 204;
    private static final int G_SUCHÝ = 140;
    private static final int B_SUCHÝ = 0;
    private static final int R_VLHKÝ = 96;
    private static final int G_VLHKÝ = 0;
    private static final int B_VLHKÝ = 0;
    private static final PointF[] wraps_tmp = new PointF[9];
    private static final double DOPPLER = .1;
    static {
        for (int i = 0; i < 9; i++) {
            wraps_tmp[i] = new PointF();
        }
    }
    final Torus space;
    private final Paint p;
    float tx, ty;
    Renderer(Torus space) {
        this.space = space;
        p = new Paint();
        p.setAntiAlias(true);
    }
    void render(Canvas canvas, Oko oko) {
        p.setARGB(255, 255, 255, 255);
        oval(canvas, oko.x, oko.y, oko.rx, oko.ry);
        p.setARGB(255, 0, 0, 255);
        circle(canvas, oko.x, oko.y, oko.ry * .8f);
        p.setARGB(255, 0, 0, 0);
        circle(canvas, oko.x, oko.y, oko.ry * .4f);
    }
    void render(Canvas canvas, Kruh kruh) {
        p.setARGB(255, R_SUCHÝ + (int) ((R_VLHKÝ - R_SUCHÝ) * kruh.vlhkost),
                       G_SUCHÝ + (int) ((G_VLHKÝ - G_SUCHÝ) * kruh.vlhkost),
                       B_SUCHÝ + (int) ((B_VLHKÝ - B_SUCHÝ) * kruh.vlhkost));
        circle(canvas, kruh.x, kruh.y, kruh.r);
        if (kruh.label != null) {
            p.setARGB(255, 0, 0, 0);
            // XXX no apparent effect on text: p.setMaskFilter(new EmbossMaskFilter(new float[] {5, -1, 3}, .7f, 8f, 3f));
            Rect bounds = new Rect();
            p.getTextBounds(kruh.label, 0, kruh.label.length(), bounds);
            text(canvas, kruh.x - bounds.right / 2, kruh.y - bounds.top / 2, kruh.label);
        }
    }
    void render(Canvas canvas, Slza slza, Oko oko) {
        float startDist = space.dist(oko.x, oko.y, slza.x, slza.y);
        float endDist = space.dist(oko.x, oko.y, slza.x + slza.vx, slza.y + slza.vy);
        double doppler = Math.tanh((endDist - startDist) * DOPPLER);
        //Log.v("Slzovod", "doppler=" + doppler + " for Δdist=" + (endDist - startDist));
        for (int i = 0; i < 5; i++) {
            p.setColor(Color.HSVToColor(180 - i * 20, new float[] {150 - (int) (60 * doppler), .7f, 1}));
            circle(canvas, slza.x - slza.vx * i / 2, slza.y - slza.vy * i / 2, 5 - i);
        }
    }
    void renderWarp(Canvas canvas, PointF warp) {
        p.setShader(new RadialGradient(warp.x, warp.y, 15, new int[] {Color.argb(255, 255, 255, 255), Color.argb(100, 255, 255, 255)}, null, Shader.TileMode.CLAMP));
        circle(canvas, warp.x, warp.y, 15);
        p.setShader(null);
    }
    private PointF[] wraps(float x, float y) {
        float ax = (float) Math.IEEEremainder(x + tx, space.w);
        if (ax < 0) {
            ax += space.w;
        }
        float ay = (float) Math.IEEEremainder(y + ty, space.h);
        if (ay < 0) {
            ay += space.h;
        }
        for (int wx = 0; wx < 3; wx++) {
            float _x = ax + space.w * wx - space.w;
            for (int wy = 0; wy < 3; wy++) {
                PointF point = wraps_tmp[wx * 3 + wy];
                point.x = _x;
                point.y = ay + space.h * wy - space.h;
            }
        }
        return wraps_tmp;
    }
    private void circle(Canvas canvas, float x, float y, float r) {
        for (PointF point : wraps(x, y)) {
             // XXX may be faster to precalculate which are definitely invisible
            canvas.drawCircle(point.x, point.y, r, p);
        }
    }
    private void oval(Canvas canvas, float x, float y, float rx, float ry) {
        for (PointF point : wraps(x, y)) {
            canvas.drawOval(new RectF(point.x - rx, point.y - ry, point.x + rx, point.y + ry), p);
        }
    }
    private void text(Canvas canvas, float x, float y, String text) {
        for (PointF point : wraps(x, y)) {
            canvas.drawText(text, point.x, point.y, p);
        }
    }
}
