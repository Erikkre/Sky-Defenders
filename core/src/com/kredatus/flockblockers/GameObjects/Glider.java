package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Handlers.AssetHandler;

/**
 * Created by Mr. Kredatus on 8/26/2017.
 */

public class Glider {
    private float rotation;
    private Circle boundingCir;
    private static Vector2 position, vel, acceleration;
    public float gamexvel;
    protected int width;
    protected int height;
    protected boolean isScrolledDown;
    public float midpointY, midpointX, starty;
    private boolean isAlive;

    float bgh = AssetHandler.bgPhoenix.getHeight();
    float originalyacc=2000, originalGamevel=750;
    private GameWorld world;
    public Glider(float midPointX, float midPointY, int width, int height, GameWorld world) {
        this.midpointY = midPointY;
        starty= bgh / 8+20;
        this.midpointX= midPointX;
        this.width = width;
        this.height = height;
        this.world=world;
        position = new Vector2(midPointX, 0);
        vel = new Vector2(5000, -200);
        acceleration = new Vector2(6000, originalyacc-1600);
        gamexvel = originalGamevel;

        isScrolledDown = false;
        isAlive = true;
        boundingCir = new Circle();
    }

    public void update(float delta) {

        //gamexvel -= 0.13;
        //System.out.println("gv: "+ gamexvel + " sv: "+vel.x);
        if (isAlive()){
            if (betweenWorlds()) {      //between worlds
                if ( isPositionitive() ) {  //if partly in normal world
                    acceleration.y -= (originalyacc * ((float) Math.pow(position.y, 2))) / (((float) Math.pow(position.y, 2)) + ((float) Math.pow(originalyacc, 2)));
                } else { //if partly in flip world
                    acceleration.y += (originalyacc * ((float) Math.pow(position.y, 2))) / (((float) Math.pow(position.y, 2)) + ((float) Math.pow(originalyacc, 2)));
                }
            }
            if (vel.y > 600) {
                vel.y = 600;
            }
            if (vel.y < -600) {
                vel.y = -600;
            }

            //if (gamexvel<350){
            //    gamexvel=350;
            //}

            if (isPositionitive()) {
                if (acceleration.y < -originalyacc) {
                    acceleration.y = -originalyacc;
                }

                vel.add(acceleration.cpy().scl(delta));
                position.add(vel.cpy().scl(delta));

                if (acceleration.y < originalyacc) {
                    acceleration.y += (originalyacc - acceleration.y) / 50;
                }
                if (vel.x > gamexvel) {
                    vel.x -= (vel.x - gamexvel) / 30;
                }
                if (acceleration.x > 0) {
                    acceleration.x -= (acceleration.x) / 20;
                }
            }else if (isNegative()){  //+ is away from ground  -i is towards

                if (acceleration.y >  originalyacc) {
                    acceleration.y =  originalyacc;
                }
                vel.add(acceleration.cpy().scl(delta));
                position.add(vel.cpy().scl(delta));

                if (acceleration.y > - originalyacc) {
                    acceleration.y -= ( originalyacc + acceleration.y) / 50;
                }
                if (vel.x  > gamexvel) {
                    vel.x -= (vel.x - gamexvel) / 30;
                }
                if (acceleration.x > 0) {
                    acceleration.x -= (acceleration.x) / 20;
                }
            }
            rotation = -(float) Math.toDegrees(Math.atan(vel.y / (-vel.x*1.5)));
        } else {    //if drowning
            if (isPositionitive()) {
                vel.add(acceleration.cpy().scl(delta));
                position.add(vel.cpy().scl(delta));
                vel.y += 0.5;
                vel.x += 0.1;
            } else {
                vel.add(acceleration.cpy().scl(delta));
                position.add(vel.cpy().scl(delta));
                vel.y -= 0.5;
                vel.x += 0.1;
            }
            rotation = -(float) Math.toDegrees(Math.atan(vel.y / (-vel.x) ));
        }
        if (isAlive && (Math.abs(position.y+height/2)  > bgh - midpointY-20 )) {
            if (vel.y < 0 && isNegative() || vel.y > 0 && isPositionitive()){
            AssetHandler.stopmusic(AssetHandler.musiclist);
            AssetHandler.deathmenumusic.play();
            AssetHandler.splashdown.play();
            //GameRenderer.splashdown=true;
            die();
                }
        }
        boundingCir.set(position.x-width/2, position.y-height/2, height/2.1f);
    }

    public boolean isAlive() {
        return isAlive;
    }
    // Getters for instance variables
    public void onClick() {
        if (isAlive && world.boost>=1.0) {
            if (position.y < 0) {//+ is away from ground  -i is towards (flipworld)
                if (vel.y < -40) {   //if falling
                    acceleration.y += 1900 + (-vel.y * 2);
                    acceleration.x -= vel.y;
                    vel.y+=80;
                } else acceleration.y += 1700;
                vel.y+=40;
            } else {//- is away from ground  + is towards
                if (vel.y > 40) {   //if falling
                    acceleration.y -= 1900 + (vel.y * 2);
                    acceleration.x += vel.y;
                    vel.y-=80;
                } else acceleration.y -= 1600;
                vel.y-=40;
            }
            AssetHandler.swoop.play();
            world.boost--;
        }
    }

    public void die() {
        isAlive = false;
        if (isPositionitive()){
            acceleration.set(Math.abs(acceleration.x), Math.abs(acceleration.y)/2);
            vel.set(vel.x / 30, vel.y / 80);
        } else {
            acceleration.set(-Math.abs(acceleration.x), -Math.abs(acceleration.y)/2);
            vel.set(vel.x / 30, vel.y / 80);
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

    public boolean isPositionitive() {
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

    public Circle getboundingCir() {
        return boundingCir;
    }

    public void onRestart() {
        position.x=midpointX;
        position.y = starty;
        vel.x=5000;
        vel.y=-200;
        acceleration.x = 6000;
        acceleration.y = originalyacc-1600;
        gamexvel=originalGamevel;
        isAlive = true;
        //AssetHandler.frontViewFlaps.setFrameDuration(0.2f);
        rotation=0;
    }

    public void updateReady(float runTime) {
        position.y=starty;
        //position.y = 20 * (float) Math.sin(7.87 * runTime) + starty;
        position.x=midpointX;
    }
}