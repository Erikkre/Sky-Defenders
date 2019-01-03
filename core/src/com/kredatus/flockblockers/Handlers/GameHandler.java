package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.TweenAccessors.BirdAccessor;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;

import aurelienribon.tweenengine.Tween;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */

public class GameHandler implements Screen {
    private GameWorld world;
    private GameRenderer renderer;
    private BgHandler bgHandler;
    private BirdHandler birdHandler;
    private TargetHandler targetHandler;
    private TurretHandler turretHandler;
    public static UiHandler uiHandler;
    private float runTime;
    public static int camWidth, midPointY, camHeight, midPointX;

    public GameHandler() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        camHeight=1280;
        camWidth= (int) (camHeight* (screenWidth/screenHeight)) ;
        //System.out.println("width: "+camWidth);
        midPointY = camHeight/2;
        midPointX = camWidth/2;

        Tween.registerAccessor(Value.class, new ValueAccessor());
        Tween.registerAccessor(BirdAbstractClass.class, new BirdAccessor());
        Tween.setWaypointsLimit(10);

        uiHandler=new UiHandler();
        world = new GameWorld();
        Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / camWidth, screenHeight / camHeight, camWidth, camHeight));
        bgHandler = new BgHandler( camWidth, camHeight);
        birdHandler= new BirdHandler(bgHandler, camWidth, camHeight);
        targetHandler = new TargetHandler();
        turretHandler = new TurretHandler(camWidth, camHeight);
        world.setHandlers(bgHandler,birdHandler, targetHandler,turretHandler,uiHandler);

        renderer = new GameRenderer(world, camWidth, camHeight, bgHandler, birdHandler, targetHandler, turretHandler,uiHandler);
        bgHandler.setRendererAndCam(renderer);
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
        Gdx.app.log( Integer.toString(Gdx.graphics.getWidth()),Integer.toString(Gdx.graphics.getHeight()) );
        Gdx.app.log("GameHandler", "show called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameHandler", "hide called");
    }

    @Override
    public void pause() {
        Gdx.app.log("GameHandler", "pause called");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameHandler", "resume called");
    }

    @Override
    public void dispose() {
        // Leave blank
    }

}