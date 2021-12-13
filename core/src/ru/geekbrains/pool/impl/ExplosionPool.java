package ru.geekbrains.pool.impl;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.pool.SpritesPool;
import ru.geekbrains.sprite.impl.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {
    private final TextureAtlas atlas;
    private final Sound explosionSound;

    public ExplosionPool(TextureAtlas atlas, Sound explosionSound) {
        this.atlas = atlas;
        this.explosionSound = explosionSound;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas,explosionSound);
    }



}
