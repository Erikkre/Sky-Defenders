// Copyright (c) 2019 Erik Kredatus. All rights reserved.

package com.kredatus.flockblockers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Screens.Loader;


public class FlockBlockersMain extends Game {
    public static final boolean fastTest = false, dontPauseOnUnfocus = false;
    public static int birdType=1;
    public GameHandler gameHandler;
    public Loader loader;
    long startTime;

    public void newGameHandler(Skin skin){

    }
    @Override
    public void resize(int width, int height) {//if after 5s since last screen created and done loading and width/height changed
        if (System.currentTimeMillis()-startTime>5000 && loader.gameHandler!=null && (width!=loader.screenWidth || height!=loader.screenHeight)) {
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
        setScreen(loader);

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