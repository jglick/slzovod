package com.myopenid.typrase.slzovod;
import android.graphics.PointF;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
class Universe {
    private static final float GAP = 10;
    private static final float MIN_KRUH_SIZE_FRACTION = 20;
    private static final float MAX_KRUH_SIZE_FRACTION = 10;
    private static final float OKO_WIDTH = 20;
    private static final float OKO_HEIGHT = 10;
    private static final float SLZA_VELOCITY = 7;
    private static final float VLHČENÍ = .9f;
    private static final float MRKNUTÍ_DELAY = 5;
    final Torus space;
    final Oko oko;
    final Collection<Kruh> kruhy = new LinkedList<Kruh>();
    final Collection<Slza> slzy = new LinkedList<Slza>();
    private final long start = System.currentTimeMillis();
    private float t;
    private float mrknutí;
    private final Random rand = new Random();
    Universe(float width, float height) {
        space = new Torus(width, height);
        oko = new Oko(width / 2, height / 2, OKO_WIDTH, OKO_HEIGHT);
    }
    void populate(int count) {
        float minsize = Math.min(space.w, space.h) / MIN_KRUH_SIZE_FRACTION;
        float maxsize = Math.min(space.w, space.h) / MAX_KRUH_SIZE_FRACTION;
        for (int i = 0; i < count; i++) {
            KRUH: while (true) {
                Kruh k = new Kruh(rand.nextFloat() * space.w, rand.nextFloat() * space.h, minsize + rand.nextFloat() * (maxsize - minsize));
                if (space.dist(k.x, k.y, oko.x, oko.y) < k.r + oko.rx + GAP) {
                    continue KRUH;
                }
                for (Kruh k2 : kruhy) {
                    if (space.dist(k.x, k.y, k2.x, k2.y) < k.r + k2.r + GAP) {
                        continue KRUH;
                    }
                }
                kruhy.add(k);
                break;
            }
        }
    }
    void mrkni() {
        Slza s = new Slza();
        double theta = rand.nextFloat() * Math.PI * 2;
        float x = (float) Math.cos(theta);
        float y = (float) Math.sin(theta);
        s.x = oko.x + oko.rx * x;
        s.y = oko.y + oko.ry * y;
        s.vx = SLZA_VELOCITY * x;
        s.vy = SLZA_VELOCITY * y;
        slzy.add(s);
    }
    void step() {
        float _t = (System.currentTimeMillis() - start) / 1000f;
        float dt = _t - t;
        t = _t;
        if (t > mrknutí + MRKNUTÍ_DELAY) {
            mrknutí = t;
            mrkni();
        }
        for (Iterator<Slza> it = slzy.iterator(); it.hasNext();) {
            Slza s = it.next();
            float oldx = s.x;
            float oldy = s.y;
            s.x = oldx + s.vx * dt;
            s.y = oldy + s.vy * dt;
            for (Kruh k : kruhy) {
                PointF accel = space.acceleration(k.x, k.y, k.mass(), oldx, oldy);
                s.vx += accel.x * dt;
                s.vy += accel.y * dt;
                if (space.crosses(k.x, k.y, k.r, oldx, oldy, s.x, s.y)) {
                    k.vlhkost = 1 - (VLHČENÍ * (1 - k.vlhkost));
                    it.remove();
                    break;
                }
            }
        }
    }
}
