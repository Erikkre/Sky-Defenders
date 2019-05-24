// Copyright (c) 2019 Erik Kredatus. All rights reserved.


package com.kredatus.flockblockers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Handlers.AssetHandler;



public class FlockBlockersMain extends Game {
    public static final boolean fastTest = false, dontPauseOnUnfocus = false;
    public static int birdType=1;
    public AssetHandler assetHandler=new AssetHandler();
    public GameHandler gameHandler;
    long startTime;

    @Override
    public void resize(int width, int height) {
        System.out.println("************************************");
        if (System.currentTimeMillis()-startTime>5000 && (width!=gameHandler.screenWidth || height!=gameHandler.screenHeight)) {
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