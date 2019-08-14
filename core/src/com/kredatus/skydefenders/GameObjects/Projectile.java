// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Projectile {
     public Polygon boundingRect;
     public int width, height ;
     public boolean isGone;
     public Vector2 position, vel=new Vector2();
     public float camWidth, camHeight, rotation;
     public float dmg, pen, spd, xPosOffset;
     public TextureRegion texture;
     private Random r=new Random();
     public boolean isRotating;
    public Projectile(TextureRegion texture, float dmg, float pen, Vector2 position, float camWidth, float camHeight, float rotation, int acc, boolean isRotating, float xPosOffset ) {
        this.xPosOffset=xPosOffset;
        this.isRotating=isRotating;
        this.texture=texture;
        this.width    = texture.getRegionWidth() ;
        this.height   = texture.getRegionHeight() ;
        this.position = position ;
        this.spd      = pen*2+4;

        this.rotation = rotation -(acc/2f)+r.nextInt(acc);   //accuracy
        if (this.rotation>360){
            this.rotation-=360;
        } else if (rotation<0) {
            this.rotation+=360;
        }
        this.camWidth = camWidth ;
        this.camHeight= camHeight;

        this.dmg=dmg; this.pen=pen;
        //sin(rotation)=xspd/spd, pen=spd

        vel.set(     -(float)(spd*Math.cos(Math.toRadians(this.rotation))),    -(float)(spd*Math.sin(Math.toRadians(this.rotation)))    );


        boundingRect  = new Polygon(new float[]{position.x-width/2f,position.y-height/2f,         position.x+width/2f,position.y-height/2f,         position.x+width/2f,position.y+height/2f,         position.x-width/2f,position.y+height/2f});
        boundingRect  . setOrigin(position.x, position.y);
//        rotation      = -(float) Math.toDegrees(Math.atan(spd.y / (-spd.x) ));
        boundingRect  . setRotation(this.rotation);
        //System.out.println("Bullet fired");
    }

    public void update() {
        position.add(vel.cpy());
        boundingRect.translate(vel.x, vel.y);
        if (isRotating) {rotation+=10;boundingRect.rotate(rotation);}
        if    ( position.x +  width/2f < 0 ||  position.x - width/2f > camWidth  ||
                position.y + height/2f < 0 || position.y - height/2f > camHeight   )  {
            isGone = true;
        }
    }
}