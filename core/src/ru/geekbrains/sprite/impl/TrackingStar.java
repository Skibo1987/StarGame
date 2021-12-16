package ru.geekbrains.sprite.impl;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class TrackingStar extends Star {

    private final Vector2 trackingV;
    private final Vector2 sum2 = new Vector2();

    public TrackingStar(TextureAtlas atlas, Vector2 trackingV) {
        super(atlas);
        this.trackingV = trackingV;
    }

    @Override
    public void update(float delta) {
        sum2.setZero().mulAdd(trackingV, 0.2f).rotateDeg(180).add(v);
        pos.mulAdd(sum2,delta);
        checkBounds();
    }
}
