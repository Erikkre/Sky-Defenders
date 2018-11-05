package com.kredatus.flockblockers.GameObjects;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Handlers.GameHandler;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Bullet  {
    public Polygon boundingRect;
    public boolean isScored = false;
    protected int width, height;
    protected boolean isGone;
    private Vector2 position, velocity;
    protected float damage, camWidth, camHeight, rotation;

    public Bullet(Vector2 position, Vector2 velocity, int width, int height, float camWidth, float camHeight) {
        this.width = width;
        this.height = height;
        this.position = position;
        this.velocity = velocity;

        this.camWidth = camWidth;
        this.camHeight = camHeight;
        boundingRect = new Polygon(new float[]{position.x - width / 2, position.y - height / 2, position.x + width / 2, position.y + height / 2});
        boundingRect.setOrigin(0, 0);
        rotation = -(float) Math.toDegrees(Math.atan(velocity.y / (-velocity.x) ));
        boundingRect.setRotation(rotation);
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
        boundingRect.setPosition(position.x,position.y);

        if (position.x + height <  - camWidth / 2 || position.x - height > camWidth / 2 ||
                position.y + height < - camHeight / 2 || position.y - height > camHeight / 2 ) {
            isGone = true;
        }
    }

    public void boostReset() {
        isGone = false;
        isScored=false;
    }



    public boolean isScored() {
        return isScored;
    }

    public void setScored(boolean b) {
        isScored = b;
    }
}