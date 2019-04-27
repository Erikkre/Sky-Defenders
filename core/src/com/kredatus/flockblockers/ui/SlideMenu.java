package com.kredatus.flockblockers.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;


public class SlideMenu extends Table {

    // only visual window and using scissor to avoid GPU to draw out of left-edge screen.
    private float areaWidth;
    private float areaHeight;
    private String originEdge;
    private float camWidth, camHeight;
    private final Rectangle areaBounds = new Rectangle();
    private final Rectangle scissorBounds = new Rectangle();

    // it's revealed with (widthStart = 60F;) when the user swipes a finger from the left edge of the screen with start touch.
    private float widthStart = 10f;
    // when the user swipes a finger from the right edge of the screen, it goes into off-screen after (widthBack = 20F;).
    private float widthBack = 0f;
    private float heightStart = 30f;
    private float heightBack = -20f;
    // speed of dragging
    private float speed = 5f;

    // some attributes to make real draggingX
    private Vector2 clamp = new Vector2();
    private Vector2 posTap = new Vector2();
    private Vector2 end = new Vector2();
    private Vector2 first = new Vector2();
    private Vector2 last = new Vector2();

    private boolean show = false;
    private boolean isTouched = false;
    private boolean isStart = false;
    private boolean isBack = false;
    private boolean auto = false;
    private boolean enableDrag = true;

    public void setAreaWidth(float areaWidth) {
        this.areaWidth = areaWidth;
    }

    public void setAreaHeight(float areaHeight) {
        this.areaHeight = areaHeight;
    }

    public SlideMenu(float width, float height, String originEdge, float camWidth, float camHeight) {
        this.areaWidth = width;
        this.areaHeight = height;
        this.originEdge = originEdge;
        this.setSize(width, height);
        this.camWidth=camWidth;
        this.camHeight=camHeight;
    }

    private SlideMenuListener listener;

    public interface SlideMenuListener {
        void moving(Vector2 clamp);
    }

    public void setSlideMenuListener(SlideMenuListener listener) {
        this.listener = listener;
    }

    /*public void setWidthstartDrag(float widthstartDragX) {
        this.widthStart = widthstartDragX;
    }

    public void setWidthBackDrag(float widthBackDrag) {
        this.widthBack = widthBackDrag;
    }*/

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void showManually(boolean show, float speed) {
        this.auto = true;
        this.show = show;
        this.speed = speed;
    }

    public void showManually(boolean show) {
        this.showManually(show, speed);
    }

    @Override
    public void draw(Batch batch, float alpha) {


        //System.out.println(clamp.y);
        if (originEdge.equals("down")) {
            if (menuButton.getY()>areaHeight/2&&menuButton.getRotation()!=270) menuButton.setRotation(270);
            else if ( menuButton.getRotation()!=90&&menuButton.getY()<areaHeight/2 ) menuButton.setRotation(90);

            getStage().calculateScissors(areaBounds.set(0, 0, areaWidth, areaHeight), scissorBounds);
            batch.flush();
            if (ScissorStack.pushScissors(scissorBounds)) {
                super.draw(batch, alpha);
                batch.flush();
                ScissorStack.popScissors();
            }

            if (isTouched() && inputY() > stgToScrCoordsY(0, this.getHeight()).y ) {
                System.out.println("************************************************************");
                auto = false;
                if (!isTouched) {
                    isTouched = true;
                    //System.out.println(scrToStgCoordsX(0, inputY()));
                    first.set(scrToStgCoordsY(0, inputY()));
                }
                last.set(scrToStgCoordsY(0, inputY())).sub(first);

                if (isCompletelyClosedY()) // open = false, close = true;
                    startDragY();

                if ((isStart || isBack) && enableDrag) // open = false, close =
                    // false;
                    if (inputY() < stgToScrCoordsY(0, heightStart).y)
                        draggingY();

                if (isCompletelyOpenedY()) // open = true, close = false;
                    backDrag();

            } else
                noDragY();


            updatePositionY();
            movingY();
            moveMenuButtonY();

        } else if (originEdge.equals("left")) {
            if (menuButton.getX()>areaWidth/2&&menuButton.getRotation()!=180) menuButton.setRotation(180);
            else if ( menuButton.getRotation()!=0&&menuButton.getX()<areaWidth/2 ) menuButton.setRotation(0);

            getStage().calculateScissors(areaBounds.set(0, 0, areaWidth, areaHeight), scissorBounds);
            batch.flush();
            if (ScissorStack.pushScissors(scissorBounds)) {
                super.draw(batch, alpha);
                batch.flush();
                ScissorStack.popScissors();
            }

            if (isTouched() && inputX() < stgToScrCoordsX(this.getWidth(), 0).x) {
                auto = false;
                if (!isTouched) {
                    isTouched = true;
                    first.set(scrToStgCoordsX(inputX(), 0));
                }
                last.set(scrToStgCoordsX(inputX(), 0)).sub(first);

                if (isCompletelyClosedX()) // open = false, close = true;
                    startDragX();

                if ((isStart || isBack) && enableDrag) // open = false, close =
                    // false;
                    if (inputX() > stgToScrCoordsX(widthStart, 0).x)
                        draggingX();

                if (isCompletelyOpenedX()) // open = true, close = false;
                    backDrag();

            } else
                noDragX();

            updatePositionX();
            movingX();
            moveMenuButtonX();
        }

        //fadeBackground();
    }

