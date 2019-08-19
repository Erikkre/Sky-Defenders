// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kredatus.skydefenders.GameObjects.Airship;
import com.kredatus.skydefenders.GameObjects.Turret;
import com.kredatus.skydefenders.Handlers.BgHandler;
import com.kredatus.skydefenders.Handlers.BirdHandler;
import com.kredatus.skydefenders.Handlers.InputHandler;
import com.kredatus.skydefenders.Handlers.LightHandler;
import com.kredatus.skydefenders.Handlers.TargetHandler;
import com.kredatus.skydefenders.Handlers.TinyBirdHandler;
import com.kredatus.skydefenders.Handlers.UiHandler;
import com.kredatus.skydefenders.SkyDefendersMain;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */

public class GameHandler implements Screen {
    public GameWorld world;
    public GameRenderer renderer;
    public BgHandler bgHandler;
    public BirdHandler birdHandler;
    private TargetHandler targetHandler;
    //private TurretHandler turretHandler;
    public TinyBirdHandler tinyBirdHandler;
    public UiHandler uiHandler;
    public LightHandler lightHandler;
    public static float runTime;
    public boolean isPaused=false;
    public static double timeOfPause, timeOfResume;
    public int waveNumber = SkyDefendersMain.waveNumber;
    public Airship airship;
    //private Actor menuButtonActor;
    public static float screenWidth, screenHeight;
    public static int camWidth, camHeight;
    public Preferences prefs=Gdx.app.getPreferences("skyDefenders");
    public GameHandler(Skin shadeSkin, int camWidth, int camHeight) {
        screenWidth=((SkyDefendersMain)Gdx.app.getApplicationListener()).loader.screenWidth;screenHeight=((SkyDefendersMain)Gdx.app.getApplicationListener()).loader.screenHeight;
        this.camWidth=camWidth;this.camHeight=camHeight;
        world = new GameWorld(camWidth, camHeight);

        //System.out.println("width: "+camWidth);

        tinyBirdHandler = new TinyBirdHandler();

        birdHandler= new BirdHandler(camWidth, camHeight, waveNumber);
        bgHandler = new BgHandler(world,camWidth,camHeight,waveNumber,birdHandler);

        //turretHandler = new TurretHandler(camWidth, camHeight);
        lightHandler = new LightHandler(bgHandler);
        //bgHandler.survivalBgTweens(tinyBirdHandler,lightHandler);
        targetHandler = new TargetHandler(birdHandler);

        uiHandler=new UiHandler(world, camWidth, camHeight, shadeSkin);
        airship=new Airship(uiHandler,camWidth, camHeight, waveNumber, birdHandler,targetHandler,lightHandler);


        birdHandler.setAirshipPos(airship);
        targetHandler.setAirship(airship);

        uiHandler.loadSurvivalStage();
        world.startToSurvival(tinyBirdHandler,lightHandler,airship,bgHandler);

        renderer = new GameRenderer(world,lightHandler,tinyBirdHandler,birdHandler,bgHandler,targetHandler,uiHandler,airship, camWidth, camHeight);
        renderer.makeTransition(0, 0, 0, 1.7f);

        bgHandler.setRendererAndCam(renderer);
        lightHandler.setCam(renderer);

        world.initialize(bgHandler,birdHandler,targetHandler,tinyBirdHandler,uiHandler,lightHandler,renderer,airship);

        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(((SkyDefendersMain)Gdx.app.getApplicationListener()).loader.stage);
        im.addProcessor(new InputHandler(world,screenWidth / camWidth,screenHeight / camHeight, camWidth, camHeight));
        Gdx.input.setInputProcessor(im);
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
        Gdx.app.log("GameHandler", "pause called");
        if (!SkyDefendersMain.dontPauseOnUnfocus){
            isPaused=true;

            timeOfPause=System.currentTimeMillis();
            for (Turret i : airship.turretList){
                if (i.firing) {
                    i.stopFiring();
                    i.firingStoppedByGamePause = true;
                    i.stopTheFiringUpdateMethod=true;
                }
            }

            birdHandler.pause();
            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.stopMusic(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.menumusiclist);
        }

        prefs.putInteger("healthLvl",airship.healthLvl);
        prefs.putInteger("armorLvl",airship.armorLvl);
        prefs.putInteger("fuelLvl",airship.fuelLvl);
        prefs.putInteger("ammoLvl",airship.ammoLvl);
        prefs.putInteger("burnerLvl",airship.burnerLvl);
        prefs.putInteger("rackLvl",airship.rackLvl);
        prefs.putInteger("speedLvl",airship.speedLvl);

        prefs.putInteger("health",airship.health);
        prefs.putInteger("armor",airship.armor);
        prefs.putInteger("fuel",(int)airship.fuel);
        prefs.putInteger("ammo",airship.ammo);


        prefs.putInteger("bgNumber",bgHandler.bgNumber);

        prefs.putInteger("score",world.score);
        prefs.putInteger("gold",world.gold);
        prefs.putInteger("diamond",world.diamond);

        prefs.putInteger("lvl", uiHandler.rank.lvl);
        prefs.putInteger("exp",uiHandler.rank.expGained);

        prefs.putInteger("round",world.round);
        prefs.flush();
    }

