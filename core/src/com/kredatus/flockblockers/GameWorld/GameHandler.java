// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameObjects.Turret;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;
import com.kredatus.flockblockers.Handlers.TinyBirdHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */

public class GameHandler implements Screen {
    public GameWorld world;
    private GameRenderer renderer;
    public BgHandler bgHandler;
    public BirdHandler birdHandler;
    private TargetHandler targetHandler;
    //private TurretHandler turretHandler;
    private TinyBirdHandler tinyBirdHandler;
    public UiHandler uiHandler;
    public LightHandler lightHandler;
    public static float runTime;
    public boolean isPaused=false;
    public static double timeOfPause, timeOfResume;
    public int birdType = FlockBlockersMain.birdType;
    private Airship airship;
    //private Actor menuButtonActor;
    public float screenWidth, screenHeight;
    public static int camWidth, camHeight;
    public GameHandler(Skin shadeSkin, int camWidth, int camHeight) {
        screenWidth=((FlockBlockersMain)Gdx.app.getApplicationListener()).loader.screenWidth;screenHeight=((FlockBlockersMain)Gdx.app.getApplicationListener()).loader.screenHeight;
        this.camWidth=camWidth;this.camHeight=camHeight;
        //System.out.println("width: "+camWidth);

        tinyBirdHandler = new TinyBirdHandler();

        birdHandler= new BirdHandler(camWidth, camHeight, birdType);
        bgHandler = new BgHandler(camWidth, camHeight, birdType,birdHandler,tinyBirdHandler);

        //turretHandler = new TurretHandler(camWidth, camHeight);
        lightHandler= new LightHandler(bgHandler);
        bgHandler.setupTweens(camWidth, camHeight,tinyBirdHandler,lightHandler);
        targetHandler = new TargetHandler(birdHandler);
        airship=new Airship(camWidth, camHeight, birdType, birdHandler,targetHandler,lightHandler);
        birdHandler.setAirshipPos(airship);
        targetHandler.setAirship(airship);

        world = new GameWorld(airship, camWidth, camHeight, bgHandler,birdHandler,targetHandler,tinyBirdHandler,uiHandler,lightHandler);


        renderer = new GameRenderer(world, camWidth, camHeight);
        renderer.prepareTransition(0,0,0,3);
        bgHandler.setRendererAndCam(renderer);
        lightHandler.setCam(renderer);


        uiHandler=new UiHandler(camWidth, camHeight, world, shadeSkin);
        world.setRendererAndUIHandler(renderer, uiHandler);
        InputHandler inputHandler=new InputHandler(world, screenWidth / camWidth, screenHeight / camHeight, camWidth, camHeight);

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
            ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.stopMusic(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.menumusiclist);
        }
    }

    @Override
    public void resume() {
        Gdx.app.log("GameHandler", "resume called");
        ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.startMusic(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.menumusiclist);
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
        uiHandler.shadeSkin.dispose();
    }

}