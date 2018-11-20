package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.Glider;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.ui.SimpleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Kredatus on 8/27/2017.
 */

public class InputHandler implements InputProcessor {
    private GameWorld myWorld;
    private Glider myGlider;

    private List<SimpleButton> menuButtons, deathButtons;
    private SimpleButton playButton, storyButton, creditsButton, exitButton, retryButton, menuButton, readyButton, instrButton, nextButton;

    public static Vector2 point;
    private static float scaleFactorX;
    private static float scaleFactorY;
    private int camWidth;
    // Ask for a reference to the glider when InputHandler is created.
    public InputHandler(GameWorld myWorld, float scaleFactorX, float scaleFactorY, int camWidth, int camHeight) {
        // myBird now represents the gameWorld's bird.
        this.camWidth=camWidth;
        this.myWorld = myWorld;
        myGlider = myWorld.getGlider();
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;


        menuButtons = new ArrayList<SimpleButton>();
        playButton = new SimpleButton(camWidth/2- AssetHandler.play.getRegionWidth()/2, camHeight- AssetHandler.play.getRegionHeight()*1.1f,
                AssetHandler.play.getRegionWidth(), AssetHandler.play.getRegionHeight(), AssetHandler.play, AssetHandler.playdown);
        menuButtons.add(playButton);
        storyButton = new SimpleButton(camWidth/4- AssetHandler.story.getRegionWidth()/5- AssetHandler.play.getRegionWidth()/4,  7.5f*camHeight/9-3f* AssetHandler.instr.getRegionHeight()/5,
                AssetHandler.story.getRegionWidth()/2.5f, AssetHandler.story.getRegionHeight()/2.5f, AssetHandler.story, AssetHandler.storydown);
        menuButtons.add(storyButton);
        instrButton = new SimpleButton(camWidth/4- AssetHandler.instr.getRegionWidth()/5- AssetHandler.play.getRegionWidth()/4, 8.5f*camHeight/9-1.5f*(AssetHandler.instr.getRegionHeight()/5),
                AssetHandler.instr.getRegionWidth()/2.5f, AssetHandler.instr.getRegionHeight()/2.5f, AssetHandler.instr, AssetHandler.instrdown);
        menuButtons.add(instrButton);
        creditsButton = new SimpleButton(AssetHandler.play.getRegionWidth()/4+3*camWidth/4- AssetHandler.credits.getRegionWidth()/5+5, 8.5f*camHeight/9-1.5f*(AssetHandler.instr.getRegionHeight()/5),
                AssetHandler.credits.getRegionWidth()/2.5f, AssetHandler.credits.getRegionHeight()/2.5f, AssetHandler.credits, AssetHandler.creditsdown);
        menuButtons.add(creditsButton);
        exitButton = new SimpleButton(AssetHandler.play.getRegionWidth()/4+3*camWidth/4- AssetHandler.exit.getRegionWidth()/5, 7.5f*camHeight/9-3*(AssetHandler.instr.getRegionHeight()/5),
                AssetHandler.exit.getRegionWidth()/2.5f, AssetHandler.exit.getRegionHeight()/2.5f, AssetHandler.exit, AssetHandler.exitdown);
        menuButtons.add(exitButton);


        deathButtons = new ArrayList<SimpleButton>();
        retryButton = new SimpleButton(camWidth- AssetHandler.retry.getRegionWidth()/2.5f-10,camHeight- AssetHandler.retry.getRegionHeight()/2.5f-10,
                AssetHandler.retry.getRegionWidth()/2.5f, AssetHandler.retry.getRegionHeight()/2.5f, AssetHandler.retry, AssetHandler.retrydown);
        deathButtons.add(retryButton);
        menuButton = new SimpleButton(10,camHeight- AssetHandler.menu.getRegionHeight()/2.5f,
                AssetHandler.menu.getRegionWidth()/2.5f, AssetHandler.menu.getRegionHeight()/2.5f, AssetHandler.menu, AssetHandler.menudown);
        deathButtons.add(menuButton);


        readyButton = new SimpleButton(9*camWidth/10- AssetHandler.ready.getRegionWidth()/5,camHeight/2- AssetHandler.ready.getRegionHeight()/5,
                AssetHandler.ready.getRegionWidth()/2.5f, AssetHandler.ready.getRegionHeight()/2.5f, AssetHandler.ready, AssetHandler.readydown);
        nextButton = new SimpleButton(camWidth- AssetHandler.next.getRegionWidth()/2.5f-10, camHeight- AssetHandler.menu.getRegionHeight()/2.5f,
                AssetHandler.next.getRegionWidth()/2.5f, AssetHandler.next.getRegionHeight()/2.5f, AssetHandler.next, AssetHandler.nextdown);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (myWorld.isRunning()) {
            myGlider.onClick();
            //point.set(screenX, screenY);
        } else if (myWorld.isMenu()) {
            for (SimpleButton buttons : menuButtons) {
                buttons.isTouchDown(screenX, screenY);}

        } else if (myWorld.isDeathMenu()) {
            for (SimpleButton buttons : deathButtons) {
                buttons.isTouchDown(screenX, screenY);}

        }else if (myWorld.isReady()) {
            readyButton.isTouchDown(screenX, screenY);
        } else if (myWorld.isStory()) {
            nextButton.isTouchDown(screenX, screenY);
            menuButton.isTouchDown(screenX, screenY);
        } else if (myWorld.isCredits()) {
            menuButton.isTouchDown(screenX, screenY);
        } else if (myWorld.isInstr()) {
            nextButton.isTouchDown(screenX, screenY);
            menuButton.isTouchDown(screenX, screenY);
        } else if (myWorld.isInstr2()) {
            menuButton.isTouchDown(screenX, screenY);
    }
        return true; // Return true to say we handled the touch.
    }

