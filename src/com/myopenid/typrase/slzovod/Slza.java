package com.myopenid.typrase.slzovod;
import android.graphics.PointF;
class Slza {
    float x;
    float y;
    float vx;
    float vy;
    void accelerate(PointF acceleration, float dt) {
        vx += acceleration.x * dt;
        vy += acceleration.y * dt;
    }
}
