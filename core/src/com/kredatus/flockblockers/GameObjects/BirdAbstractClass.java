package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import java.util.Random;

/**
 * Created by Erik Kredatus on 9/8/2018.
 *                  Health Speed  Gold                        Size   Amount
 * FlockBird  =     2      M      4(400)                      S      100
 * ThunderBird=     4      F      15(450)                     M      30
 * GoldBird   =     25     S      100(400)                    L      4
 * LunarBird  =     7      F      35(525)                     S      15
 * PhoenixBird=     50     S      30++Diamond                 XL      1


                                            Damage      Fire Rate     Penetration       Spread
 bow, Pistol, Machinegun, Minigun, arty     2           L            M                  S
 crossbow, sniper, cannon, gauss cannon     4           S            L                  S
 catapult, shotgun, canister shot, missile  1           M            L                  XL
 */





public abstract class BirdAbstractClass {
    protected GameWorld world;
    protected float rotation;
    protected Circle boundingCircle;
    protected static Vector2 position;
    protected static Vector2 velocity;
    protected Vector2 acceleration;
    public float gamexvelocity;
    protected int width, height, sizeVariance, camwidth, camheight;
    protected boolean isGone;
    public float  starty;
    protected boolean isAlive;
    protected Random r;
    protected OrthographicCamera cam;

    public BirdAbstractClass(int width, int height, OrthographicCamera cam, int camwidth, int camheight) {
        position.set(r.nextInt(camwidth)+cam.position.x,r.nextInt(camheight)+cam.position.y);
        isAlive=true;
        this.cam=cam;

        this.camwidth = camwidth;
        this.camheight = camheight;
        //this.sizeVariance=sizeVariance;
        //this.world=world;
        isGone = false;
        isAlive = true;
        boundingCircle = new Circle();
    };

    public abstract void update(float delta) ;


    public void die(float delta){
        velocity.y=30;
        while (position.y+height>cam.position.y-(camheight/2)){
            velocity.add(acceleration.cpy().scl(delta));
            position.add(velocity.cpy().scl(delta));
        }
    }
    public void dead(float delta){

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

}
