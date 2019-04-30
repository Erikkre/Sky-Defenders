// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Turret;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;
import com.kredatus.flockblockers.Handlers.TinyBirdHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.TweenAccessors.BirdAccessor;
import com.kredatus.flockblockers.TweenAccessors.LightAccessor;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;
import com.kredatus.flockblockers.TweenAccessors.VectorAccessor;
import com.kredatus.flockblockers.ui.SlideMenu;

import aurelienribon.tweenengine.Tween;
import box2dLight.Light;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */

public class GameHandler implements Screen {
    private GameWorld world;
    private GameRenderer renderer;
    public BgHandler bgHandler;
    private BirdHandler birdHandler;
    private TargetHandler targetHandler;
    //private TurretHandler turretHandler;
    private TinyBirdHandler tinyBirdHandler;
    public UiHandler uiHandler;
    public LightHandler lightHandler;
    public static float runTime;
    public static int camWidth, camHeight;
    public boolean isPaused=false;
    public static double timeOfPause, timeOfResume;
    public int birdType = FlockBlockersMain.birdType;
    private Airship airship;
    //private Actor menuButtonActor;

    public GameHandler() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        camHeight=1000;
        camWidth= (int) (camHeight* (screenWidth/screenHeight)) ;
        //System.out.println("width: "+camWidth);

        Tween.registerAccessor(Value.class, new ValueAccessor());
        Tween.registerAccessor(BirdAbstractClass.class, new BirdAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        Tween.registerAccessor(Light.class, new LightAccessor());
        Tween.setWaypointsLimit(10);

        tinyBirdHandler = new TinyBirdHandler();
        bgHandler = new BgHandler(camWidth, camHeight, birdType);
        birdHandler= new BirdHandler(bgHandler, camWidth, camHeight, birdType);

        //turretHandler = new TurretHandler(camWidth, camHeight);
        lightHandler= new LightHandler(bgHandler);
        airship=new Airship(camWidth, camHeight, birdType);
        targetHandler = new TargetHandler(airship);

        world = new GameWorld(airship, camWidth, camHeight, bgHandler,birdHandler, targetHandler,tinyBirdHandler,uiHandler, lightHandler);


        renderer = new GameRenderer(world, camWidth, camHeight);

        bgHandler.setRendererAndCam(renderer);
        lightHandler.setCam(renderer);




        uiHandler=new UiHandler(renderer.viewport, renderer.batcher, camWidth, camHeight);
        world.setRendererAndUIHandler(renderer, uiHandler);
        InputHandler inputHandler=new InputHandler(world, screenWidth / camWidth, screenHeight / camHeight, camWidth, camHeight);
        Gdx.input.setInputProcessor(uiHandler.stage);
        renderer.assignButtonsUsingInputHandlerAndUiHandler(inputHandler, uiHandler);
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            runTime += delta;
            world.update(delta, runTime);
            renderer.render(delta, runTime);
            //Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
            //            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

    @Override
    public void resize(int width, int height) {
        uiHandler.stage.getViewport().update(width, height, true);
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
        if (!FlockBlockersMain.dontPauseOnUnfocus){
            isPaused=true;

            timeOfPause=System.currentTimeMillis();
            for (Turret i : airship.turretList){
                if (i.firing) {
                    i.stopFiring();
                    i.firingStoppedByGamePause = true;
                }
            }

            birdHandler.pause();
            AssetHandler.stopMusic(AssetHandler.menumusiclist);
        }
    }

    @Override
    public void resume() {
        Gdx.app.log("GameHandler", "resume called");
        AssetHandler.startMusic(AssetHandler.menumusiclist);
        if (!FlockBlockersMain.dontPauseOnUnfocus) {
            isPaused = false;

            timeOfResume = System.currentTimeMillis();    //has to be before turrets restart firing so know how much longer left of interval before fire next shot

            for (Turret i : airship.turretList) {
                if (i.firingStoppedByGamePause) {
                    i.startFiring();
                    i.firingStoppedByGamePause = false;
                }
            }

            birdHandler.resume();
        }
    }

    @Override
    public void dispose() {
        uiHandler.stage.dispose();
        uiHandler.skin.dispose();
    }

}