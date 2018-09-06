package com.kredatus.flockblockers.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.kredatus.flockblockers.GameObjects.Glider;
import com.kredatus.flockblockers.GameObjects.ScrollHandler;
import com.kredatus.flockblockers.GlideOrDieHelpers.AssetLoader;

/**
 * Created by Mr. Kredatus on 8/5/2017.
 */
public class GameWorld {
    private Glider glider;
    private ScrollHandler scroller;
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

    private GameState currentState;
    public GameWorld(int midPointY, int midPointX, float camwidth) {
        if (AssetLoader.getHighScore()==0){
            currentState= GameState.STORY;
        } else {
        currentState = GameState.MENU;}

        this.camwidth=camwidth;
        this.midPointY=midPointY;
        glider = new Glider(midPointX, midPointY, AssetLoader.gliderMid.getRegionWidth()/AssetLoader.getgliderScaling(), AssetLoader.gliderMid.getRegionHeight()/AssetLoader.getgliderScaling(), this);
        scroller = new ScrollHandler(this);
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
                updateRunning(delta);
                break;
            default:
                break;
        }
    }

    private void updateReady(float runTime) {
        glider.updateReady(runTime);
        //scroller.updateReady(delta);
    }

    private void updateRunning(float delta) {
        updatedboostnumber=(int)((-orgboostnumber*renderer.scorenumber/105f)+orgboostnumber);//keep rendering boosts until 130
        //System.out.println((int)((-renderer.scorenumber/5f)+orgboostnumber));
        if (delta > .15f) {
            delta = .15f;}
        scroller.collides(glider, updatedboostnumber);
        glider.update(delta);
        scroller.update(updatedboostnumber);

        if (Math.abs(glider.getPosition().y) > scroller.bgh) {
            renderer.prepareSunshine();
            currentState = GameState.DEATHMENU;
            if (renderer.scorenumber > AssetLoader.getHighScore()) {
                AssetLoader.setHighScore(renderer.scorenumber);
            }

            scroller.onRestart();
            AssetLoader.frontViewFlaps.setFrameDuration(0.2f);
            renderer.setCamPositionOriginal();
            renderer.prepareTransition(255, 255, 255, 1);
        }
    }

    public void addBoost(double increment) {
        boost += increment;
    }

    public Glider getGlider() {
        return glider;
    }

    public ScrollHandler getScroller() {
        return scroller;
    }

    public void start() {
        currentState = GameState.RUNNING;
        boost=5;
        AssetLoader.flaps.setFrameDuration(0.12f);
    }

    public void restart() {
        boost = 0;
        glider.onRestart();
        renderer.setCamPositionOriginal();
        renderer.scorenumber=0;
        AssetLoader.deathmenumusic.stop();
        AssetLoader.playnext(AssetLoader.musiclist);
        AssetLoader.flaps.setFrameDuration(0.2f);
        currentState = GameState.READY;
        renderer.prepareTransition(0, 0, 0, 1f);}

    public void backToMenu() {
        boost = 0;
        glider.onRestart();
        scroller.onRestart();
        renderer.setCamPositionOriginal();
        currentState = GameState.MENU;
        renderer.prepareTransition(0, 0, 0, 1f);}

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public void ready() {
        renderer.sunshineManager.killAll();
        renderer.sunshineManager2.killAll();
        renderer.manager.killAll();
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