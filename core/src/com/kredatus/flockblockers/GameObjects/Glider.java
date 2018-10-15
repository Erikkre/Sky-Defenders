package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.GlideOrDieHelpers.AssetLoader;

/**
 * Created by Mr. Kredatus on 8/26/2017.
 */

public class Glider {
    private float rotation;
    private Circle boundingCircle;
    private static Vector2 position, velocity, acceleration;
    public float gamexvelocity;
    protected int width;
    protected int height;
    protected boolean isScrolledDown;
    public float midpointY, midpointX, starty;
    private boolean isAlive;

    float bgh = AssetLoader.bgPhoenix.getHeight();
    float originalyacc=2000, originalGamevelocity=750;
    private GameWorld world;
    public Glider(float midPointX, float midPointY, int width, int height, GameWorld world) {
        this.midpointY = midPointY;
        starty= bgh / 8+20;
        this.midpointX= midPointX;
        this.width = width;
        this.height = height;
        this.world=world;
        position = new Vector2(midPointX, 0);
        velocity = new Vector2(5000, -200);
        acceleration = new Vector2(6000, originalyacc-1600);
        gamexvelocity = originalGamevelocity;

        isScrolledDown = false;
        isAlive = true;
        boundingCircle = new Circle();
    }

    public void update(float delta) {

        //gamexvelocity -= 0.13;
        //System.out.println("gv: "+ gamexvelocity + " sv: "+velocity.x);
        if (isAlive()){
            if (betweenWorlds()) {      //between worlds
                if ( isPositive() ) {  //if partly in normal world
                    acceleration.y -= (originalyacc * ((float) Math.pow(position.y, 2))) / (((float) Math.pow(position.y, 2)) + ((float) Math.pow(originalyacc, 2)));
                } else { //if partly in flip world
                    acceleration.y += (originalyacc * ((float) Math.pow(position.y, 2))) / (((float) Math.pow(position.y, 2)) + ((float) Math.pow(originalyacc, 2)));
                }
            }
            if (velocity.y > 600) {
                velocity.y = 600;
            }
            if (velocity.y < -600) {
                velocity.y = -600;
            }

            //if (gamexvelocity<350){
            //    gamexvelocity=350;
            //}

            if (isPositive()) {
                if (acceleration.y < -originalyacc) {
                    acceleration.y = -originalyacc;
                }

                velocity.add(acceleration.cpy().scl(delta));
                position.add(velocity.cpy().scl(delta));

                if (acceleration.y < originalyacc) {
                    acceleration.y += (originalyacc - acceleration.y) / 50;
                }
                if (velocity.x > gamexvelocity) {
                    velocity.x -= (velocity.x - gamexvelocity) / 30;
                }
                if (acceleration.x > 0) {
                    acceleration.x -= (acceleration.x) / 20;
                }
            }else if (isNegative()){  //+ is away from ground  -i is towards

                if (acceleration.y >  originalyacc) {
                    acceleration.y =  originalyacc;
                }
                velocity.add(acceleration.cpy().scl(delta));
                position.add(velocity.cpy().scl(delta));

                if (acceleration.y > - originalyacc) {
                    acceleration.y -= ( originalyacc + acceleration.y) / 50;
                }
                if (velocity.x  > gamexvelocity) {
                    velocity.x -= (velocity.x - gamexvelocity) / 30;
                }
                if (acceleration.x > 0) {
                    acceleration.x -= (acceleration.x) / 20;
                }
            }
            rotation = -(float) Math.toDegrees(Math.atan(velocity.y / (-velocity.x*1.5)));
        } else {    //if drowning
            if (isPositive()) {
                velocity.add(acceleration.cpy().scl(delta));
                position.add(velocity.cpy().scl(delta));
                velocity.y += 0.5;
                velocity.x += 0.1;
            } else {
                velocity.add(acceleration.cpy().scl(delta));
                position.add(velocity.cpy().scl(delta));
                velocity.y -= 0.5;
                velocity.x += 0.1;
            }
            rotation = -(float) Math.toDegrees(Math.atan(velocity.y / (-velocity.x) ));
        }
        if (isAlive && (Math.abs(position.y+height/2)  > bgh - midpointY-20 )) {
            if (velocity.y < 0 && isNegative() || velocity.y > 0 && isPositive()){
            AssetLoader.stopmusic(AssetLoader.musiclist);
            AssetLoader.deathmenumusic.play();
            AssetLoader.splashdown.play();
            //GameRenderer.splashdown=true;
            die();
                }
        }
        boundingCircle.set(position.x-width/2, position.y-height/2, height/2.1f);
    }

    public boolean isAlive() {
        return isAlive;
    }
    // Getters for instance variables
    public void onClick() {
        if (isAlive && world.boost>=1.0) {
            if (position.y < 0) {//+ is away from ground  -i is towards (flipworld)
                if (velocity.y < -40) {   //if falling
                    acceleration.y += 1900 + (-velocity.y * 2);
                    acceleration.x -= velocity.y;
                    velocity.y+=80;
                } else acceleration.y += 1700;
                velocity.y+=40;
            } else {//- is away from ground  + is towards
                if (velocity.y > 40) {   //if falling
                    acceleration.y -= 1900 + (velocity.y * 2);
                    acceleration.x += velocity.y;
                    velocity.y-=80;
                } else acceleration.y -= 1600;
                velocity.y-=40;
            }
            AssetLoader.swoop.play();
            world.boost--;
        }
    }

    public void die() {
        isAlive = false;
        if (isPositive()){
            acceleration.set(Math.abs(acceleration.x), Math.abs(acceleration.y)/2);
            velocity.set(velocity.x / 30, velocity.y / 80);
        } else {
            acceleration.set(-Math.abs(acceleration.x), -Math.abs(acceleration.y)/2);
            velocity.set(velocity.x / 30, velocity.y / 80);
        }
    }

    public boolean shouldntFlap() {
        return (position.y > bgh / 8 && acceleration.y > 50) || (position.y < -bgh / 8 && acceleration.y < -50);}

    public boolean betweenWorlds() {
        return Math.abs(position.y) < bgh / 8;
    }

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

    public void onRestart() {
        position.x=midpointX;
        position.y = starty;
        velocity.x=5000;
        velocity.y=-200;
        acceleration.x = 6000;
        acceleration.y = originalyacc-1600;
        gamexvelocity=originalGamevelocity;
        isAlive = true;
        AssetLoader.frontViewFlaps.setFrameDuration(0.2f);
        rotation=0;
    }

    public void updateReady(float runTime) {
        position.y=starty;
        //position.y = 20 * (float) Math.sin(7.87 * runTime) + starty;
        position.x=midpointX;
    }
}