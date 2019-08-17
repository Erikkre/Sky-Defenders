package com.kredatus.skydefenders.ui;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.kredatus.skydefenders.GameObjects.Airship;
import com.kredatus.skydefenders.GameWorld.GameHandler;
import com.kredatus.skydefenders.Handlers.InputHandler;
import com.kredatus.skydefenders.Handlers.UiHandler;

public class AppearOnTouchPad extends Widget {
    private com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle style;
    public boolean touched;
    boolean resetOnTouchUp;
    private float deadzoneRadius;
    private final Circle knobBounds;
    private final Circle touchBounds;
    private final Circle deadzoneBounds;
    private final Vector2 knobPosition;
    public final Vector2 knobPercent;

    public AppearOnTouchPad(float leftTouchBound, float rightTouchBound, float deadzoneRadius, Skin skin, boolean rotationTouchpad) {
        this(leftTouchBound,rightTouchBound, deadzoneRadius, skin.get(com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle.class),rotationTouchpad);
    }

    public AppearOnTouchPad(float leftTouchBound, float rightTouchBound, float deadzoneRadius, Skin skin, String styleName,boolean rotationTouchpad) {
        this(leftTouchBound,rightTouchBound, deadzoneRadius, skin.get(styleName, com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle.class),rotationTouchpad);
    }

    private Vector2 screenPos;
    private Vector2 localPos;
    private InputEvent fakeTouchDownEvent;
    private float leftTouchBound,rightTouchBound;
    public boolean rotationTouchpad;
    public AppearOnTouchPad thisTouchPad;
    public int currentPtr=-1;

    @Override
    public void act(float delta){
       //if (Gdx.input.justTouched()) System.out.println(ptr0X()+", "+GameHandler.camWidth/2);
        //if (this==UiHandler.movPad)System.out.println(!isVisible());
        //System.out.println(isVisible());
        if (Gdx.input.justTouched()&&!UiHandler.isTouched&&(!touched&&!thisTouchPad.isVisible())&&currentPtr==-1){

            if (this==UiHandler.movPad) {
                if ((UiHandler.aimPad.currentPtr==1||UiHandler.aimPad.currentPtr==-1)&&Gdx.input.isTouched(0)&&(ptr0X()>leftTouchBound&& ptr0X()<rightTouchBound)) {
                    System.out.println("movpad ptr=0");currentPtr=0;screenPos.set(Gdx.input.getX(0), Gdx.input.getY(0));
                } else if ((UiHandler.aimPad.currentPtr==0||UiHandler.aimPad.currentPtr==-1)&&Gdx.input.isTouched(1)&&(ptr1X()>leftTouchBound&& ptr1X()<rightTouchBound)){
                    System.out.println("movpad ptr=1");currentPtr=1;screenPos.set(Gdx.input.getX(1), Gdx.input.getY(1));
                }
            } else if (this==UiHandler.aimPad) {
                if ((UiHandler.movPad.currentPtr==1||UiHandler.movPad.currentPtr==-1)&&Gdx.input.isTouched(0)&&(ptr0X()>leftTouchBound&& ptr0X()<rightTouchBound)) {
                    System.out.println("aimPad ptr=0");currentPtr=0;screenPos.set(Gdx.input.getX(0), Gdx.input.getY(0));
                } else if ((UiHandler.movPad.currentPtr==0||UiHandler.movPad.currentPtr==-1)&&Gdx.input.isTouched(1)&&(ptr1X()>leftTouchBound&& ptr1X()<rightTouchBound)){
                    System.out.println("aimPad ptr=1");currentPtr=1;screenPos.set(Gdx.input.getX(1), Gdx.input.getY(1));
                }
            }
            if (thisTouchPad==UiHandler.aimPad)System.out.println("Should be a ptr= right above this");

            // Convert the touch point into local coordinates, place the touchpad and show it.
            if (currentPtr!=-1) {
                localPos.set(screenPos);
                localPos = this.getParent().screenToLocalCoordinates(localPos);
                this.setPosition(localPos.x - this.getWidth() / 2, localPos.y - this.getHeight() / 2);
                //System.out.println("setposition");
                // Fire a touch down event to get the touchpad working.
                Vector2 stagePos = this.getStage().screenToStageCoordinates(screenPos);
                fakeTouchDownEvent.setStageX(stagePos.x);
                fakeTouchDownEvent.setStageY(stagePos.y);
                this.fire(fakeTouchDownEvent);
            }
        } else if (isVisible()&&!isTouched()&&Airship.turretList.size()>0) { //only for rotationTouchpad
                //System.out.println(getY()+", "+ GameHandler.camHeight+", "+this.getStage().screenToStageCoordinates(new Vector2(getX(),0)).x+", "+GameHandler.camWidth);
            System.out.println("********************************************************************");
            calculatePositionAndValue(
                    knobBounds.x  -(float)Math.cos(Math.toRadians(Airship.turretList.get(0).targetRot))*100,
                    knobBounds.y  -(float)Math.sin(Math.toRadians(Airship.turretList.get(0).targetRot))*100,
                        false);
        }
    }

