package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.kredatus.flockblockers.GameObjects.Glider;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.ImpactHandler;
import com.kredatus.flockblockers.Screens.SplashScreen;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */
public class GameWorld {
    private Glider glider;
    public BgHandler bgHandler;
    public BirdHandler birdHandler;
    public ImpactHandler impactHandler;
    //private boolean isAlive = true;
    private Rectangle ground;
    public double boost = 0;  //boostamount
    public int updatedboostnumber, orgboostnumber= AssetHandler.getBoostnumber();

    private static GameRenderer renderer;
    public enum GameState {
        MENU, READY, RUNNING, STORY, CREDITS, DEATHMENU, INSTR, INSTR2
    }
    public static boolean isFirstTime;
    private GameState currentState;

    public static void addGold(int goldAddition) {
        if (gold+goldAddition>=100){
            gold += goldAddition;
        } else {
            gold=100;
        }
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

    public GameWorld() {
        if (AssetHandler.getHighScore()==0){
            isFirstTime=true;
            currentState= GameState.STORY;
        } else {
            currentState = GameState.MENU;
        }

        //this.camWidth=camWidth;
        //this.midPointY=midPointY;
        //glider = new Glider(0, 0, AssetHandler.frontFlaps.getKeyFrame(0).getRegionWidth(), AssetHandler.frontFlaps.getKeyFrame(0).getRegionHeight(), this);

        AssetHandler.playnext(AssetHandler.menumusiclist);
        updatedboostnumber=orgboostnumber;


    }

    public void update(float delta, float runTime) {
        switch (currentState) {
            case MENU:
                break;
            case READY:
                break;
            case DEATHMENU:
                updateReady(runTime);
                break;
            case RUNNING:
                updateRunning(delta, runTime);
                break;
            case STORY:
                updateStory(delta, runTime);
                break;
            default:
                break;
        }
    }

    private void updateStory(float delta, float runTime) {
        bgHandler.update(delta);
        birdHandler.update();
        impactHandler.update(delta, runTime);
    }

    private void updateReady(float runTime) {
        glider.updateReady(runTime);
    }

    private void updateRunning(float delta, float runTime) {
        updatedboostnumber=(int)((-orgboostnumber*renderer.scorenumber/105f)+orgboostnumber);//keep rendering boosts until 130
        //System.out.println((int)((-renderer.scorenumber/5f)+orgboostnumber));
        if (delta > .15f) {
            delta = .15f;}
        //bgHandler.collides(glider, updatedboostnumber);
        glider.update(delta);
        bgHandler.update(delta);

        if (Math.abs(glider.getPosition().y) > bgHandler.bgh) {
           // renderer.prepareSunshine();
            currentState = GameState.DEATHMENU;
            if (renderer.scorenumber > AssetHandler.getHighScore()) {
                AssetHandler.setHighScore(renderer.scorenumber);
            }

            //bgHandler.onRestart();
           // AssetHandler.frontViewFlaps.setFrameDuration(0.2f);
           //renderer.setCamPositionOriginal();
            renderer.prepareTransition(255, 255, 255, 1);
        }
    }

    public void addBoost(double increment) {
        boost += increment;
    }

    public Glider getGlider() {
        return glider;
    }

    public BgHandler getbgHandler() {
        return bgHandler;
    }

    public void start() {
        currentState = GameState.RUNNING;
        boost=5;
        //AssetHandler.frontFlaps.setFrameDuration(0.12f);
    }

    public void restart() {
        boost = 0;
        glider.onRestart();
        //renderer.setCamPositionOriginal();
        renderer.scorenumber=0;
        AssetHandler.deathmenumusic.stop();
        AssetHandler.playnext(AssetHandler.musiclist);
        //AssetHandler.frontFlaps.setFrameDuration(0.2f);
        currentState = GameState.READY;
        renderer.prepareTransition(0, 0, 0, 1f);}

    public void backToMenu() {
        boost = 0;
        glider.onRestart();
        //bgHandler.onRestart();
        //renderer.setCamPositionOriginal();
        currentState = GameState.MENU;
        renderer.prepareTransition(0, 0, 0, 1f);}

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public void ready() {
        //renderer.sunshineManager.killAll();
        //renderer.sunshineManager2.killAll();
        SplashScreen.getManager().killAll();
        AssetHandler.stopmusic(AssetHandler.menumusiclist);
        AssetHandler.playnext(AssetHandler.musiclist);
        currentState = GameState.READY;
        renderer.prepareTransition(0, 0, 0, 1f);}

    public void exit() {
        Gdx.app.exit();
    }

    public void story() {
        renderer.prepareTransition(0, 0, 0, 1f);
        currentState = GameState.STORY;}

    public void credits() {
        renderer.prepareTransition(0, 0, 0, 1f);
        currentState = GameState.CREDITS;}

    public void instr() {
        renderer.prepareTransition(0, 0, 0, 1f);
        currentState = GameState.INSTR;}

    public void instr2() {
        renderer.prepareTransition(0, 0, 0, 1f);
        currentState = GameState.INSTR2;}

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isDeathMenu() {
        return currentState == GameState.DEATHMENU;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    public boolean isStory() {
        return currentState == GameState.STORY;
    }

    public boolean isInstr() {
        return currentState == GameState.INSTR;
    }

    public boolean isInstr2() {
        return currentState == GameState.INSTR2;
    }

    public boolean isCredits() {
        return currentState == GameState.CREDITS;
    }

    public void setRenderer(GameRenderer renderer) {
        this.renderer = renderer;
    }

    public void setBgHandler(BgHandler bgHandler) {
        this.bgHandler = bgHandler;
    }
    public void setBirdHandler(BirdHandler BirdHandler) {
        this.birdHandler = BirdHandler;
    }
    public void setImpactHandler(ImpactHandler impactHandler) {
        this.impactHandler = impactHandler;
    }
    public static GameRenderer getRenderer() {
        return renderer;
    }
    public int getUpdatedboostnumber() {
        return updatedboostnumber;
    }

}