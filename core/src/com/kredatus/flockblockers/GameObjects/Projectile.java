// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects;
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
     public float dmg, pen, spd;
     public TextureRegion texture;
     private Random r=new Random();
    public Projectile(TextureRegion texture, float dmg, float pen, Vector2 position, float camWidth, float camHeight, float rotation, int acc) {
        this.texture=texture;
        this.width    = texture.getRegionWidth() ;
        this.height   = texture.getRegionHeight() ;
        this.position = position.cpy() ;
        this.spd      = pen*2+4;

        this.rotation = rotation -(acc/2)+r.nextInt(acc);   //accuracy
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


        boundingRect  = new Polygon(new float[]{position.x-width/2,position.y-height/2,         position.x+width/2,position.y-height/2,         position.x+width/2,position.y+height/2,         position.x-width/2,position.y+height/2});
        boundingRect  . setOrigin(position.x, position.y);
//        rotation      = -(float) Math.toDegrees(Math.atan(spd.y / (-spd.x) ));
        boundingRect  . setRotation(this.rotation);
        //System.out.println("Bullet fired");
    }

    public void update(float delta) {
        position.add(vel.cpy());
        boundingRect.translate(vel.x, vel.y);
        if    ( position.x +  width/2 < 0 ||  position.x - width/2 > camWidth  ||
                position.y + height/2 < 0 || position.y - height/2 > camHeight   )  {
            isGone = true;
        }
    }
}