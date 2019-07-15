// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.Screens.Loader;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Background {

    public float x, y;
    protected int width;
    public int height;
    private boolean isScrolledDown;
    public TextureRegion texture;
    public float addedY;
    public Background(float x, float y, int width, int height, TextureRegion texture) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        isScrolledDown = false;
        this.texture=texture;
    }

    public void update() {
        if (y + height < 0) {  //-GameHandler.camHeight / 2 is a buffer of half the camera height
            height=texture.getRegionHeight();
            isScrolledDown = true;
        }
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y + addedY;
    }
    public void setYToTail(float y) {
        this.y = y;
    }

    // Reset: Should Override in subclass for more specific behavior.
    public void reset(float newY, int bgNumber) {
        y = newY;

        texture= Loader.bgList.get(bgNumber);
        height=texture.getRegionHeight();
        isScrolledDown = false;
    }

    public boolean isScrolledDown() {
        return isScrolledDown;}

    public float getTailY() {return y + height;}

    public TextureRegion getTexture() {
        return texture;
    }

    public float getX() {return x;}

    public float getY() {return y;}

    public int getWidth() {return width;}

    public int getHeight() {return height;}
}
