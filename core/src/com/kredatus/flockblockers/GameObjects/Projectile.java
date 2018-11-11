package com.kredatus.flockblockers.GameObjects;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Projectile {
     public Polygon boundingRect;
     private boolean isScored = false;
     protected int width, height ;
     private boolean isGone;
     private Vector2 position, velocity;
     private float camWidth, camHeight;
     private double rotation;
    public float dmg, rof, pen, vel;
    public Projectile(float dmg, float rof, float pen, Vector2 position, int width, int height, float camWidth, float camHeight, double rotation) {
        this.width    = width    ;
        this.height   = height   ;
        this.position = position ;
        this.vel      = pen;
        this.rotation = rotation;
        this.camWidth = camWidth ;
        this.camHeight= camHeight;

        this.dmg=dmg; this.rof=rof; this.pen=pen;
        //sin(rotation)=xVel/Velocity, pen=velocity
        velocity.set((float)(pen*Math.sin(Math.toRadians(rotation))),(float)(pen*Math.cos(Math.toRadians(rotation))));

        boundingRect  = new Polygon(new float[]{position.x-width/2,position.y-height/2,         position.x+width/2,position.y-height/2,         position.x+width/2,position.y+height/2,         position.x-width/2,position.y+height/2});
        boundingRect  . setOrigin(position.x, position.y);
//        rotation      = -(float) Math.toDegrees(Math.atan(velocity.y / (-velocity.x) ));
        boundingRect  . setRotation((float)rotation);
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
        boundingRect.setPosition(position.x,position.y);

        if    ( position.x + height < - camWidth  / 2 || position.x - height > camWidth  / 2  ||
                position.y + height < - camHeight / 2 || position.y - height > camHeight / 2 )  {
            isGone = true;
        }
    }

    public void boostReset() {
        isGone  =false;
        isScored=false;
    }

    public boolean isScored() {
           return isScored;
    }

    public void setScored(boolean b) {
        isScored = b;
    }
}