    @Override
    public void pause() {
        Gdx.app.log("GameHandler", "pause called");
        if (!SkyDefendersMain.dontPauseOnUnfocus){
            isPaused=true;

            timeOfPause=System.currentTimeMillis();
            for (Turret i : airship.turretList){
                if (i.firing) {
                    i.stopFiring();
                    i.firingStoppedByGamePause = true;
                    i.stopTheFiringUpdateMethod=true;
                }
            }

            birdHandler.pause();
            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.stopMusic(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.menumusiclist);
        }

        prefs.putInteger("healthLvl",airship.healthLvl);
        prefs.putInteger("armorLvl",airship.armorLvl);
        prefs.putInteger("fuelLvl",airship.fuelLvl);
        prefs.putInteger("ammoLvl",airship.ammoLvl);
        prefs.putInteger("burnerLvl",airship.burnerLvl);
        prefs.putInteger("rackLvl",airship.rackLvl);
        prefs.putInteger("speedLvl",airship.speedLvl);

        prefs.putInteger("health",airship.health);
        prefs.putInteger("armor",airship.armor);
        prefs.putInteger("fuel",(int)airship.fuel);
        prefs.putInteger("ammo",airship.ammo);


        prefs.putInteger("bgNumber",bgHandler.bgNumber);

        prefs.putInteger("score",world.score);
        prefs.putInteger("gold",world.gold);
        prefs.putInteger("diamond",world.diamond);

        prefs.putInteger("lvl", uiHandler.rank.lvl);
        prefs.putInteger("exp",uiHandler.rank.expGained);

        prefs.putInteger("round",world.round);
        prefs.flush();
    }

    @Override
    public void resume() {
        Gdx.app.log("GameHandler", "resume called");
        ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.startMusic(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.menumusiclist);
        if (!SkyDefendersMain.dontPauseOnUnfocus) {
            isPaused = false;

            timeOfResume = System.currentTimeMillis();    //has to be before turrets restart firing so know how much longer left of interval before fire next shot

            for (Turret i : airship.turretList) {
                if (i.firingStoppedByGamePause) {   //can resume because firing would have been disabled for reasons like ammo=0 or damage before pause
                    i.firingStoppedByGamePause = false;
                    i.stopTheFiringUpdateMethod=false;
                    i.startFiring();
                }
            }

            birdHandler.resume();
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameHandler", "dispose called");

        uiHandler.timer.shutdownNow();
        uiHandler.stage.dispose();
        uiHandler.shadeSkin.dispose();
    }

}