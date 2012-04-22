package com.myopenid.typrase.slzovod;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
class Universe {
    final float width;
    final float height;
    final Oko oko;
    final Collection<Kruh> kruhy = new LinkedList<Kruh>();
    final Collection<Slza> slzy = new LinkedList<Slza>();
    private final Random rand = new Random();
    Universe(float width, float height) {
        this.width = width;
        this.height = height;
        oko = new Oko(width / 2, height / 2);
    }
    void populate(int count) {
        float minsize = Math.min(width, height) / 20;
        float maxsize = Math.min(width, height) / 10;
        for (int i = 0; i < count; i++) {
            KRUH: while (true) {
                Kruh k = new Kruh(rand.nextFloat() * width, rand.nextFloat() * height, minsize + rand.nextFloat() * (maxsize - minsize));
                for (Kruh k2 : kruhy) {
                    // XXX reject also those which are simply too close
                    if (k.overlaps(k2)) {
                        continue KRUH;
                    }
                    // XXX check overlap w/ oko
                }
                kruhy.add(k);
                break;
            }
        }
    }
    void mrkni() {
        // XXX
    }
}