    public AppearOnTouchPad(float leftTouchBound, float rightTouchBound, float deadzoneRadius, com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle style, final boolean rotationTouchpad) {
        thisTouchPad=this;
        this.leftTouchBound=leftTouchBound;this.rightTouchBound=rightTouchBound;this.rotationTouchpad=rotationTouchpad;
        screenPos = new Vector2(0,0);
        localPos = new Vector2();
        fakeTouchDownEvent = new InputEvent();
        fakeTouchDownEvent.setType(InputEvent.Type.touchDown);

        this.resetOnTouchUp = false;
        this.knobBounds = new Circle(0.0F, 0.0F, 0.0F);
        this.touchBounds = new Circle(0.0F, 0.0F, 0.0F);
        this.deadzoneBounds = new Circle(0.0F, 0.0F, 0.0F);
        this.knobPosition = new Vector2();
        this.knobPercent = new Vector2();

        if (deadzoneRadius < 0.0F) {
            throw new IllegalArgumentException("deadzoneRadius must be > 0");
        } else {
            this.deadzoneRadius = deadzoneRadius;
            this.knobPosition.set(this.getWidth() / 2.0F, this.getHeight() / 2.0F);
            this.setStyle(style);
            this.setSize(this.getPrefWidth(), this.getPrefHeight());

            this.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (thisTouchPad==UiHandler.aimPad){System.out.println("touchDown for aimpad, currentptr:"+currentPtr+", eventpointer:"+pointer);}
                    else System.out.println("touchDown for movpad, currentptr:"+currentPtr+", eventpointer:"+pointer);
                        if (touched||pointer!=currentPtr) {
                            return false;
                        } else {
                            thisTouchPad.setVisible(true);
                            System.out.println("set visible");
                            if (!rotationTouchpad) {
                                touched = true;
                                calculatePositionAndValue(x, y, false);
                            }
                            return true;
                        }
                }

                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    //System.out.println("touchDrag");
                    if (thisTouchPad==UiHandler.aimPad){System.out.println("touchdrag for aimpad, currentptr:"+currentPtr+", eventPointer:"+pointer);}
                    else System.out.println("touchdrag for movpad, currentptr:"+currentPtr+", eventPointer:"+pointer);

                    if (!touched&&!deadzoneBounds.contains(x,y))touched=true;//lets aimpad aim once finger moves out of deadzone, otherwise let it follow Airship.turretList.get(0).targetRot
                    if (currentPtr==pointer&&touched) {

                        //if (thisTouchPad==UiHandler.aimPad)System.out.println("calculate aimpad pos");
                        calculatePositionAndValue(x, y, false);
                    }
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (thisTouchPad==UiHandler.aimPad){System.out.println("touchup for aimpad, currentptr:"+currentPtr+", eventPointer:"+pointer);}
                    else System.out.println("touchup for movpad, currentptr:"+currentPtr+", eventPointer:"+pointer);

                        if (pointer==currentPtr) {
                            touched = false;
                            currentPtr = -1;
                            setPosition(GameHandler.camWidth, GameHandler.camHeight);
                            thisTouchPad.setVisible(false);
                        }
                }
            });
        }
    }

    public float ptr1X(){
        return InputHandler.scaleX(Gdx.input.getX(1));
    }
    public float ptr0X(){
        return InputHandler.scaleX(Gdx.input.getX(0));
    }
    private float inputY() {
        return InputHandler.scaleY(Gdx.input.getY());
    }

    public void calculatePositionAndValue(float x, float y, boolean isTouchUp) {
        System.out.println(knobPercent);
        //System.out.println("calculate new pos");
        //System.out.println("knobPercent.x: "+knobPercent.x+", knobPercent.y: "+knobPercent.y);
        float oldPositionX = this.knobPosition.x;
        float oldPositionY = this.knobPosition.y;
        float oldPercentX = this.knobPercent.x;
        float oldPercentY = this.knobPercent.y;
        float centerX = this.knobBounds.x;
        float centerY = this.knobBounds.y;
        //System.out.println("Center: "+new Vector2(centerX,centerY)+"Current Knob Percent"+knobPercent+", currentX:"+x+",currentY:"+y);
        if (!rotationTouchpad) {
            this.knobPosition.set(centerX, centerY);
            this.knobPercent.set(0.0F, 0.0F);
        }

        if (!isTouchUp && !this.deadzoneBounds.contains(x, y)) {

            this.knobPercent.set((x - centerX) / this.knobBounds.radius, (y - centerY) / this.knobBounds.radius);

            float length = this.knobPercent.len();
            if (length > 1.0F) this.knobPercent.scl(1.0F / length);

            if (rotationTouchpad)
                this.knobPosition.set(this.knobPercent).nor().scl(this.knobBounds.radius).add(this.knobBounds.x, this.knobBounds.y);
            else {
                if (knobBounds.contains(x, y)) knobPosition.set(x, y);
                else  knobPosition.set(knobPercent).nor().scl(knobBounds.radius).add(knobBounds.x, knobBounds.y);
            }
        }
        if (oldPercentX != knobPercent.x || oldPercentY != knobPercent.y) {
            ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
            if (fire(changeEvent)) {
                knobPercent.set(oldPercentX, oldPercentY);
                knobPosition.set(oldPositionX, oldPositionY);
            }
            Pools.free(changeEvent);
        }
    }

    public void setStyle(com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("style cannot be null");
        } else {
            this.style = style;
            this.invalidateHierarchy();
        }
    }

    public com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle getStyle() {
        return this.style;
    }

    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && this.getTouchable() != Touchable.enabled) {
            return null;
        } else {
            return this.touchBounds.contains(x, y) ? this : null;
        }
    }

    public void layout() {
        float halfWidth = this.getWidth() / 2.0F;
        float halfHeight = this.getHeight() / 2.0F;
        float radius = Math.min(halfWidth, halfHeight);
        this.touchBounds.set(halfWidth, halfHeight, radius);
        if (this.style.knob != null) {
            radius -= Math.max(this.style.knob.getMinWidth(), this.style.knob.getMinHeight()) / 2.0F;
        }

        this.knobBounds.set(halfWidth, halfHeight, radius);
        this.deadzoneBounds.set(halfWidth, halfHeight, this.deadzoneRadius);
        this.knobPosition.set( this.style.knob.getMinWidth()/2,halfHeight);
        this.knobPercent.set(0.0F, 0.0F);
    }

    public void draw(Batch batch, float parentAlpha) {
        this.validate();
        Color c = this.getColor();
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
        float x = this.getX();
        float y = this.getY();
        float w = this.getWidth();
        float h = this.getHeight();
        Drawable bg = this.style.background;
        if (bg != null) {
            bg.draw(batch, x, y, w, h);
        }

        Drawable knob = this.style.knob;
        if (knob != null) {
            x += this.knobPosition.x - knob.getMinWidth() / 2.0F;
            y += this.knobPosition.y - knob.getMinHeight() / 2.0F;
            knob.draw(batch, x, y, knob.getMinWidth(), knob.getMinHeight());
        }

    }

    public float getPrefWidth() {
        return this.style.background != null ? this.style.background.getMinWidth() : 0.0F;
    }

    public float getPrefHeight() {
        return this.style.background != null ? this.style.background.getMinHeight() : 0.0F;
    }

    public boolean isTouched() {
        return this.touched;
    }

    public boolean getResetOnTouchUp() {
        return this.resetOnTouchUp;
    }

    public void setResetOnTouchUp(boolean reset) {
        this.resetOnTouchUp = reset;
    }

    public void setDeadzone(float deadzoneRadius) {
        if (deadzoneRadius < 0.0F) {
            throw new IllegalArgumentException("deadzoneRadius must be > 0");
        } else {
            this.deadzoneRadius = deadzoneRadius;
            this.invalidate();
        }
    }

    public float getKnobX() {
        return this.knobPosition.x;
    }

    public float getKnobY() {
        return this.knobPosition.y;
    }

    public float getKnobPercentX() {
        return this.knobPercent.x;
    }

    public float getKnobPercentY() {
        return this.knobPercent.y;
    }

}

