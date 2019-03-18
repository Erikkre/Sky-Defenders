// Copyright (c) 2019 Erik Kredatus. All rights reserved.


package com.kredatus.flockblockers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Screens.SplashScreen;


public class FlockBlockersMain extends Game {
    public static final boolean fastTest = false, dontPauseOnUnfocus = true;
    public static int birdType=0;
    @Override
    public void create() {
        Gdx.app.log("CloudDefenders", "created");
        AssetHandler.load();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetHandler.dispose();
        GameRenderer.dispose();
    }
}