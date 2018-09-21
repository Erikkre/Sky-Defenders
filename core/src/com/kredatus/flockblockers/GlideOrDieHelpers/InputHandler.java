package com.kredatus.flockblockers.GlideOrDieHelpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.kredatus.flockblockers.GameObjects.Glider;
import com.kredatus.flockblockers.GameWorld.ExGameWorld;
import com.kredatus.flockblockers.ui.SimpleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Kredatus on 8/27/2017.
 */

public class InputHandler implements InputProcessor {
    private ExGameWorld myWorld;
    private Glider myGlider;

    private List<SimpleButton> menuButtons, deathButtons;
    private SimpleButton playButton, storyButton, creditsButton, exitButton, retryButton, menuButton, readyButton, instrButton, nextButton;

    private float scaleFactorX;
    private float scaleFactorY;
    private int camwidth;
    // Ask for a reference to the glider when InputHandler is created.
    public InputHandler(ExGameWorld myWorld, float scaleFactorX, float scaleFactorY, int camwidth, int camheight) {
        // myBird now represents the gameWorld's bird.
        this.camwidth=camwidth;
        this.myWorld = myWorld;
        myGlider = myWorld.getGlider();
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;


        menuButtons = new ArrayList<SimpleButton>();
        playButton = new SimpleButton(camwidth/2-AssetLoader.play.getRegionWidth()/2, camheight-AssetLoader.play.getRegionHeight()*1.1f,
                AssetLoader.play.getRegionWidth(), AssetLoader.play.getRegionHeight(), AssetLoader.play, AssetLoader.playdown);
        menuButtons.add(playButton);
        storyButton = new SimpleButton(camwidth/4-AssetLoader.story.getRegionWidth()/5-AssetLoader.play.getRegionWidth()/4,  7.5f*camheight/9-3f*AssetLoader.instr.getRegionHeight()/5,
                AssetLoader.story.getRegionWidth()/2.5f, AssetLoader.story.getRegionHeight()/2.5f, AssetLoader.story, AssetLoader.storydown);
        menuButtons.add(storyButton);
        instrButton = new SimpleButton(camwidth/4-AssetLoader.instr.getRegionWidth()/5-AssetLoader.play.getRegionWidth()/4, 8.5f*camheight/9-1.5f*(AssetLoader.instr.getRegionHeight()/5),
                AssetLoader.instr.getRegionWidth()/2.5f, AssetLoader.instr.getRegionHeight()/2.5f, AssetLoader.instr, AssetLoader.instrdown);
        menuButtons.add(instrButton);
        creditsButton = new SimpleButton(AssetLoader.play.getRegionWidth()/4+3*camwidth/4-AssetLoader.credits.getRegionWidth()/5+5, 8.5f*camheight/9-1.5f*(AssetLoader.instr.getRegionHeight()/5),
                AssetLoader.credits.getRegionWidth()/2.5f, AssetLoader.credits.getRegionHeight()/2.5f, AssetLoader.credits, AssetLoader.creditsdown);
        menuButtons.add(creditsButton);
        exitButton = new SimpleButton(AssetLoader.play.getRegionWidth()/4+3*camwidth/4-AssetLoader.exit.getRegionWidth()/5, 7.5f*camheight/9-3*(AssetLoader.instr.getRegionHeight()/5),
                AssetLoader.exit.getRegionWidth()/2.5f, AssetLoader.exit.getRegionHeight()/2.5f, AssetLoader.exit, AssetLoader.exitdown);
        menuButtons.add(exitButton);


        deathButtons = new ArrayList<SimpleButton>();
        retryButton = new SimpleButton(camwidth-AssetLoader.retry.getRegionWidth()/2.5f-10,camheight-AssetLoader.retry.getRegionHeight()/2.5f-10,
                AssetLoader.retry.getRegionWidth()/2.5f, AssetLoader.retry.getRegionHeight()/2.5f, AssetLoader.retry, AssetLoader.retrydown);
        deathButtons.add(retryButton);
        menuButton = new SimpleButton(10,camheight-AssetLoader.menu.getRegionHeight()/2.5f,
                AssetLoader.menu.getRegionWidth()/2.5f, AssetLoader.menu.getRegionHeight()/2.5f, AssetLoader.menu, AssetLoader.menudown);
        deathButtons.add(menuButton);


        readyButton = new SimpleButton(9*camwidth/10-AssetLoader.ready.getRegionWidth()/5,camheight/2-AssetLoader.ready.getRegionHeight()/5,
                AssetLoader.ready.getRegionWidth()/2.5f, AssetLoader.ready.getRegionHeight()/2.5f, AssetLoader.ready, AssetLoader.readydown);
        nextButton = new SimpleButton(camwidth-AssetLoader.next.getRegionWidth()/2.5f-10, camheight-AssetLoader.menu.getRegionHeight()/2.5f,
                AssetLoader.next.getRegionWidth()/2.5f, AssetLoader.next.getRegionHeight()/2.5f, AssetLoader.next, AssetLoader.nextdown);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (myWorld.isRunning())
            myGlider.onClick();

        else if (myWorld.isMenu()) {
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
                AssetLoader.deathmenumusic.stop();
                AssetLoader.playnext(AssetLoader.menumusiclist);
            } else if (retryButton.isTouchUp(screenX, screenY)){
                myWorld.restart();
            }
        } else if (myWorld.isReady()){
            myWorld.start();

        } else if ( myWorld.isStory() ) {
            if (AssetLoader.getHighScore() != 0) {
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

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
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


