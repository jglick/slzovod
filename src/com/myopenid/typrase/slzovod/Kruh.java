package com.myopenid.typrase.slzovod;
class Kruh {
    private static final float VLHKOST_MULTIPLIER = 1;
    private static final int DENSITY = 7;
    final float x;
    final float y;
    final float r;
    final String label;
    /* [0,1) */
    float vlhkost;
    Kruh(float x, float y, float r, String label) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.label = label;
    }
    float mass() {
        return (1 + vlhkost * VLHKOST_MULTIPLIER) * (float) Math.pow(r, 2) * DENSITY;
    }
}
