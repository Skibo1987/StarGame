package ru.geekbrains.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.impl.BulletPool;
import ru.geekbrains.pool.impl.EnemyPool;
import ru.geekbrains.screen.BaseScreen;
import ru.geekbrains.sprite.impl.Background;
import ru.geekbrains.sprite.impl.EnemyShip;
import ru.geekbrains.sprite.impl.MainShip;
import ru.geekbrains.sprite.impl.Star;
import ru.geekbrains.sprite.util.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private Texture bg;
//    private TextureAtlas ship;
    private Vector2 touch;
    private Vector2 v;
    private Background background;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;

    private static final int STAR_COUNT = 256;

    private TextureAtlas atlas;
    private Star[] stars;
    private MainShip mainShip;

    private Music music;
    private Sound lasersound;
    private Sound bulletSound;

    private EnemyEmitter enemyEmitter;


    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");
        background = new Background(bg);
//        ship = new TextureAtlas("mainAtlas.tpack");
        atlas = new TextureAtlas("mainAtlas.tpack");
        lasersound = new Gdx().audio.newSound(Gdx.files.internal("sound/laser.wav"));
        bulletSound = new Gdx().audio.newSound(Gdx.files.internal("sound/bullet.wav"));
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool,bulletSound,worldBounds);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        mainShip = new MainShip(atlas,bulletPool, lasersound);

        enemyEmitter = new EnemyEmitter(atlas,worldBounds,enemyPool);

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
        checkCollisions();
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
        bulletSound.dispose();
        enemyPool.dispose();
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
        enemyPool.updateActiveSprites(delta);
        enemyEmitter.generate(delta);
    }

    private void checkCollisions(){
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList){
            if (enemyShip.isDestroyed()){
                continue;
            }
            float minDist = (mainShip.getWidth()+enemyShip.getWidth())*0.5f;
            if (mainShip.pos.dst(enemyShip.pos) < minDist){
                enemyShip.destroy();
            }
        }
    }

    private void freeAllDestroyed(){
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {

            star.draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }

}
