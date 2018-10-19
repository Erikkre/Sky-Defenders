package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.GlideOrDieHelpers.AssetLoader;
import com.kredatus.flockblockers.Screens.GameScreen;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class Background {
    // Protected is similar to private, but allows inheritance by subclasses.
    protected float x;
    public float y;
    protected int width;
    protected int height;
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

        if (y + height < -GameScreen.camheight / 2) {
            isScrolledDown = true;
        }
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y + addedY;
    }

    // Reset: Should Override in subclass for more specific behavior.
    public void reset(float newY, int bgNumber) {
        y = newY;
        texture=AssetLoader.bgList.get(bgNumber);
        isScrolledDown = false;}

    public boolean isScrolledDown() {return isScrolledDown;}

    public float getTailY() {return y + height;}

    public TextureRegion getTexture() {
        return texture;
    }

    public float getX() {return x;}

    public float getY() {return y;}

    public int getWidth() {return width;}

    public int getHeight() {return height;}
}

/*package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Mr. Kredatus on 8/31/2017.


public class Background extends Background{
    public int width, height;
    public Background(float x, float y, int width, int height, TextureRegion texture) {
        super(x, y, width, height, texture);
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }

    public void onRestart(float xpos, float ypos) {
        x = xpos;
        y = ypos;
    }
}*/