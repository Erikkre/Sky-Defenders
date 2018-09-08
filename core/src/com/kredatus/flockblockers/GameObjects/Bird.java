package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameWorld.GameWorld;

import java.util.Random;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public abstract class Bird {
    private GameWorld world;
    private float rotation;
    private Circle boundingCircle;
    private static Vector2 position;
    private static Vector2 velocity;
    private Vector2 acceleration;
    public float gamexvelocity;
    protected int width, height, sizeVariance, camwidth, camheight;
    protected boolean isGone;
    public float  starty;
    private boolean isAlive;
    private Random r;
    private OrthographicCamera cam;

    public Bird(int width, int height, OrthographicCamera cam, int camwidth, int camheight) {

        starty= 0;
        isAlive=true;
        this.cam=cam;
        this.width = width;
        this.height = height;
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
}
