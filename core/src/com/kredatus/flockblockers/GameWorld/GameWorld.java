// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;
import com.kredatus.flockblockers.Handlers.TinyBirdHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.Screens.SplashScreen;
import com.kredatus.flockblockers.TweenAccessors.Value;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */
public class GameWorld {

    public BgHandler bgHandler;
    public BirdHandler birdHandler;
    public TargetHandler targetHandler;
    //public TurretHandler turretHandler;
    public TinyBirdHandler tinyBirdHandler;
    public UiHandler uiHandler;
    private LightHandler lightHandler;
    //private boolean isAlive = true;
    private Rectangle ground;
    public Airship airship;
    public double boost = 0;  //boostamount
    public int updatedboostnumber, orgboostnumber= AssetHandler.getBoostnumber();

    private static GameRenderer renderer;
    public enum GameState {
        MENU, SURVIVAL, OPTIONS, BUYMENU, CREDITS, DEATHMENU, INSTR, LOGOS
    }
    public static boolean isFirstTime;
    private GameState currentState;

    public static void addGold(int goldAddition) {
            gold += goldAddition;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static int gold, diamonds, score;

    public Value alpha =new Value(0);
    public Tween logoTween, timerTween;
    public boolean startGame;
    private TextureRegion logo,logoBg;
    private float logoScale;

    public GameWorld(Airship airship, int camWidth, int camHeight, BgHandler bgHandler, BirdHandler birdHandler, TargetHandler targetHandler, TinyBirdHandler tinyBirdHandler, UiHandler uiHandler, LightHandler lightHandler) {
        this.bgHandler = bgHandler;
        this.birdHandler = birdHandler;
        this.targetHandler = targetHandler;
        //this.turretHandler=turretHandler;
        this.tinyBirdHandler=tinyBirdHandler;
        this.uiHandler=uiHandler;
        this.lightHandler=lightHandler;
        this.airship=airship;

        /*if (AssetHandler.getHighScore()==0){
            isFirstTime=true;
            currentState= GameState.SURVIVAL;
        } else {
            currentState = GameState.MENU;
        }
*/
        //this.camWidth=camWidth;
        //this.midPointY=midPointY;
        //glider = new Glider(0, 0, AssetHandler.frontFlaps.getKeyFrame(0).getRegionWidth(), AssetHandler.frontFlaps.getKeyFrame(0).getRegionHeight(), this);

        AssetHandler.playnext(AssetHandler.menumusiclist);
        updatedboostnumber=orgboostnumber;

        startLogos();
    }

    public void startLogos() {
        LightHandler.rayHandlerAmbLightLvl=0;
        currentState=GameState.LOGOS;
        logo=AssetHandler.logo;
        logoBg=AssetHandler.slidemenuBg;

        /*Sprite temp=new Sprite(AssetHandler.slidemenuBg);
        temp.setColor(new Color(0,0,0,0.5f));
        final Image image_backgroundX = new Image(new SpriteDrawable(temp));*/
        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if (!startGame) {
                    startGame = true;
                    System.out.println("start game");
                } else {
                    currentState=GameState.SURVIVAL;
                    System.out.println("switch to survival");
                }
            }
        };

        float dur=1f;
        logoScale=(float)GameHandler.camHeight/logo.getRegionHeight()/2;
        logoTween=(Tween.to(alpha, 0, dur).target(1)
                .ease(TweenEquations.easeInOutQuad).repeatYoyo(1, .0f)).setCallback(cb).setCallbackTriggers(TweenCallback.END)
                .start();

