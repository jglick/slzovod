package com.myopenid.typrase.slzovod;
class Kruh {
    final float x;
    final float y;
    final float r;
    /* [0,1) */
    float vlhkost;
    Kruh(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
    boolean overlaps(Kruh o) {
        // XXX convert to toroidal
        return Math.sqrt(Math.pow(x - o.x, 2) + Math.pow(y - o.y, 2)) < r + o.r;
    }
    float mass() {
        return (1 + vlhkost) * (float) Math.pow(r, 2);
    }
}
