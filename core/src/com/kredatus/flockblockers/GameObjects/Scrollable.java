package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Screens.GameScreen;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.Screens.GameScreen;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Scrollable {
    // Protected is similar to private, but allows inheritance by subclasses.
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected boolean isScrolledDown;
    private TextureRegion texture;

    public Scrollable(float x, float y, int width, int height, TextureRegion texture) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        isScrolledDown = false;
        this.texture=texture;
    }

    public void update() {
        // If the Scrollable object is no longer visible:
        //try {
        //System.out.println("scrollhandler edge:"+(GameRenderer.getCameraPosition().x - GameScreen.camwidth / 2));
        if (y + height <  GameScreen.camheight / 2) {
            isScrolledDown = true;}
    }


    // Reset: Should Override in subclass for more specific behavior.
    public void reset(float newY) {
        y = newY;
        isScrolledDown = false;}

    public boolean isScrolledDown() {return isScrolledDown;}

    public float getTailY() {return x + width;}

    public TextureRegion getTexture() {
        return texture;
    }

    public float getX() {return x;}

    public float getY() {return y;}

    public int getWidth() {return width;}

    public int getHeight() {return height;}
}
