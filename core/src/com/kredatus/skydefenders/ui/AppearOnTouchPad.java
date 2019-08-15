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
import com.kredatus.skydefenders.GameWorld.GameHandler;
import com.kredatus.skydefenders.Handlers.InputHandler;

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
    public AppearOnTouchPad(float leftTouchBound, float rightTouchBound, float deadzoneRadius, com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle style,boolean rotationTouchpad) {
        this.leftTouchBound=leftTouchBound;this.rightTouchBound=rightTouchBound;this.rotationTouchpad=rotationTouchpad;
        screenPos = new Vector2(0,0);
        localPos = new Vector2();
        fakeTouchDownEvent = new InputEvent();
        fakeTouchDownEvent.setType(InputEvent.Type.touchDown);


        this.resetOnTouchUp = true;
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

                    if (touched) {
                        return false;
                    } else {
                        touched = true;
                        calculatePositionAndValue(x, y, false);
                        return true;
                    }
                }

                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    calculatePositionAndValue(x, y, false);
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    touched = false;
                    //calculatePositionAndValue(x, y, resetOnTouchUp);
                }
            });
        }
    }

    public float inputX(){
        return InputHandler.scaleX(Gdx.input.getX());
    }
    private float inputY() {
        return -(InputHandler.scaleY(Gdx.input.getY())- GameHandler.camHeight);
    }

    public void calculatePositionAndValue(float x, float y, boolean isTouchUp) {
        // System.out.println("UiTouched: "+!UiHandler.isTouched+", Screen touched: "+Gdx.input.justTouched()+", x:"+inputX()+", leftBound:"+leftTouchBound+", rightBound:"+rightTouchBound+", Movpad: "+!rotationTouchpad);
        if (!touched&&InputHandler.scaleX((int)x)>leftTouchBound&&InputHandler.scaleX((int)x)<rightTouchBound) {

            // Get the touch point in screen coordinates.
            screenPos.set(x, y);

            // Convert the touch point into local coordinates, place the touchpad and show it.
            localPos.set(screenPos);
            localPos = this.getParent().screenToLocalCoordinates(localPos);
            this.setPosition(localPos.x - this.getWidth() / 2, localPos.y - this.getHeight() / 2);
            this.setVisible(true);

            // Fire a touch down event to get the touchpad working.
            Vector2 stagePos = this.getStage().screenToStageCoordinates(screenPos);
            fakeTouchDownEvent.setStageX(stagePos.x);
            fakeTouchDownEvent.setStageY(stagePos.y);
            this.fire(fakeTouchDownEvent);


        } else if (!Gdx.input.isTouched()) {
            // The touch was just released, so hide the touchpad.
            System.out.println("hide touchpad");
            this.setVisible(false);
        }

        if (rotationTouchpad) {
            //System.out.println("knobPercent.x: "+knobPercent.x+", knobPercent.y: "+knobPercent.y);
            float oldPositionX = this.knobPosition.x;
            float oldPositionY = this.knobPosition.y;
            float oldPercentX = this.knobPercent.x;
            float oldPercentY = this.knobPercent.y;
            float centerX = this.knobBounds.x;
            float centerY = this.knobBounds.y;
            this.knobPosition.set(centerX, centerY);
            this.knobPercent.set(0.0F, 0.0F);
            if (!isTouchUp && !this.deadzoneBounds.contains(x, y)) {
                this.knobPercent.set((x - centerX) / this.knobBounds.radius, (y - centerY) / this.knobBounds.radius);
                float length = this.knobPercent.len();
                if (length > 1.0F) {
                    this.knobPercent.scl(1.0F / length);
                }
                this.knobPosition.set(this.knobPercent).nor().scl(this.knobBounds.radius).add(this.knobBounds.x, this.knobBounds.y);
            }

            if (oldPercentX != this.knobPercent.x || oldPercentY != this.knobPercent.y) {
                ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent) Pools.obtain(ChangeListener.ChangeEvent.class);
                if (this.fire(changeEvent)) {
                    this.knobPercent.set(oldPercentX, oldPercentY);
                    this.knobPosition.set(oldPositionX, oldPositionY);
                }

                Pools.free(changeEvent);
            }
        } else {
            float oldPositionX = knobPosition.x;
            float oldPositionY = knobPosition.y;
            float oldPercentX = knobPercent.x;
            float oldPercentY = knobPercent.y;
            float centerX = knobBounds.x;
            float centerY = knobBounds.y;
            knobPosition.set(centerX, centerY);
            knobPercent.set(0f, 0f);
            if (!isTouchUp) {
                if (!deadzoneBounds.contains(x, y)) {
                    knobPercent.set((x - centerX) / knobBounds.radius, (y - centerY) / knobBounds.radius);
                    float length = knobPercent.len();
                    if (length > 1) knobPercent.scl(1 / length);
                    if (knobBounds.contains(x, y)) {
                        knobPosition.set(x, y);
                    } else {
                        knobPosition.set(knobPercent).nor().scl(knobBounds.radius).add(knobBounds.x, knobBounds.y);
                    }
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

