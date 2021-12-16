package ru.geekbrains.screen.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.font.Font;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.impl.BulletPool;
import ru.geekbrains.pool.impl.EnemyPool;
import ru.geekbrains.pool.impl.ExplosionPool;
import ru.geekbrains.screen.BaseScreen;
import ru.geekbrains.sprite.impl.Background;
import ru.geekbrains.sprite.impl.Bullet;
import ru.geekbrains.sprite.impl.ButtonNewGame;
import ru.geekbrains.sprite.impl.EnemyShip;
import ru.geekbrains.sprite.impl.GameOver;
import ru.geekbrains.sprite.impl.MainShip;
import ru.geekbrains.sprite.impl.Star;
import ru.geekbrains.sprite.impl.TrackingStar;
import ru.geekbrains.sprite.util.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private Texture bg;
    //    private TextureAtlas ship;
    private Vector2 touch;
    private Vector2 v;
    private Background background;
    private GameOver gameOver;
    private ButtonNewGame buttonNewGame;

    private ExplosionPool explosionPool;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;

    private static final int STAR_COUNT = 64;
    private static final float MARGIN = 0.01f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String Level = "Level: ";


    private TextureAtlas atlas;
    private TrackingStar[] stars;
    private MainShip mainShip;

    private Music music;
    private Sound lasersound;
    private Sound bulletSound;
    private Sound explosionSound;

    private EnemyEmitter enemyEmitter;

    private int frags;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;



    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");
        background = new Background(bg);
//        ship = new TextureAtlas("mainAtlas.tpack");
        atlas = new TextureAtlas("mainAtlas.tpack");
        lasersound = new Gdx().audio.newSound(Gdx.files.internal("sound/laser.wav"));
        bulletSound = new Gdx().audio.newSound(Gdx.files.internal("sound/bullet.wav"));
        explosionSound = new Gdx().audio.newSound(Gdx.files.internal("sound/explosion.wav"));

        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(0.02f);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();


        gameOver = new GameOver(atlas);
        bulletPool = new BulletPool();
        buttonNewGame = new ButtonNewGame(atlas,this);
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(explosionPool, bulletPool, bulletSound, worldBounds);
        mainShip = new MainShip(atlas, explosionPool, bulletPool, lasersound);
        stars = new TrackingStar[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new TrackingStar(atlas,mainShip.getV());
        }


        enemyEmitter = new EnemyEmitter(atlas, worldBounds, enemyPool);

        touch = new Vector2();
        v = new Vector2(1, 1);

        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        music.setLooping(true);
        music.play();
        frags = 0;
    }

    public void startNewGame(){
        frags = 0;

        mainShip.startNewGame();
        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
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
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        explosionPool.dispose();
        bulletPool.dispose();
        music.dispose();
        lasersound.dispose();
        bulletSound.dispose();
        enemyPool.dispose();
        explosionSound.dispose();
        font.dispose();
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
        if (mainShip.isDestroyed()) {
            buttonNewGame.touchDown(touch, pointer, button);
        }else {
            mainShip.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (mainShip.isDestroyed()) {
            buttonNewGame.touchUp(touch, pointer, button);
        }else {
        mainShip.touchUp(touch, pointer, button);
        }
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
        if (!mainShip.isDestroyed()) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        }
        explosionPool.updateActiveSprites(delta);
    }

    private void checkCollisions() {

        if (mainShip.isDestroyed()) {
            return;
        }

        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList) {
            if (enemyShip.isDestroyed()) {
                continue;
            }
            float minDist = (mainShip.getWidth() + enemyShip.getWidth()) * 0.5f;
            if (mainShip.pos.dst(enemyShip.pos) < minDist) {
                mainShip.damage(enemyShip.getHp() * 2);
                enemyShip.destroy();
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed()) {
                continue;
            }
            if (bullet.getOwner() != mainShip) {
                if (mainShip.isBulletCollision(bullet)) {
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
                continue;
            }
            for (EnemyShip enemyShip : enemyShipList) {
                if (enemyShip.isDestroyed()) {
                    continue;
                }
                if (enemyShip.isBulletCollision(bullet)) {
                    enemyShip.damage(bullet.getDamage());
                    if (enemyShip.isDestroyed()){
                        frags++;
                    }
                    bullet.destroy();
                }

            }
        }
    }

    private void freeAllDestroyed() {
        explosionPool.freeAllDestroyed();
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {

            star.draw(batch);
        }
        if (!mainShip.isDestroyed()) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else {
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo(){
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + MARGIN,worldBounds.getTop() - MARGIN);
        sbHP.setLength(0);
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x,worldBounds.getTop() - MARGIN, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(Level).append(enemyEmitter.getLevel()), worldBounds.getRight() - MARGIN,worldBounds.getTop() - MARGIN,Align.right);
    }
}
