package ru.koshakmine.icstd.render.animation;

import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.runtime.Updatable;

public class AnimationItem extends AnimationStaticItem {
    private float speed = 0.03f;

    public AnimationItem(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public void updateRender() {
        setInterpolationEnabled(true);
        super.updateRender();
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public synchronized void load() {
        super.load();

        Updatable.addUpdatable(NetworkSide.LOCAL, new Updatable() {
            private final Transform transform = getTransform();

            @Override
            public boolean update() {
                transform.lock();
                transform.rotate(0, speed, 0);
                transform.unlock();

                return !isLoaded();
            }
        });
    }
}
