package com.kredatus.skydefenders.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.kredatus.skydefenders.Handlers.InputHandler;
import com.kredatus.skydefenders.Handlers.UiHandler;


public class SlideMenu extends Table {

    // only visual window and using scissor to avoid GPU to draw out of left-edge screen.
    public float areaWidth;
    public float areaHeight;
    public String originEdge;
    private float camWidth, camHeight;
    public final Rectangle areaBounds = new Rectangle();
    public final Rectangle scissorBounds = new Rectangle();

    // it's revealed with (widthStart = 60F;) when the user swipes a finger from the left edge of the screen with start touch.
    private float widthStart = 15f;
    // when the user swipes a finger from the right edge of the screen, it goes into off-screen after (widthBack = 20F;).
    private float widthBack = 0f;
    private float heightStart = 15f;
    private float heightBack = 0f;
    // speed of dragging
    private float speed = 30f;

    // some attributes to make real draggingX
    private Vector2 clamp = new Vector2();
    private Vector2 posTap = new Vector2();
    private Vector2 end = new Vector2();

    private boolean show = false;
    public boolean isTouched = false;
    private boolean auto = false;
    private boolean enableDrag = true;

    public float offsetFromCenter;
    public SlideMenu(float width, float height, String originEdge, float camWidth, float camHeight, float offsetFromCenter) {
        this.offsetFromCenter=offsetFromCenter;
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
    public void act(float delta){
        super.act(delta);
        noDrag();

        if (originEdge.equals("down")) {
            if (menuButton.getY() > areaHeight / 2 && menuButton.getRotation() != 270)
                menuButton.setRotation(270);
            else if (menuButton.getRotation() != 90 && menuButton.getY() < areaHeight / 2)
                menuButton.setRotation(90);

            updatePositionY();
            moveMenuButtonY();

            //System.out.println((stgXToScrX(menuButton.getX())-menuButton.getHeight()/4)+", "+ptr0X()+", "+(stgXToScrX(menuButton.getX())+5*menuButton.getHeight()/4));
            //System.out.println(stgY()+", "+stgYToScrY(this.getHeight()/2f)+", "+stgYToScrY(this.getHeight()) );


            if (auto && (isCompletelyClosedY()||isCompletelyOpenedY())) auto = false;

            //if not autoSliding and touched and (input is above 950 when closed or above menuHeight when opened) and    ptr0X is on menu button   and  touchpads not touched
            if (!auto && isTouched()
                    && !UiHandler.aimPad.isVisible() && !UiHandler.movPad.isVisible() )  {

                if (!isTouched) {isTouched=true;UiHandler.isTouched=true;}
                //if closed, in zone and swiping down
                if (isCompletelyClosedY()&&Gdx.input.getDeltaY()<-2) showManually(true);// open = false, close = true;

                else if (isCompletelyOpenedY()&&Gdx.input.getDeltaY()>2) showManually(false);// open = true, close = false;
            } else if (isTouched) isTouched=false;
            //can compare to how it was when edge of menu followed finger in ui v10 git commit
            getStage().calculateScissors(areaBounds.set(camWidth/2f+offsetFromCenter-areaWidth/2f , 0, areaWidth, areaHeight), scissorBounds);

        } else if (originEdge.equals("left")) {
            //System.out.println("width"+this.getWidth()+", scrX:"+scrX());

            if ( menuButton.getX() > areaWidth / 2 && menuButton.getRotation() != 180 )
                menuButton.setRotation(180);
            else if (menuButton.getRotation() != 0 && menuButton.getX() < areaWidth / 2)
                menuButton.setRotation(0);

            updatePositionX();
            moveMenuButtonX();

            if (auto && (isCompletelyClosedX() || isCompletelyOpenedX())) auto = false;


            /*if (isTouched()){
                System.out.println("stgY: "+stgY()+", menuButtonTopEdge: "+(menuButton.getY()-menuButton.getHeight()*1.35)+", menuButtonBottEdge: "+(menuButton.getY()-menuButton.getHeight()/1.45));
            }//used to test if drag input is on button*/

            //System.out.println(stgY()+", "+(menuButton.getY()+menuButton.getHeight()/2));

            System.out.println(!auto+", "+isTouched()+", "+!UiHandler.movPad.isVisible());
            //if not autoSliding and touched and (input is above 950 when closed or above menuHeight when opened) and    stgY is on menu button   and  touchpads not touched
            if (!auto && isTouched()
                    && !UiHandler.movPad.isVisible()  ) {

                if (!isTouched) {isTouched=true;UiHandler.isTouched=true;}
                //if closed, in zone and swiping left
                if (isCompletelyClosedX() && Gdx.input.getDeltaX() > 2) showManually(true);// open = false, close = true;

                else if (isCompletelyOpenedX() && Gdx.input.getDeltaX() < -2) showManually(false);// open = true, close = false;
            } else if (isTouched) isTouched=false;
            //can compare to how it was when edge of menu followed finger in ui v10 git commit
            getStage().calculateScissors(areaBounds.set(0, camHeight/2f+offsetFromCenter-areaHeight/2f, areaWidth, areaHeight), scissorBounds);
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.flush();
        if (ScissorStack.pushScissors(scissorBounds)) {
            super.draw(batch, alpha);
            batch.flush();
            ScissorStack.popScissors();
        }
    }

    private void updatePositionX() {
        clamp.set(MathUtils.clamp(end.x, 0, this.getWidth()), 0);
        this.setPosition(clamp.x-getWidth(), camHeight/2-areaHeight/2f+offsetFromCenter);
        //System.out.println("Left slide menu: x: "+clamp.x+", y: "+clamp.y);
    }
    private void updatePositionY() {
        clamp.set(0, MathUtils.clamp(end.y, 0, this.getHeight()) );
        //System.out.println(clamp.y);
        this.setPosition(camWidth/2f-this.areaWidth/2f+2+offsetFromCenter, clamp.y-getHeight());
        //System.out.println("Down slide menu: x: "+clamp.x+", y: "+clamp.y);
    }

    private void noDrag() {
        // set end of X to updated X from clamp
        end.set(clamp);

        if (auto) {
            //System.out.println("updating");

            if (show) {
                if (originEdge.equals("down")) end.add(0, speed); // player want to OPEN drawer
                else if (originEdge.equals("left")) end.add(speed, 0);
            } else {
                    if (originEdge.equals("down")) end.sub(0, speed); // player want to CLOSE drawer
                    else if (originEdge.equals("left")) end.sub(speed, 0);
            }
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

    private float stgXToScrX(float x) {
        return getStage().stageToScreenCoordinates(posTap.set(x, 0)).x;
    }
    private float stgYToScrY(float y) {
        //System.out.println("stage to screen coords: "+y );
        return getStage().stageToScreenCoordinates(posTap.set(0, y)).y;
    }

    /*private float stgX() {
        return InputHandler.scaleX(Gdx.input.getX());
    }
    private float stgY() {
        return InputHandler.scaleY(Gdx.input.getY());
    }
    private float scrX() {
        return Gdx.input.getX();
    }
    private float scrY() {
        return Gdx.input.getY();
    }*/
    public float ptrX(int ptr){
        return InputHandler.scaleX(Gdx.input.getX(ptr));
    }
    public float ptrY(int ptr){
        return InputHandler.scaleY(Gdx.input.getY(ptr));
    }
    
    private boolean isTouched() {
        if (originEdge.equals("left")) System.out.println(getX()+getWidth()*1.7);
        return (Gdx.input.isTouched(0)&&(

                ( originEdge.equals("down") && ptrX(0)<getX()+getWidth() && ptrX(0)>getX() && ptrY(0)>getY() && ptrY(0)<getY()+getHeight()*1.7 )
                ||
                ( originEdge.equals("left") && ptrX(0)<getX()+getWidth()*1.8 && ptrX(0)>getX() && ptrY(0)>getY() && ptrY(0)<getY()+getHeight() )

                ))||(Gdx.input.isTouched(1)&&(

                ( originEdge.equals("down") && ptrX(1)<getX()+getWidth() && ptrX(1)>getX() && ptrY(1)>getY() && ptrY(1)<getY()+getHeight()*1.7 )
                ||
                ( originEdge.equals("left") && ptrX(1)<getX()+getWidth()*1.8 && ptrX(1)>getX() && ptrY(1)>getY() && ptrY(1)<getY()+getHeight() )
            )
        );
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
        if (originEdge.equals("left")){
            actor.setY((camHeight/2f +offsetFromCenter)- actor.getHeight()/2f);
        } else if (originEdge.equals("down")){
            actor.setX((camWidth/2f+offsetFromCenter)-actor.getWidth()/2f);
        }
        this.menuButton = actor;
        this.ismoveMenuButton = true;
        //this.moveMenuButtonX = rotation;
    }
}