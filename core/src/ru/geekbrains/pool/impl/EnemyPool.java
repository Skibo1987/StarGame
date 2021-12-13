package ru.geekbrains.pool.impl;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.SpritesPool;
import ru.geekbrains.sprite.impl.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {
    private final ExplosionPool explosionPool;
    private final BulletPool bulletPool;
    private final Sound bulletSound;
    private Rect worldBounds;

    public EnemyPool(ExplosionPool explosionPool, BulletPool bulletPool, Sound bulletSound, Rect worldBounds) {
        this.explosionPool = explosionPool;
        this.bulletPool = bulletPool;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(explosionPool, bulletPool, bulletSound, worldBounds);
    }
}
