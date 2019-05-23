// Copyright (c) 2019 Erik Kredatus. All rights reserved.


package com.kredatus.flockblockers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Handlers.AssetHandler;

import static java.lang.Thread.sleep;


public class FlockBlockersMain extends Game {
    public static final boolean fastTest = false, dontPauseOnUnfocus = false;
    public static int birdType=1;
    public AssetHandler assetHandler=new AssetHandler();


    @Override
    public void create() {
        Gdx.app.log("CloudDefenders", "created");
        //this.wait();\
        assetHandler.load();
        setScreen(new GameHandler());
    }

    @Override
    public void dispose() {
        //super.dispose();
        assetHandler.dispose();
        GameRenderer.dispose();
    }
}