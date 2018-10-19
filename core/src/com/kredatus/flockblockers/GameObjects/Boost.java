package com.kredatus.flockblockers.GameObjects;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */
/*
public class Boost extends Background {
    private Circle circle;
    public boolean isScored = false, isScrolledDown;

    public Boost(float x, float y, int width, int height) {
        //super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        circle = new Circle(x+width/2, y+height/2, width / 2);
    }

    public void update() {
        // Call the update method in the superclass (Background)
        super.update();

    }

    public void boostReset() {
        isScrolledDown = false;
        isScored=false;
    }

    public boolean collides(Glider glider) {
        if (x <= glider.getPosition().x + glider.getWidth()&& y-height<glider.getPosition().y+glider.getHeight()/2&&y+height*2>glider.getPosition().y) {
            return Intersector.overlaps(glider.getBoundingCircle(), circle);
        }
        return false;
    }

    public boolean isScored() {
        return isScored;
    }

    public void setScored(boolean b) {
        isScored = b;
    }
}*/