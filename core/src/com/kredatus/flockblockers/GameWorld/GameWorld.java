package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.kredatus.flockblockers.GameObjects.Glider;
import com.kredatus.flockblockers.GlideOrDieHelpers.BgHandler;
import com.kredatus.flockblockers.GlideOrDieHelpers.AssetLoader;
import com.kredatus.flockblockers.Screens.SplashScreen;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */
public class GameWorld {
    private Glider glider;
    public BgHandler bgHandler;
    //private boolean isAlive = true;
    private Rectangle ground;
    public double boost = 0;  //boostamount
    public int updatedboostnumber, orgboostnumber=AssetLoader.getBoostnumber();
    public float camwidth;
    public float midPointY;
    private GameRenderer renderer;
    public enum GameState {
        MENU, READY, RUNNING, STORY, CREDITS, DEATHMENU, INSTR, INSTR2
    }
    public boolean isFirstTime;
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

    public GameWorld(int midPointY, int midPointX, float camwidth, float camheight) {
        if (AssetLoader.getHighScore()==0){
            isFirstTime=true;
            currentState= GameState.STORY;
        } else {
            currentState = GameState.MENU;}

        this.camwidth=camwidth;
        this.midPointY=midPointY;
        glider = new Glider(0, 0, AssetLoader.frontFlaps.getKeyFrame(0).getRegionWidth(), AssetLoader.frontFlaps.getKeyFrame(0).getRegionHeight(), this);
        bgHandler = new BgHandler( camwidth, camheight);
        AssetLoader.playnext(AssetLoader.menumusiclist);
        updatedboostnumber=orgboostnumber;
    }

    public void update(float delta, float runTime) {
        switch (currentState) {
            case MENU:
            case READY:
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
        bgHandler.update(updatedboostnumber, runTime, delta);
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
        bgHandler.update(updatedboostnumber, runTime, delta);

        if (Math.abs(glider.getPosition().y) > bgHandler.bgh) {
           // renderer.prepareSunshine();
            currentState = GameState.DEATHMENU;
            if (renderer.scorenumber > AssetLoader.getHighScore()) {
                AssetLoader.setHighScore(renderer.scorenumber);
            }

            //bgHandler.onRestart();
           // AssetLoader.frontViewFlaps.setFrameDuration(0.2f);
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
        AssetLoader.frontFlaps.setFrameDuration(0.12f);
    }

    public void restart() {
        boost = 0;
        glider.onRestart();
        //renderer.setCamPositionOriginal();
        renderer.scorenumber=0;
        AssetLoader.deathmenumusic.stop();
        AssetLoader.playnext(AssetLoader.musiclist);
        AssetLoader.frontFlaps.setFrameDuration(0.2f);
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
        AssetLoader.stopmusic(AssetLoader.menumusiclist);
        AssetLoader.playnext(AssetLoader.musiclist);
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

    public int getUpdatedboostnumber() {
        return updatedboostnumber;
    }

}