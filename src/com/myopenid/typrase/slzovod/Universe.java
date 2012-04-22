package com.myopenid.typrase.slzovod;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
class Universe {
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
        oko = new Oko(width / 2, height / 2, 20, 10);
    }
    void populate(int count) {
        float minsize = Math.min(space.w, space.h) / 20;
        float maxsize = Math.min(space.w, space.h) / 10;
        for (int i = 0; i < count; i++) {
            KRUH: while (true) {
                Kruh k = new Kruh(rand.nextFloat() * space.w, rand.nextFloat() * space.h, minsize + rand.nextFloat() * (maxsize - minsize));
                if (space.dist(k.x, k.y, oko.x, oko.y) < k.r + oko.rx + 10) {
                    continue KRUH;
                }
                for (Kruh k2 : kruhy) {
                    if (space.dist(k.x, k.y, k2.x, k2.y) < k.r + k2.r + 10) {
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
        s.x = oko.x + 10;
        s.y = oko.y + 10;
        s.vx = 7;
        s.vy = 7;
        slzy.add(s);
    }
    void step() {
        float _t = (System.currentTimeMillis() - start) / 1000f;
        float dt = _t - t;
        t = _t;
        //Log.v("Slzovod", "step @" + t);
        if (t > mrknutí + 5) {
            mrknutí = t;
            mrkni();
        }
        for (Slza s : slzy) {
            s.x += s.vx * dt;
            s.y += s.vy * dt;
        }
    }
}
