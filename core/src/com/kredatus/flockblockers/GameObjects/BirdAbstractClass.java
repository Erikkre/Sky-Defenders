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
 *                  Health Speed  Size  Gold             Amount/Wave
 * FlockBird  =     3      M      S      4(400 / wave)   60
 * ThunderBird=     7      XF     M      15(450)         20
 * AcidBird   =     7      F      M      15(450)         20
 * FireBird   =     7      F      S      30(600)         20
 * LunarBird  =     7      XF     S      50(750)         15
 * GoldBird   =     25     S      L      100(400)        4
 * PhoenixBird=     100    S      XL     7+Diamond       1

 Gun upgrades work as: each can be upgraded once, to upgrade to level 2 must upgrade whole turret, see .excel file (Dmg, RoF, Pen)

                                                                                                     Damage      Fire Rate     Penetration   Spread
 (Fast Firing) knife thrower,    bow,        submachinegun,  assault rifle,    Machinegun,  Minigun, laser,  ion cannon        2           L             M
 (High Damage) spear thrower,    crossbow,   ballistae,  hunting rifle,  anti-tank sniper, cannon,      gauss cannon           4           S             L
 (Wide Spread) shuriken thrower, tripleShot, tripleCatapult, shotgun, blunderbuss, missile, Microwave emitter                  1           M             L             XL

 1 Diamond=10000 Gold
 Powerups: Climate Cooling(10 diamonds), Overclock Turrets(20 diamonds), Nuclear Bomb (50 Diamonds)
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
    protected Animation frontFlaps, backFlaps, leftFlaps, rightFlaps;

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

        if (position.y<cam.position.y+camheight/2){
            fly(delta);
        }


    };

    public abstract void fly(float delta) ;

    public void die(float delta){
        velocity.y=30;
        dead(delta);
    }

    public final void load(String path){
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

        frontFlaps= new Animation<TextureRegion>(0.15f, front);
        frontFlaps.setPlayMode(Animation.PlayMode.LOOP);

        rightFlaps= new Animation<TextureRegion>(0.12f, side);
        rightFlaps.setPlayMode(Animation.PlayMode.LOOP);

        for (TextureRegion i : side){
            i.flip(true, false);
        }
        leftFlaps= new Animation<TextureRegion>(0.12f, side);
        leftFlaps.setPlayMode(Animation.PlayMode.LOOP);

        backFlaps= new Animation<TextureRegion>(0.12f, back);
        backFlaps.setPlayMode(Animation.PlayMode.LOOP);
    }

    public final void hit(Bullet bullet){
        health-=bullet.damage;
    }

    public void dead(float delta){
        velocity.add(acceleration.cpy().scl(delta));
        position.add(velocity.cpy().scl(delta));
        if ( position.y+height>cam.position.y-(camheight/2) && position.x-width<cam.position.x+(camwidth/2) && position.x+width>cam.position.x-(camwidth/2)){
            //delete
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
