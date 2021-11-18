package ru.geekbrains;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    TextureRegion region;
    int x, y;


    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        region = new TextureRegion(img, 30, 40, 100, 150);

    }

    @Override
    public void render() {
        x++;
        y++;
        ScreenUtils.clear(Color.BROWN);
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(img, x, y);
        batch.draw(img, 120, 120, 50, 50);
        batch.draw(region, 250, 400, 100, 150);

//		Gdx.graphics.getHeight();
//		Gdx.graphics.getWidth();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