    private boolean isMax = false;
    private boolean isMin = false;

    private void movingX() {
        if (listener == null)
            return;
        if (!isCompletelyClosedX() && !isCompletelyOpenedX()) {
            listener.moving(clamp);
        } else {
            if (!isMax && isCompletelyOpenedX()) {
                isMax = true;
                isMin = false;
                listener.moving(clamp);
            }
            if (!isMin && isCompletelyClosedX()) {
                isMin = true;
                isMax = false;
                listener.moving(clamp);
            }
        }
    }
    private void movingY() {
        if (listener == null)
            return;
        if (!isCompletelyClosedY() && !isCompletelyOpenedY()) {
            listener.moving(clamp);
            System.out.println(77);
        } else {
            if (!isMax && isCompletelyOpenedY()) {
                isMax = true;
                isMin = false;
                listener.moving(clamp);
            }
            if (!isMin && isCompletelyClosedY()) {
                isMin = true;
                isMax = false;
                listener.moving(clamp);
            }
        }
    }

    private void updatePositionX() {
        clamp.set(MathUtils.clamp(end.x, 0, this.getWidth()), 0);
        this.setPosition(clamp.x, 0, Align.bottomRight);
        //System.out.println("Left slide menu: x: "+clamp.x+", y: "+clamp.y);
    }
    private void updatePositionY() {
        clamp.set(0, MathUtils.clamp(end.y, 0, this.getHeight()) );
        this.setPosition(0, clamp.y, Align.bottomRight);
        //System.out.println("Down slide menu: x: "+clamp.x+", y: "+clamp.y);
    }
    
    private void draggingX() {
        if (isStart)
            end.set(scrToStgCoordsX(inputX(), 0));

        if (isBack && last.x < -widthBack)
            end.set(last.add(this.getWidth() + widthBack, 0));
    }
    private void draggingY() {
        if (isStart)
            end.set(scrToStgCoordsY(0, inputY()));

        if (isBack && last.y < -heightBack)
            end.set(last.add(0, this.getHeight() + heightBack));
    }

    private void backDrag() {
        isStart = false;
        isBack = true;
        show = false;
    }

    private void startDragX() {
        // check if the player touch on the drawer to OPEN it.
        if (inputX() < stgToScrCoordsX(widthStart, 0).x) {
            isStart = true;
            isBack = false;
            hintToOpenX(); // hint to player if he want to open the drawer
        }
    }
    private void startDragY() {
        // check if the player touch on the drawer to OPEN it.
        if (inputY() < stgToScrCoordsY(0, heightStart).y) {
            isStart = true;
            isBack = false;
            hintToOpenY(); // hint to player if he want to open the drawer
        }
    }
    
