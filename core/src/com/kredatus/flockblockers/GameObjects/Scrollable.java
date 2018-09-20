package com.kredatus.flockblockers.GameObjects;

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
    protected boolean isScrolledLeft;

    public Scrollable(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        isScrolledLeft = false;
    }

    public void update() {

        // If the Scrollable object is no longer visible:
        //try {
        //System.out.println("scrollhandler edge:"+(GameRenderer.getCameraPosition().x - GameScreen.camwidth / 2));
        if (x + width < GameRenderer.getCameraPosition().x - GameScreen.camwidth / 2) {
            isScrolledLeft = true;}
    }

    // Reset: Should Override in subclass for more specific behavior.
    public void reset(float newX) {
        x = newX;
        isScrolledLeft = false;}

    public boolean isScrolledLeft() {return isScrolledLeft;}

    public float getTailX() {return x + width;}

    public float getX() {return x;}

    public float getY() {return y;}

    public int getWidth() {return width;}

    public int getHeight() {return height;}
}
