package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Helpers.FloatAccessor;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Erik Kredatus on 9/8/2018.
 *
 *                  Health Speed  Size  Gold             Amount/Wave
 * FlockBird  =     3      S      S      8(480 / wave)   60
 * WaterBird  =     3      M      S      12(480)         40
 * AcidBird   =     7      F      M      15(300)         20
 * FireBird   =     7      F      S      15(300)         20
 * ThunderBird=     7      XF     M      15(300)         20
 * LunarBird  =     7      XF     S      50(500)         10
 * GoldBird   =     25     S      L      100(500)        5
 * PhoenixBird=     100    S      XL     7+Diamond       1

 Gun upgrades work as: each can be upgraded once, to upgrade to level 2 must upgrade whole turret, see .excel file (Dmg, RoF, Pen)

                                                                                                                                Damage      Fire Rate     Penetration   Spread
 (Fast Firing) knife thrower,    bow,        submachinegun,  assault rifle,    Machinegun,  Minigun, laser,  ion cannon        2           L             M
 (High Damage) spear thrower,    crossbow,   ballistae,  hunting rifle,  anti-tank sniper, cannon,      gauss cannon           4           S             L
 (Wide Spread) shuriken thrower, tripleShot, tripleCatapult, shotgun, blunderbuss, missile, Microwave emitter                  1           M             L             XL

 1 Diamond=10000 Gold
 Powerups: Climate Cooling(5 diamonds), Overclock Turrets(15 diamonds), Nuclear Bomb (25 Diamonds)
 */

public abstract class BirdAbstractClass {
    protected GameWorld world;
    protected Circle boundingCircle;
    public float x, y, yVel, yAcc, xVel, rotation;

    private final float edge;
    public int width, height;
    protected double camWidth, camHeight;
    public boolean isOffCam;
    public float  starty;
    protected boolean isAlive;
    protected Random r =new Random();
    public Animation frontFlaps, backFlaps, leftFlaps, rightFlaps, animation;
    protected int sizeVariance, coins, health, diamonds, counter;
    protected Tween xMotion;

    public BirdAbstractClass( float camHeight, float camWidth) {
        edge = (camWidth)-width/2;

        x= width/2 + r.nextInt((int)edge-width/2);
        y=0;
        isAlive=true;

        this.camWidth = camWidth;
        this.camHeight = camHeight;
        isOffCam = false;
        isAlive = true;
        //this.manager=manager;
        boundingCircle = new Circle();
        Tween.registerAccessor(Float.class, new FloatAccessor());
        setManager(camWidth, edge);
    };

    public abstract void setManager(float camWidth, float edge);

    //public abstract void fly(float delta) ;

    public void update(float delta){
        xVel=x;
        xMotion.update(delta);
        //manager.update(delta);
        xVel=x-xVel;    //rate of change of x (next tweened value - last value)
        //rotation = -(float) Math.toDegrees(Math.atan(yVel / -xVel ));
        y+=yVel;
        if (isAlive) {
            if (health <= 0) {
                die();
            }
            if (y > camHeight - 0) { //0 being height of top of tower where score & diamonds are
                health=0;
                GameWorld.addGold(-coins);
                isOffCam=true;
            }
        } else {
            yVel+=yAcc;
            x+=xVel;

            if (y+height/2<-(0) || x+width/2< 0 || x-width/2> camWidth){
                isOffCam=true;
            }
        }
    };



    public void die(){
        xMotion.kill();
        isAlive=false;
        animation=frontFlaps;
        animation.setFrameDuration(0.1f);

        yAcc=-5;
        yVel=40;
        if (x>0){   //if dying on right side fall to left and vice versa
            xVel=-2;
        } else {
            xVel=2;
        }
    }

    public final void load(String path, float flapSpeed){
        Texture sprites = new Texture(Gdx.files.internal(path));
        sprites.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        ArrayList<TextureRegion> positions = new ArrayList<TextureRegion>();

        TextureRegion[] front=new TextureRegion[0];
        TextureRegion[] side=new TextureRegion[0];
        TextureRegion[] back=new TextureRegion[0];

        for (int i=0;i<16;i++) {

            TextureRegion temp = new TextureRegion(sprites, 481 * i, 0, 481, 423);
            temp.flip(false, true);
            positions.add(temp);
            if (i == 5) {
                front =  positions.toArray(new TextureRegion[6]);
                positions.clear();
            } else if (i == 11){
                side = positions.toArray(new TextureRegion[6]);
                positions.clear();
            } else if (i==15){
                back = positions.toArray(new TextureRegion[4]);
                positions.clear();
            }
        }

        frontFlaps= new Animation<TextureRegion>(flapSpeed, front);
        frontFlaps.setPlayMode(Animation.PlayMode.LOOP);

        rightFlaps= new Animation<TextureRegion>(flapSpeed, side);
        rightFlaps.setPlayMode(Animation.PlayMode.LOOP);

        for (TextureRegion i : side){
            i.flip(true, true);

        }
        leftFlaps= new Animation<TextureRegion>(flapSpeed, side);
        leftFlaps.setPlayMode(Animation.PlayMode.LOOP);

        backFlaps= new Animation<TextureRegion>(flapSpeed, back);
        backFlaps.setPlayMode(Animation.PlayMode.LOOP);

        height=back[3].getRegionHeight();
        width=back[0].getRegionWidth();
        animation=frontFlaps;
    }

    public final void hit(Bullet bullet){
        health-=bullet.damage;
    }

    public void dead(float delta){

    }

/*
    public float distanceAfterDeath() {
        return Math.abs(position.y+height/2) - (bgh - midpointY);
    }

    public boolean isFlipWorld() {
        return position.y < -bgh / 8;
    }

    public boolean isNormalWorld() {
        return position.y > bgh / 8;
    }

    public boolean isPositive() {
        return position.y >0;
    }

    public boolean isNegative() {
        return position.y <0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRotation() {
        return rotation;
    }

    public Circle getBoundingCircle() {
        return boundingCircle;
    }
    */

}
