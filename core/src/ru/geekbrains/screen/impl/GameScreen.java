package ru.geekbrains.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.impl.BulletPool;
import ru.geekbrains.screen.BaseScreen;
import ru.geekbrains.sprite.impl.Background;
import ru.geekbrains.sprite.impl.MainShip;
import ru.geekbrains.sprite.impl.Star;

public class GameScreen extends BaseScreen {

    private Texture bg;
//    private TextureAtlas ship;
    private Vector2 touch;
    private Vector2 v;
    private Background background;

    private BulletPool bulletPool;
    private static final int STAR_COUNT = 256;

    private TextureAtlas atlas;
    private Star[] stars;
    private MainShip mainShip;

    private Music music;
    private Sound lasersound;




    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");
        background = new Background(bg);
//        ship = new TextureAtlas("mainAtlas.tpack");
        atlas = new TextureAtlas("mainAtlas.tpack");
        lasersound = new Gdx().audio.newSound(Gdx.files.internal("sound/laser.wav"));
        bulletPool = new BulletPool();
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        mainShip = new MainShip(atlas,bulletPool, lasersound);
        touch = new Vector2();
        v = new Vector2(1, 1);

        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
//        batch.begin();
        update(delta);
        freeAllDestroyed();
        draw();

//        batch.draw(mainShip,touch.x,touch.y);
//        batch.end();
//        touch.add(v);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        music.dispose();
        lasersound.dispose();
//        ship.dispose();
    }

//    @Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
//        return super.touchDown(screenX, screenY, pointer, button);
//    }
//
    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch,pointer,button);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }

        mainShip.update(delta);
        bulletPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyed(){
        bulletPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {

            star.draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }

}
