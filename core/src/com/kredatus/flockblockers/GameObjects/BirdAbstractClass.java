package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameWorld.GameWorld;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Erik Kredatus on 9/8/2018.
 *
 *                  Health Speed  Gold                        Size   Amount/Wave
 * FlockBird  =     2      M      4(400 / wave)               S      100
 * ThunderBird=     4      F      15(450)                     M      30
 * GoldBird   =     25     S      100(400)                    L      4
 * LunarBird  =     7      F      35(525)                     S      15
 * PhoenixBird=     50     S      30+Diamond                 XL      1

                                                          Damage      Fire Rate     Penetration       Spread
 (Fast Firing) bow, Pistol, Machinegun, Minigun, arty     2           L            M                  S
 (High Damage) crossbow, sniper, cannon, gauss cannon     4           S            L                  S
 (Wide Spread) catapult, shotgun, canister shot, missile  1           M            L                  XL
 */

public abstract class BirdAbstractClass {
    protected GameWorld world;
    protected float rotation;
    protected Circle boundingCircle;
    protected static Vector2 position;
    protected static Vector2 velocity;
    protected Vector2 acceleration;
    public float gamexvelocity;
    protected int width, height, camwidth, camheight, health;
    protected boolean isGone;
    public float  starty;
    protected boolean isAlive;
    protected Random r;
    protected OrthographicCamera cam;
    protected Animation frontFlaps, backFlaps, leftSideFlaps, rightSideFlaps;
    public BirdAbstractClass(int width, int height, OrthographicCamera cam, int camwidth, int camheight, int health) {
        position.set(r.nextInt(camwidth)+cam.position.x,r.nextInt(camheight)+cam.position.y);
        isAlive=true;
        this.cam=cam;

        this.camwidth = camwidth;
        this.camheight = camheight;
        isGone = false;
        isAlive = true;
        boundingCircle = new Circle();
    };

    public void update(float delta){

        //if (position.x)
        fly(delta);
    };

    public abstract void fly(float delta) ;

    public void die(float delta){
        velocity.y=30;
        dead(delta);
    }

    public final void load(){
        Texture sprites = new Texture(Gdx.files.internal("sprites/phoenixHD.png"));
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

        frontFlaps= new Animation<TextureRegion>(0.15f, front);
        frontFlaps.setPlayMode(Animation.PlayMode.LOOP);

        rightSideFlaps= new Animation<TextureRegion>(0.12f, side);
        rightSideFlaps.setPlayMode(Animation.PlayMode.LOOP);
        for (TextureRegion i : side){
            i.flip(true, false);
        }
        leftSideFlaps= new Animation<TextureRegion>(0.12f, side);
        leftSideFlaps.setPlayMode(Animation.PlayMode.LOOP);

        backFlaps= new Animation<TextureRegion>(0.12f, back);
        backFlaps.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void dead(float delta){
        velocity.add(acceleration.cpy().scl(delta));
        position.add(velocity.cpy().scl(delta));
        if ( position.y+height>cam.position.y-(camheight/2) && position.x-width<cam.position.x+(camwidth/2) && position.x+width>cam.position.x-(camwidth/2)){
            //bird.delete();
        }
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
