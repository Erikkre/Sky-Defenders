// Copyright (c) 2019 Erik Kredatus. All rights reserved.

package com.kredatus.flockblockers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Screens.Loader;
import com.kredatus.flockblockers.TweenAccessors.BirdAccessor;
import com.kredatus.flockblockers.TweenAccessors.LightAccessor;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;
import com.kredatus.flockblockers.TweenAccessors.VectorAccessor;

import aurelienribon.tweenengine.Tween;
import box2dLight.Light;


public class FlockBlockersMain extends Game {
    public static final boolean fastTest = false, dontPauseOnUnfocus = true;
    public static int waveNumber;
    public Loader loader;
    public static long startTime;

    public FlockBlockersMain(){

        Tween.registerAccessor(Value.class, new ValueAccessor());
        Tween.registerAccessor(BirdAbstractClass.class, new BirdAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        Tween.registerAccessor(Light.class, new LightAccessor());
        Tween.setWaypointsLimit(10);
    }
    public void newGameHandler(Skin skin){

    }
    @Override
    public void resize(int width, int height) {//if after 5s since last screen created and done loading and width/height changed
        if (System.currentTimeMillis()-startTime>500 && (width!=loader.screenWidth || height!=loader.screenHeight)) {
            Gdx.app.log("CloudDefenders", "resized");
            super.resize(width, height);
            loader=new Loader(this);
            setScreen(loader);
        }
    }

    @Override
    public void create() {
        startTime = System.currentTimeMillis();
        Gdx.app.log("CloudDefenders", "created");

        loader=new Loader(this);
        this.setScreen(loader);

        waveNumber =Gdx.app.getPreferences("skyDefenders").getInteger("bgNumber",0)/9;
    }

    @Override
    public void dispose() {
        //super.dispose();
        //assetHandler.dispose();
        GameRenderer.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }
}