    @Override
    public boolean keyDown(int keycode) {
        // Can now use Space Bar to play the game
        if (keycode == Input.Keys.SPACE) {
            if (myWorld.isMenu()) {
                myWorld.ready();
            } else if (myWorld.isReady()) {
                myWorld.start();
            }
            myGlider.onClick();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (myWorld.isMenu()) {
            if (playButton.isTouchUp(screenX, screenY)) {
                myWorld.ready();
            } else if (storyButton.isTouchUp(screenX, screenY)){
                myWorld.story();
            } else if (instrButton.isTouchUp(screenX, screenY)){
                myWorld.instr();
            } else if (creditsButton.isTouchUp(screenX, screenY)){
                myWorld.credits();
            } else if (exitButton.isTouchUp(screenX, screenY)){
                myWorld.exit();
            }
        } else if (myWorld.isDeathMenu()) {
            if (menuButton.isTouchUp(screenX, screenY)){
                myWorld.backToMenu();
                AssetHandler.deathmenumusic.stop();
                AssetHandler.playnext(AssetHandler.menumusiclist);
            } else if (retryButton.isTouchUp(screenX, screenY)){
                myWorld.restart();
            }
        } else if (myWorld.isReady()){
            myWorld.start();

        } else if ( myWorld.isStory() ) {
            if (AssetHandler.getHighScore() != 0) {
                if (menuButton.isTouchUp(screenX, screenY)) {
                    myWorld.backToMenu();
                }
            } else {
                if (nextButton.isTouchUp(screenX, screenY)) {
                    myWorld.instr();
                }
            }
        } else if (myWorld.isCredits()) {
            if (menuButton.isTouchUp(screenX, screenY)) {
                myWorld.backToMenu();
            }

        } else if (myWorld.isInstr()) {
            if (menuButton.isTouchUp(screenX, screenY)) {
                        myWorld.backToMenu();}
            if (nextButton.isTouchUp(screenX, screenY)) {
                        myWorld.instr2();
            }
        } else if (myWorld.isInstr2()) {
            if (menuButton.isTouchUp(screenX, screenY)) {
                myWorld.backToMenu();}
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public static int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    public static int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

    public List<SimpleButton> getMenuButtons() {
        return menuButtons;
    }

    public List<SimpleButton> getDeathButtons() {
        return deathButtons;
    }
    public SimpleButton getReadyButton() {return readyButton;}
    public SimpleButton getMenuButton() {return menuButton;}
    public SimpleButton getNextButton() {return nextButton;}
}


