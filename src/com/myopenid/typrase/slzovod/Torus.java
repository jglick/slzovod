package com.myopenid.typrase.slzovod;
import android.graphics.PointF;
import static java.lang.Math.*;
class Torus {
    final float w, h;
    private final PointF tmp = new PointF();
    Torus(float w, float h) {
        this.w = w;
        this.h = h;
    }
    float dist(float x1, float y1, float x2, float y2) {
        return (float) sqrt(pow(IEEEremainder(x1 - x2, w), 2) + pow(IEEEremainder(y1 - y2, h), 2));
    }
    boolean crosses(float cx, float cy, float r, float x1, float y1, float x2, float y2) {
        // XXX take into consideration x1/y1
        return dist(cx, cy, x2, y2) < r;
    }
    PointF acceleration(float cx, float cy, float mass, float x, float y, float displacement) {
        float dx = (float) IEEEremainder(cx - x, w);
        float dy = (float) IEEEremainder(cy - y, h);
        float scale = mass * (float) pow(pow(dx, 2) + pow(dy, 2) + displacement, -1.5);
        tmp.x = dx * scale;
        tmp.y = dy * scale;
        return tmp;
    }
}
