// Copyright (c) 2019 Erik Kredatus. All rights reserved.


package com.kredatus.flockblockers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;

import javax.swing.Timer;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import static java.lang.Thread.sleep;


public class FlockBlockersMain extends Game {
    public static final boolean fastTest = false, dontPauseOnUnfocus = false;
    public static int birdType=1;
    public AssetHandler assetHandler=new AssetHandler();
    public GameHandler gameHandler;
    long startTime;
    @Override
    public void resize(int width, int height) {
        System.out.println("************************************");
        if (System.currentTimeMillis()-startTime>5000) {
            super.resize(width, height);
            gameHandler = new GameHandler();
            setScreen(gameHandler);
        }
    }

    @Override
    public void create() {
        startTime = System.currentTimeMillis();
        Gdx.app.log("CloudDefenders", "created");
        //this.wait();\
        assetHandler.load();
        gameHandler=new GameHandler();
        setScreen(gameHandler);
    }

    @Override
    public void dispose() {
        //super.dispose();
        assetHandler.dispose();
        GameRenderer.dispose();
    }
}