package ru.geekbrains.sprite.impl;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.impl.GameScreen;
import ru.geekbrains.sprite.BaseButton;


public class ButtonNewGame extends BaseButton {

    private final GameScreen gameScreen;
    private static final float HEIGHT = 0.05f;

    public ButtonNewGame(TextureAtlas atlas,GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }



    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProtection(HEIGHT);
        setBottom(-0.01f);
    }

    @Override
    public void action() {
        gameScreen.startNewGame();
    }
}