    private void noDragX() {
        isStart = false;
        isBack = false;
        isTouched = false;
        // set end of X to updated X from clamp
        end.set(clamp);

        if (auto) {
            if (show)
                end.add(speed, 0); // player want to OPEN drawer
            else
                end.sub(speed, 0); // player want to CLOSE drawer
        } else {
            if (toOpenX())
                end.add(speed, 0); // player want to OPEN drawer
            else if (toCloseX())
                end.sub(speed, 0); // player want to CLOSE drawer
        }
    }
    private void noDragY() {
        isStart = false;
        isBack = false;
        isTouched = false;
        // set end of X to updated X from clamp
        end.set(clamp);

        if (auto) {
            if (show)
                end.add(0, speed); // player want to OPEN drawer
            else
                end.sub(0, speed); // player want to CLOSE drawer
        } else {
            if (toOpenY())
                end.add(0, speed); // player want to OPEN drawer
            else if (toCloseY())
                end.sub(0, speed); // player want to CLOSE drawer
        }
    }
    
    private void hintToOpenX() {
        end.set(stgToScrCoordsX(widthStart, 0));
    }
    private void hintToOpenY() {
        end.set(stgToScrCoordsY(0, heightStart));
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
    
    private boolean toOpenX() {
        return clamp.x > this.getWidth() / 2;
    }
    private boolean toOpenY() {
        return clamp.y > this.getHeight() / 2;
    }
    private boolean toCloseX() {
        return clamp.x < this.getWidth() / 2;
    }
    private boolean toCloseY() {
        return clamp.y < this.getHeight() / 2;
    }

    private Vector2 stgToScrCoordsX(float x, float y) {
        return getStage().stageToScreenCoordinates(posTap.set(x, y));
    }
    private Vector2 stgToScrCoordsY(float x, float y) {
        //System.out.println("stage to screen coords: "+y );
        return getStage().stageToScreenCoordinates(posTap.set(x, y )  );
    }

    private Vector2 scrToStgCoordsX(float x, float y) {
        return getStage().screenToStageCoordinates(posTap.set(x, y));
    }
    private Vector2 scrToStgCoordsY(float x, float y) {
        //System.out.println("screen to stage coords: "+ y );
        return getStage().screenToStageCoordinates(posTap.set(x, y) );
    }
    private float inputX() {
        return Gdx.input.getX();
    }

    private float inputY() {
        //System.out.println("InputY: "+ -(InputHandler.scaleY(Gdx.input.getY())-camHeight)+", stage y: "+-((stgToScrCoordsY(0, this.getHeight()).y)-camHeight));
        //-(InputHandler.scaleY(Gdx.input.getY())- camHeight)
        //System.out.println("InputY: "+Gdx.input.getY());
        return Gdx.input.getY();
    }
    
    private boolean isTouched() {
        return Gdx.input.isTouched();
    }

    private Actor menuButton = new Actor();
    private boolean isRotateMenuButton = false, ismoveMenuButton=false;
    private float menuButtonRotation = 0f;

    private void rotateMenuButton() {
        if (isRotateMenuButton)
            menuButton.setRotation(clamp.x / this.getWidth() * menuButtonRotation);
    }

    private void moveMenuButtonX() {
        if (ismoveMenuButton)
            menuButton.setPosition(clamp.x,menuButton.getY());
    }
    private void moveMenuButtonY() {
        if (ismoveMenuButton) {
            menuButton.setPosition(menuButton.getX(), clamp.y-35);
        }
    }
    
    /*public void setRotateMenuButton(Actor actor, float rotation) {
        this.menuButton = actor;
        this.isRotateMenuButton = true;
        this.menuButtonRotation = rotation;
    }*/

    public void setMoveMenuButton(Actor actor) {
        this.menuButton = actor;
        this.ismoveMenuButton = true;
        //this.moveMenuButtonX = rotation;
    }

    public void setEnableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
    }

    private Actor background = new Actor();
    private boolean isFadeBackground = false;
    private float maxFade = 1f;

    private void fadeBackground() {
        if (isFadeBackground)
            background.setColor(background.getColor().r, background.getColor().g, background.getColor().b,
                    MathUtils.clamp(clamp.x / this.getWidth() / 2, 0, maxFade));
    }

    public void setFadeBackground(Actor background, float maxFade) {
        this.background = background;
        this.isFadeBackground = true;
        this.maxFade = maxFade;
    }

}