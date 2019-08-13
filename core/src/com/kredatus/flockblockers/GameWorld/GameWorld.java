// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameObjects.Turret;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;
import com.kredatus.flockblockers.Handlers.TinyBirdHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.NonGameHandlerScreens.Loader;
import com.kredatus.flockblockers.TweenAccessors.Value;

import aurelienribon.tweenengine.Tween;

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
    public LightHandler lightHandler;
    //private boolean isAlive = true;
    private Rectangle ground;
    public  Airship airship;
    public double boost = 0;  //boostamount

    private  GameRenderer renderer;
    public enum GameState {
        MENU, SURVIVAL, OPTIONS, BUYMENU, CREDITS, DEATHMENU, INSTR, LOGOS
    }
    public  boolean isFirstTime;
    private  GameState currentState;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static int exp, score, gold, diamond, round;
    public static boolean soundMuted=false;

    public Value alpha =new Value(0),alphaBg=new Value(0);
    public Tween logoTween, logoBgTween, timerTween;
    public boolean startGame;
    private TextureRegion logo;
    private Sprite logoBg;

    public static int camWidth,camHeight,waveStartHealth,waveStartArmor,waveStartFuel,waveStartAmmo,waveStartGold,waveStartDiamond,waveStartExp;
    Preferences prefs = Gdx.app.getPreferences("skyDefenders");
    public GameWorld(int camWidth, int camHeight) {
        this.camWidth=camWidth;this.camHeight=camHeight;

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

        Loader.playnext(Loader.musiclist);

        currentState=GameState.SURVIVAL;
        //startLogos(camWidth,camHeight);
        exp=prefs.getInteger("exp",0);
        score=prefs.getInteger("score",0);
        gold=prefs.getInteger("gold",100);UiHandler.totalGoldNum=gold;
        diamond =prefs.getInteger("diamond",0);UiHandler.totalDiamondNum=diamond;
        round=prefs.getInteger("round",1);
    }

    public void initialize(BgHandler bgHandler, BirdHandler birdHandler, TargetHandler targetHandler, TinyBirdHandler tinyBirdHandler, UiHandler uiHandler, LightHandler lightHandler,GameRenderer renderer, Airship airship) {
        this.renderer = renderer;
        this.airship=airship;
        this.bgHandler = bgHandler;
        this.birdHandler = birdHandler;
        this.targetHandler = targetHandler;
        this.tinyBirdHandler=tinyBirdHandler;
        this.uiHandler=uiHandler;
        this.lightHandler=lightHandler;
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
            case LOGOS://not used
                updateLogos(delta, runTime);
                if (startGame) updateSurvival(delta,runTime);
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
        targetHandler.update(delta, runTime);
        airship.update(delta);

        tinyBirdHandler.update(delta);
        lightHandler.update();
        lightHandler.foreRayHandler.update();  //used for airship and gun lights too
        lightHandler.backRayHandler.update();
    }
    private void updateBuyMenu(float delta, float runTime) {
        bgHandler.update(delta);
        uiHandler.update(delta);

        for (Projectile i : targetHandler.projectileList){ i.update();}
        for (BirdAbstractClass i : birdHandler.deadBirdQueue){
            i.update(delta, runTime);
            if (i.isOffCam()&&i.dropsList==null) {
                birdHandler.deadBirdQueue.remove(i);
            }
        }
        airship.update(delta);
        tinyBirdHandler.update(delta);
        //lightHandler.foreRayHandler.update();  //used for airship and gun lights too
    }
    private void updateLogos(float delta, float runTime) {
        //timerTween.update(delta);
        logoTween.update(delta);
        logoBgTween.update(delta);
    }
    private void updateMenu(float delta, float runTime) {
        bgHandler.update(delta);
        birdHandler.update();
        //turretHandler.update();
        uiHandler.update(delta);
        targetHandler.update(delta, runTime);
        airship.update(delta);

        tinyBirdHandler.update(delta);
        lightHandler.update();
        lightHandler.foreRayHandler.update();  //used for airship and gun lights too
        lightHandler.backRayHandler.update();
    }
    private void updateReady(float runTime) {
        //glider.updateReady(runTime);
    }
    private void updateRunning(float delta, float runTime) {
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
            renderer.makeTransition(255, 255, 255, 1);
        }*/
    }

    public void addBoost(double increment) {
        boost += increment;
    }
    public BgHandler getbgHandler() {
        return bgHandler;
    }

    public void deathAndRestart(){
        //AssetHandler.frontFlaps.setFrameDuration(0.2f);
        //currentState = GameState.READY;


        int waveNumberFromLastWave=(BgHandler.bgNumber-2)/9;

        System.out.println("bgNumber: "+BgHandler.bgNumber);
        bgHandler=null;
        bgHandler=new BgHandler(this,camWidth,camHeight,waveNumberFromLastWave,birdHandler);
        bgHandler.startToSurvival(tinyBirdHandler,lightHandler);
        bgHandler.setRendererAndCam(renderer);

        //System.out.println("airshipAmmo("+Airship.ammo+") set to: "+waveStartAmmo);
        Airship.health=waveStartHealth;UiHandler.airshipHealthLabel.setText(Airship.health);UiHandler.airshipHealthBar.setValue(Airship.health);
        Airship.armor=waveStartArmor;UiHandler.airshipArmorLabel.setText(Airship.armor);UiHandler.airshipArmorBar.setValue(Airship.armor);
        Airship.fuel=waveStartFuel;UiHandler.fuelLabel.setText((int)Airship.fuel);
        Airship.ammo=waveStartAmmo;UiHandler.ammoLabel.setText(Airship.ammo);

        gold=waveStartGold+Integer.parseInt(uiHandler.ammoPrice)+Integer.parseInt(uiHandler.fuelPrice);UiHandler.goldLabel.setText(gold);//have minimum to fly and shoot
        diamond=waveStartDiamond;UiHandler.diamondLabel.setText(diamond);
        exp=waveStartExp;UiHandler.expLabel.setText(exp);UiHandler.expBar.setValue(exp);

        UiHandler.totalHealthNum=waveStartHealth;UiHandler.totalArmorNum=waveStartArmor;UiHandler.totalFuelNum=waveStartFuel;
        UiHandler.totalAmmoNum=waveStartAmmo;UiHandler.totalGoldNum=gold;UiHandler.totalDiamondNum=diamond;

        if (birdHandler.taskRunning){
            //System.out.println("Timer cancelled");
            birdHandler.task.cancel();
            birdHandler.taskRunning=false;
            birdHandler.activeBirdQueue.clear();
            birdHandler.deadBirdQueue.clear();
            birdHandler.birdQueue.clear();
            birdHandler.update();
        }
        for (Turret i: airship.turretList){
            i.stopFiring();
        }
        uiHandler.boughtItemsList.clear();
        Airship.pos=new Vector2(airship.startX,airship.startY);
        renderer.makeTransition(0, 0, 0, 3f);
    }

    public void survivalToBuyMenu() {
        uiHandler.survivalToBuyMenu();
        GameHandler.timeOfPause = System.currentTimeMillis();

        currentState = GameState.BUYMENU;
        airship.survivalToBuyMenu();
        bgHandler.survivalToBuyMenu();
        for (Turret i : airship.turretList) {
            if (i.firing) {
                i.stopTheFiringUpdateMethod =true;//dont need to stop firing because we are pausing entire update and refiring immediately after
                i.stopFiring();
            }
        }
        birdHandler.pause();
        //renderer.makeTransition(0, 0, 0, 0.7f);
    }
    public void buyMenuToSurvival() {
        uiHandler.buyMenuToSurvival();
        GameHandler.timeOfResume = System.currentTimeMillis();

        currentState = GameState.SURVIVAL;
        airship.toSurvival("buyMenuToSurvival");
        bgHandler.buyMenuToSurvival();

        for (Turret i : airship.turretList) {
            if (i.stopTheFiringUpdateMethod) {
                i.stopTheFiringUpdateMethod=false;
            }
        }
        birdHandler.resume();
    }
    public void startToSurvival(TinyBirdHandler tinyBirdHandler, LightHandler lightHandler, Airship airship, BgHandler bgHandler) {//the one that startSurvival starts with
        currentState = GameState.SURVIVAL;
        airship.toSurvival("startToSurvival");
        bgHandler.startToSurvival(tinyBirdHandler,lightHandler);
    }
    public void survivalToMenu() {
        //glider.onRestart();
        //bgHandler.onRestart();
        //renderer.setCamPositionOriginal();
        currentState = GameState.MENU;
        renderer.makeTransition(0, 0, 0, 0.7f);
        GameHandler.timeOfPause = System.currentTimeMillis();
    }
    public void exit() {
        Gdx.app.exit();
    }

    public void story() {
        renderer.makeTransition(0, 0, 0, 1f);
        //currentState = GameState.STORY;
        }

    public void credits() {
        renderer.makeTransition(0, 0, 0, 1f);
        currentState = GameState.CREDITS;}

    public void instr() {
        renderer.makeTransition(0, 0, 0, 1f);
        currentState = GameState.INSTR;}

    public boolean isLogos() {
        return currentState == GameState.LOGOS;
        //currentState = GameState.INSTR2;
        }

    public  boolean isMenu() {
        return currentState == GameState.MENU;
    }
    public  boolean isBuyMenu() {
        return currentState == GameState.BUYMENU;
    }
    public boolean isDeathMenu() {
        return currentState == GameState.DEATHMENU;
    }
    public  boolean isSurvival() {
        return currentState == GameState.SURVIVAL;
    }
    public boolean isInstr() {
        return currentState == GameState.INSTR;
    }
    public boolean isCredits() {
        return currentState == GameState.CREDITS;
    }
}

    /*public void startLogos(int camWidth,int camHeight) {
        LightHandler.rayHandlerAmbLightLvl=0.75f;
        currentState=GameState.LOGOS;
        logo=((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("companyLogo");
        logoBg=new Sprite(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("slideMenuBackground"));
        logoBg.setSize(camWidth,camHeight);
        logoBg.setColor(new Color(0,0,0,1f));

        //Sprite temp=new Sprite(AssetHandler.slidemenuBg);
        //temp.setColor(new Color(0,0,0,0.5f));
        //final Image image_backgroundX = new Image(new SpriteDrawable(temp));
        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if (!startGame) {
                    startGame = true;
                    System.out.println("start game");
                } else {
                    currentState=GameState.SURVIVAL;
                    System.out.println("switch to startToSurvival");
                }
            }
        };

        float dur=0.7f;
        logoTween=(Tween.to(alpha, 0, dur).target(1)
                .ease(TweenEquations.easeInSine).repeatYoyo(1, 1.4f));
                //.start();
        logoBgTween=(Tween.to(alphaBg, 0, dur).target(1)
                .ease(TweenEquations.easeOutQuint).repeatYoyo(1, 1.5f)).setCallback(cb).setCallbackTriggers(TweenCallback.END);
                //.start();
        //timerTween=Tween.to(0, 0, dur).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE);
    }*/

    /*public void drawLogos(SpriteBatch batch, float camWidth, float camHeight) {
        if (!logoTween.isStarted()) logoTween.start();
        if (!logoBgTween.isStarted()) logoBgTween.start();


        logoBg.setAlpha(alphaBg.get());
        System.out.println("A: "+alpha.get()+", eq: "+ alphaBg.get());
        logoBg.draw(batch);

        batch.setColor(1, 1, 1, alpha.get());
        batch.draw(logo, camWidth / 2f - ( logo.getRegionWidth() * camHeight/(float)logo.getRegionHeight())/2, 0, logo.getRegionWidth() * camHeight/(float)logo.getRegionHeight(), camHeight);
        batch.setColor(Color.WHITE);
    }*/