package com.kredatus.flockblockers.GameObjects;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Projectile {
     public Polygon boundingRect;
     public int width, height ;
     public boolean isGone;
     public Vector2 position, velocity=new Vector2();
     public float camWidth, camHeight, rotation;
     public float dmg, rof, pen, vel;
     public TextureRegion texture;
    public Projectile(TextureRegion texture, float dmg, float pen, Vector2 position, float camWidth, float camHeight, float rotation) {
        this.texture=texture;
        this.width    = texture.getRegionWidth() ;
        this.height   = texture.getRegionHeight() ;
        this.position = position.cpy() ;
        this.vel      = pen*15;
        this.rotation = rotation;
        this.camWidth = camWidth ;
        this.camHeight= camHeight;

        this.dmg=dmg; this.pen=pen;
        //sin(rotation)=xVel/Velocity, pen=velocity


            velocity.set(   -(float)(vel*Math.cos(Math.toRadians(rotation))), -(float)(vel*Math.sin(Math.toRadians(rotation)))   );

        //System.out.println("Velocity: "+velocity);

        boundingRect  = new Polygon(new float[]{position.x-width/2,position.y-height/2,         position.x+width/2,position.y-height/2,         position.x+width/2,position.y+height/2,         position.x-width/2,position.y+height/2});
        boundingRect  . setOrigin(position.x, position.y);
//        rotation      = -(float) Math.toDegrees(Math.atan(velocity.y / (-velocity.x) ));
        boundingRect  . setRotation(rotation);
        //System.out.println("Bullet fired");
    }

    public void update(float delta) {
        //System.out.println(this.toString()+"'s position: "+position);
        position.add(velocity.cpy());   //.cpy.scl is so we scale copy not original
        boundingRect.translate(velocity.x, velocity.y);
        //System.out.println(position);
        if    ( position.x +  width/2 < 0 ||  position.x - width/2 > camWidth  ||
                position.y + height/2 < 0 || position.y - height/2 > camHeight   )  {
            isGone = true;
        }
    }
}