package ru.geekbrains.sprite.impl;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Sprite;

public class GameOver extends Sprite {
    private static final float HEIGHT = 0.08f;
    private static final float MARGIN = 0.04f;

    public GameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProtection(HEIGHT);
        setBottom(MARGIN);
    }
}
