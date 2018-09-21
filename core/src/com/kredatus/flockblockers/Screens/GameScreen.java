package com.kredatus.flockblockers.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.GameWorld.ExGameWorld;
import com.kredatus.flockblockers.GlideOrDieHelpers.InputHandler;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */

public class GameScreen implements Screen {
    private ExGameWorld world;
    private GameRenderer renderer;
    private float runTime;
    public static int camwidth, midPointY, camheight, midPointX;

    public GameScreen() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        camwidth=1100;
        camheight= (int) (camwidth* (screenHeight/screenWidth)) ;

        midPointY = camheight/2;
        midPointX = camwidth/2;

        world = new ExGameWorld(midPointY, midPointX, camwidth);
        Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / camwidth, screenHeight / camheight, camwidth, camheight));
        renderer = new GameRenderer(world, camwidth, camheight);
        world.setRenderer(renderer);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta, runTime);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.app.log(Integer.toString(Gdx.graphics.getWidth()),Integer.toString(Gdx.graphics.getHeight()) );
        Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void hide() {

        Gdx.app.log("GameScreen", "hide called");
    }

    @Override
    public void pause() {

        Gdx.app.log("GameScreen", "pause called");
    }

    @Override
    public void resume() {

        Gdx.app.log("GameScreen", "resume called");
    }

    @Override
    public void dispose() {
        // Leave blank
    }

}