package com.kredatus.flockblockers.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;


public class SlideMenu extends Table {

    // only visual window and using scissor to avoid GPU to draw out of left-edge screen.
    private float areaWidth;
    private float areaHeight;
    private String originEdge;
    private float camWidth, camHeight;
    private final Rectangle areaBounds = new Rectangle();
    private final Rectangle scissorBounds = new Rectangle();

    // it's revealed with (widthStart = 60F;) when the user swipes a finger from the left edge of the screen with start touch.
    private float widthStart = 15f;
    // when the user swipes a finger from the right edge of the screen, it goes into off-screen after (widthBack = 20F;).
    private float widthBack = 0f;
    private float heightStart = 15f;
    private float heightBack = 0f;
    // speed of dragging
    private float speed = 15f;

    // some attributes to make real draggingX
    private Vector2 clamp = new Vector2();
    private Vector2 posTap = new Vector2();
    private Vector2 end = new Vector2();
    private Vector2 first = new Vector2();
    private Vector2 last = new Vector2();

    private boolean show = false;
    public boolean isTouched = false;
    private boolean isStart = false;
    private boolean isBack = false;
    private boolean auto = false;
    private boolean enableDrag = true;

    public SlideMenu(float width, float height, String originEdge, float camWidth, float camHeight) {
        this.areaWidth = width;
        this.areaHeight = height;
        this.originEdge = originEdge;
        this.setSize(width, height);
        this.camWidth=camWidth;
        this.camHeight=camHeight;


    }


    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void showManually(boolean show, float speed) {
        if (!auto) {
            this.auto = true;
            this.show = show;
            this.speed = speed;
        }
        //System.out.println(show);
    }

    public void showManually(boolean show) {
        this.showManually(show, speed);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        noDrag();

        if (originEdge.equals("down")) {
            if (menuButton.getY() > areaHeight / 2 && menuButton.getRotation() != 270)
                menuButton.setRotation(270);
            else if (menuButton.getRotation() != 90 && menuButton.getY() < areaHeight / 2)
                menuButton.setRotation(90);

            updatePositionY();
            moveMenuButtonY();

            if (auto && (isCompletelyClosedY()||isCompletelyOpenedY())) auto = false;

                //if not autoSliding and touched and (input is above 950 when closed or above menuHeight when opened) and touchpads not touched
            if (!auto && isTouched() && ( (isCompletelyClosedY()&&inputY() > stgToScrCoordsY(0, this.getHeight()/2.5f).y) || (!isCompletelyClosedY()&&inputY() > stgToScrCoordsY(0, this.getHeight()).y)) && !UiHandler.movPad.isTouched() && !UiHandler.aimPad.isTouched() )  {
                if (!isTouched) isTouched=true;
                //if closed, in zone and swiping down
                if (isCompletelyClosedY()&&Gdx.input.getDeltaY()<-3) showManually(true);// open = false, close = true;

                else if (isCompletelyOpenedY()&&Gdx.input.getDeltaY()>3)showManually(false);// open = true, close = false;
            } else if (isTouched) isTouched=false;
            //can compare to how it was when edge of menu followed finger in ui v10 git commit
            getStage().calculateScissors(areaBounds.set(camWidth/2-areaWidth/2, 0, areaWidth, areaHeight), scissorBounds);

        } else if (originEdge.equals("left")) {
            if (menuButton.getX() > areaWidth / 2 && menuButton.getRotation() != 180)
                menuButton.setRotation(180);
            else if (menuButton.getRotation() != 0 && menuButton.getX() < areaWidth / 2)
                menuButton.setRotation(0);

            updatePositionX();
            moveMenuButtonX();

            if (auto && (isCompletelyClosedX() || isCompletelyOpenedX())) auto = false;

            //if not autoSliding and touched and (input is above 950 when closed or above menuHeight when opened) and touchpads not touched
            if (!auto && isTouched() && ( (isCompletelyClosedX()&&inputX() < stgToScrCoordsX(this.getWidth()/1.5f, 0).x) || (!isCompletelyClosedX()&&inputX() < stgToScrCoordsX(this.getWidth(), 0).x) ) && !UiHandler.movPad.isTouched() && !UiHandler.aimPad.isTouched()) {
                if (!isTouched) isTouched=true;
            //if closed, in zone and swiping left
                if (isCompletelyClosedX() && Gdx.input.getDeltaX() > 3)
                    showManually(true);// open = false, close = true;

                else if (isCompletelyOpenedX() && Gdx.input.getDeltaX() < -3)
                    showManually(false);// open = true, close = false;
            } else if (isTouched) isTouched=false;
            //can compare to how it was when edge of menu followed finger in ui v10 git commit
            getStage().calculateScissors(areaBounds.set(0, 0, areaWidth, areaHeight), scissorBounds);
        }
        batch.flush();
        if (ScissorStack.pushScissors(scissorBounds)) {
            super.draw(batch, alpha);
            batch.flush();
            ScissorStack.popScissors();
        }
    }

    private void updatePositionX() {
        clamp.set(MathUtils.clamp(end.x, 0, this.getWidth()), 0);
        this.setPosition(clamp.x-getWidth(), 0);
        //System.out.println("Left slide menu: x: "+clamp.x+", y: "+clamp.y);
    }
    private void updatePositionY() {
        clamp.set(0, MathUtils.clamp(end.y, 0, this.getHeight()) );
        //System.out.println(clamp.y);
        this.setPosition(camWidth/2f-this.areaWidth/2f, clamp.y-getHeight());
        //System.out.println("Down slide menu: x: "+clamp.x+", y: "+clamp.y);
    }

    private void noDrag() {
        isStart = false;
        isBack = false;
        isTouched = false;
        // set end of X to updated X from clamp
        end.set(clamp);

        if (auto) {
            System.out.println("updating");
            if (show)
                end.add(0, speed); // player want to OPEN drawer
            else
                end.sub(0, speed); // player want to CLOSE drawer
        }
    }
    
    public boolean isCompletelyClosedX() {
        return clamp.x == 0;
    }
    public boolean isCompletelyClosedY() {
        return clamp.y == 0;
    }
    
    public boolean isCompletelyOpenedX() {
        return clamp.x == this.getWidth();
    }
    public boolean isCompletelyOpenedY() {
        return clamp.y == this.getHeight();
    }

    private Vector2 stgToScrCoordsX(float x, float y) {
        return getStage().stageToScreenCoordinates(posTap.set(x, y));
    }
    private Vector2 stgToScrCoordsY(float x, float y) {
        //System.out.println("stage to screen coords: "+y );
        return getStage().stageToScreenCoordinates(posTap.set(x, y )  );
    }

    private float inputX() {
        return Gdx.input.getX();
    }

    private float inputY() {
        return Gdx.input.getY();
    }
    
    private boolean isTouched() {
        return Gdx.input.isTouched();
    }

    private Actor menuButton = new Actor();
    private boolean ismoveMenuButton=false;

    private void moveMenuButtonX() {
        if (ismoveMenuButton)
            menuButton.setPosition(clamp.x-7,menuButton.getY());
    }
    private void moveMenuButtonY() {
        if (ismoveMenuButton) {
            menuButton.setPosition(menuButton.getX(), clamp.y-60);
        }
    }

    public void setMoveMenuButton(Actor actor) {
        this.menuButton = actor;
        this.ismoveMenuButton = true;
        //this.moveMenuButtonX = rotation;
    }
    }