        timerTween=Tween.to(0, 0, dur).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE);
    }

    public void drawLogos(SpriteBatch batch, float camWidth, float camHeight){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        //glBlendEquation();
        //Gdx.gl.glClearColor(1, 1, 1, alpha.get());
        System.out.println(alpha.get());
        //Gdx.gl.glClear( GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        batch.setColor(1,1,1,alpha.get());
        batch.draw(logo,camWidth/2 - logo.getRegionWidth()*logoScale/2,camHeight/2-logo.getRegionHeight()*logoScale/2, logo.getRegionWidth()*logoScale, logo.getRegionHeight()*logoScale);
        batch.setColor(1,1,1,1);
    }

    public void update(float delta, float runTime) {
        switch (currentState) {
            case MENU:
                updateMenu(delta,runTime);
                break;
            case BUYMENU:
                updateBuyMenu(delta,runTime);
                break;
            case SURVIVAL:
                updateSurvival(delta, runTime);
                break;
            case LOGOS:
                updateLogos(delta, runTime);
                break;
            default:
                break;
        }
    }

    private void updateSurvival(float delta, float runTime) {
        bgHandler.update(delta);
        birdHandler.update();
        //turretHandler.update();
        uiHandler.update(delta);
        airship.update(delta);
        targetHandler.update(delta, runTime);
        tinyBirdHandler.update(delta);
        lightHandler.update();
        LightHandler.foreRayHandler.update();  //used for airship and gun lights too
        LightHandler.backRayHandler.update();
    }

    private void updateLogos(float delta, float runTime) {
        timerTween.update(delta);
        logoTween.update(delta);
        if (startGame) {
            bgHandler.update(delta);
            birdHandler.update();
            //turretHandler.update();
            uiHandler.update(delta);
            airship.update(delta);
            targetHandler.update(delta, runTime);
            tinyBirdHandler.update(delta);
            lightHandler.update();
            LightHandler.foreRayHandler.update();  //used for airship and gun lights too
            LightHandler.backRayHandler.update();
        }
    }

    private void updateMenu(float delta, float runTime) {
        bgHandler.update(delta);
        birdHandler.update();
        //turretHandler.update();
        uiHandler.update(delta);
        airship.update(delta);
        targetHandler.update(delta, runTime);
        tinyBirdHandler.update(delta);
        lightHandler.update();
        LightHandler.foreRayHandler.update();  //used for airship and gun lights too
        LightHandler.backRayHandler.update();
    }

    private void updateBuyMenu(float delta, float runTime) {
        bgHandler.update(delta);
        birdHandler.update();
        //turretHandler.update();
        uiHandler.update(delta);
        airship.update(delta);
        targetHandler.update(delta, runTime);
        tinyBirdHandler.update(delta);
        lightHandler.update();
        LightHandler.foreRayHandler.update();  //used for airship and gun lights too
        LightHandler.backRayHandler.update();
    }

    private void updateReady(float runTime) {
        //glider.updateReady(runTime);
    }

    private void updateRunning(float delta, float runTime) {
        updatedboostnumber=(int)((-orgboostnumber*renderer.scorenumber/105f)+orgboostnumber);//keep rendering boosts until 130
        //System.out.println((int)((-renderer.scorenumber/5f)+orgboostnumber));
        if (delta > .15f) {
            delta = .15f;}
        //bgHandler.collides(glider, updatedboostnumber);

        bgHandler.update(delta);
        /*if (Math.abs(glider.getPosition().y) > bgHandler.bgh) {
           // renderer.prepareSunshine();
            currentState = GameState.DEATHMENU;
            if (renderer.scorenumber > AssetHandler.getHighScore()) {
                AssetHandler.setHighScore(renderer.scorenumber);
            }
            //bgHandler.onRestart();
           // AssetHandler.frontViewFlaps.setFrameDuration(0.2f);
           //renderer.setCamPositionOriginal();
            renderer.prepareTransition(255, 255, 255, 1);
        }*/
    }

    public void addBoost(double increment) {
        boost += increment;
    }


    public BgHandler getbgHandler() {
        return bgHandler;
    }

    public void start() {
        currentState = GameState.SURVIVAL;
        boost=5;
        //AssetHandler.frontFlaps.setFrameDuration(0.12f);
    }

    public void restart() {
        boost = 0;
        //glider.onRestart();
        //renderer.setCamPositionOriginal();
        renderer.scorenumber=0;
        AssetHandler.deathmenumusic.stop();
        AssetHandler.playnext(AssetHandler.musiclist);
        //AssetHandler.frontFlaps.setFrameDuration(0.2f);
        //currentState = GameState.READY;
        renderer.prepareTransition(0, 0, 0, 1f);}

    public void buyMenu() {
        currentState = GameState.BUYMENU;
    }

    public void menu() {
        boost = 0;
        //glider.onRestart();
        //bgHandler.onRestart();
        //renderer.setCamPositionOriginal();
        currentState = GameState.MENU;
        renderer.prepareTransition(0, 0, 0, 1f);}



    public void ready() {
        //renderer.sunshineManager.killAll();
        //renderer.sunshineManager2.killAll();
        SplashScreen.getManager().killAll();
        AssetHandler.stopMusic(AssetHandler.menumusiclist);
        AssetHandler.playnext(AssetHandler.musiclist);
        //currentState = GameState.READY;
        renderer.prepareTransition(0, 0, 0, 1f);}

    public void exit() {
        Gdx.app.exit();
    }

    public void story() {
        renderer.prepareTransition(0, 0, 0, 1f);
        //currentState = GameState.STORY;
        }

    public void credits() {
        renderer.prepareTransition(0, 0, 0, 1f);
        currentState = GameState.CREDITS;}

    public void instr() {
        renderer.prepareTransition(0, 0, 0, 1f);
        currentState = GameState.INSTR;}

    public boolean isLogos() {
        return currentState == GameState.LOGOS;
        //currentState = GameState.INSTR2;
        }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isDeathMenu() {
        return currentState == GameState.DEATHMENU;
    }

    public boolean isSurvival() {
        return currentState == GameState.SURVIVAL;
    }

    public boolean isInstr() {
        return currentState == GameState.INSTR;
    }

    public boolean isCredits() {
        return currentState == GameState.CREDITS;
    }

    public void setRendererAndUIHandler(GameRenderer renderer, UiHandler uiHandler) {
        this.renderer = renderer;
        this.uiHandler=uiHandler;
    }
}