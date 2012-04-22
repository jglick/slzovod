package com.myopenid.typrase.slzovod;
class Torus {
    final float w, h;
    Torus(float w, float h) {
        this.w = w;
        this.h = h;
    }
    float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(Math.IEEEremainder(x1 - x2, w), 2) + Math.pow(Math.IEEEremainder(y1 - y2, h), 2));
    }
}
