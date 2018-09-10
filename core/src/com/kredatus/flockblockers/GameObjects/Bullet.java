package com.kredatus.flockblockers.GameObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Screens.GameScreen;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Bullet  {
    private Rectangle rectangle;
    public boolean isScored = false;
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected boolean isGone;


    public Bullet(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rectangle= new Rectangle(x+width/2, y+height/2, width, height);
    }

    public void update() {
        if (x + width < GameRenderer.getCameraPosition().x - GameScreen.camwidth / 2 || x - width > GameRenderer.getCameraPosition().x + GameScreen.camwidth / 2 ||
                y + height < GameRenderer.getCameraPosition().y - GameScreen.camheight / 2 || y - height > GameRenderer.getCameraPosition().y - GameScreen.camheight / 2 ) {
            isGone = true;}
    }

    public void boostReset() {
        isGone = false;
        isScored=false;
    }

    public boolean collides(Glider glider) {
        if (x <= glider.getPosition().x + glider.getWidth()&& y-height<glider.getPosition().y+glider.getHeight()/2&&y+height*2>glider.getPosition().y) {
            return Intersector.overlaps(glider.getBoundingCircle(), rectangle);
        }
        return false;
    }

    public boolean isScored() {
        return isScored;
    }

    public void setScored(boolean b) {
        isScored = b;
    }
}